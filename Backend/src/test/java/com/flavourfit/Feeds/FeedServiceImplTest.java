package com.flavourfit.Feeds;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.FeedsException;
import com.flavourfit.Feeds.Comments.CommentDto;
import com.flavourfit.Feeds.Comments.CommentServiceImpl;
import com.flavourfit.Feeds.Comments.ICommentsService;
import com.flavourfit.Helpers.FeedHelper;
import com.flavourfit.Recipes.CompleteRecipeDto;
import com.flavourfit.Recipes.IRecipeService;
import com.flavourfit.Recipes.RecipeDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @InjectMocks
    private CommentServiceImpl commentService;


    @Test
    public void getFeedsByUserTest() throws SQLException {
        ArrayList<FeedDto> mockFeeds = new ArrayList<>();

        when(feedDao.getFeedsByUser(4, 0)).thenReturn(mockFeeds);

        ArrayList<FeedDto> resultFeeds = feedService.getFeedsByUser(4, 0);

        // Assert
        assertTrue(resultFeeds.isEmpty());
    }

    @Test
    public void recordPostTest() throws FeedsException {
        FeedDto feedDto = new FeedDto();
        feedDto.setFeedId(0);
        feedDto.setFeedContent("Test post content");
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentContent("Wow!");
        int userId = 123;
        commentService.recordComment(commentDto, userId);
        FeedDto result = feedService.recordPost(feedDto, userId);
        // Asserting a new post feed
        assertNotNull(result);
        assertEquals(userId, result.getUserId());

        //Asserting existing post feed
        feedDto.setFeedId(456);
        feedDto.setFeedContent("Updated post content");
        userId = 789;
        result = feedService.recordPost(feedDto, userId);
        assertNotNull(result);
        assertEquals(userId, result.getUserId());

        //Asserting invalid user id
        feedDto.setFeedId(0);
        feedDto.setFeedContent("Test post content");
        int invalidUserId = 0;

        assertThrows(FeedsException.class, () -> feedService.recordPost(feedDto, invalidUserId));

        //Success case
        //Asserting valid recipeID and userID
        int recipeId = 789;
        userId = 123;

        result = feedService.postRecipe(recipeId, userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());

    }

    @Test
    public void postRecipeTest() throws FeedsException {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setRecipeId(676);
        recipeDto.setRecipeName("Make burger");
        FeedDto result = feedService.postRecipe(676, 667);

        // Assertion
        assertNotNull(result);
        assertEquals(667, result.getUserId());

        assertThrows(FeedsException.class, () -> feedService.postRecipe(0, 124));


        assertThrows(RuntimeException.class, () -> feedService.postRecipe(789, 0));

    }

}
