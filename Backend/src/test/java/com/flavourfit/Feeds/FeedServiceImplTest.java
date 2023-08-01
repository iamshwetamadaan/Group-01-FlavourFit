package com.flavourfit.Feeds;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Feeds.Comments.CommentDto;
import com.flavourfit.Feeds.Comments.CommentServiceImpl;
import com.flavourfit.Feeds.Comments.ICommentsService;
import com.flavourfit.Helpers.FeedHelper;
import com.flavourfit.Recipes.CompleteRecipeDto;
import com.flavourfit.Recipes.IRecipeService;
import com.flavourfit.Recipes.RecipeDto;
import com.mysql.cj.xdevapi.JsonString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FeedServiceImplTest {
    @Mock
    private IFeedDao feedDao;

    @InjectMocks
    private FeedServiceImpl feedService;
    @Mock
    private IAuthService authService;

    @Mock
    private IRecipeService recipeService;

    private MockMvc mockMvc;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(feedService).build();
    }

    @Test
    public void getFeedsByUserTest() throws SQLException {
        ArrayList<FeedDto> mockFeeds = new ArrayList<>();

        when(feedDao.getFeedsByUser(4, 0)).thenReturn(mockFeeds);

        ArrayList<FeedDto> resultFeeds = feedService.getFeedsByUser(4, 0);

        // Assert
        assertTrue(resultFeeds.isEmpty());
    }

    @Test
    public void recordPostTest() throws Exception {
        int userId = 0;
        String token = "dummyToken";
        FeedDto feedDto = new FeedDto();
        feedDto.setFeedId(0); // Simulate adding a new feed

        // Configure mockito to return invalid userId
        when(authService.extractUserIdFromToken(token)).thenReturn(userId);
        mockMvc.perform(put("/feeds/record-post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(feedDto))
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid userId for feed"))
                .andExpect(jsonPath("$.data").doesNotExist());


        //Success case
        // Mocking behavior of the feedDao.addPost method to return a valid feedId
        userId=1; //setting user for successful authentication
        int feedId = 123;
        when(authService.extractUserIdFromToken(token)).thenReturn(userId);
        when(feedService.recordPost(feedDto, userId)).thenReturn(feedDto);
        when(feedDao.addPost(feedDto)).thenReturn(feedId);

        mockMvc.perform(put("/feeds/record-post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(feedDto))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully recorded feed"))
                .andExpect(jsonPath("$.data").exists());

    }

    @Test
    public void postRecipeTest() throws Exception {
        int userId = 0;
        int recipeId = 123;
        String token = "dummyToken";
        CompleteRecipeDto recipe = new CompleteRecipeDto();
        recipe.setRecipe(new RecipeDto());
        recipe.setIngredients(new ArrayList<>(4));

        // Configure mockito to return valid userId and recipe
        when(authService.extractUserIdFromToken(token)).thenReturn(userId);
        //Invalid user id

        mockMvc.perform(put("/feeds/post-recipe")
                        .param("recipeId", String.valueOf(recipeId))
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid user"))
                .andExpect(jsonPath("$.data").doesNotExist());

        //
        userId=1;
        when(authService.extractUserIdFromToken(token)).thenReturn(userId);
        when(recipeService.fetchRecipeByRecipeId(recipeId)).thenReturn(recipe);

        FeedDto feedDto = FeedHelper.convertRecipeToFeed(recipe.getRecipe(), recipe.getIngredients());

        // Mock the behavior of the feedService.recordPost method to return a valid FeedDto
        when(feedService.recordPost(feedDto, userId)).thenReturn(feedDto);


        //Invalid Recipe
        mockMvc.perform(put("/feeds/post-recipe")
                        .param("recipeId", String.valueOf(recipeId))
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid recipe"))
                .andExpect(jsonPath("$.data").doesNotExist());



        //Success
        userId =1;
        when(authService.extractUserIdFromToken(token)).thenReturn(userId);
        mockMvc.perform(put("/feeds/post-recipe")
                        .param("recipeId", String.valueOf(recipeId))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully posted recipe"))
                .andExpect(jsonPath("$.data").exists());
    }

    static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
