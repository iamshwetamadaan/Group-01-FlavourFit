package com.flavourfit.Feeds;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.FeedsException;

import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.Feeds.Comments.CommentDto;
import com.flavourfit.Feeds.Comments.CommentServiceImpl;
import com.flavourfit.Feeds.Comments.ICommentsDao;
import com.flavourfit.Feeds.Comments.ICommentsService;
import com.flavourfit.Helpers.FeedHelper;
import com.flavourfit.Recipes.CompleteRecipeDto;
import com.flavourfit.Recipes.IRecipeService;
import com.flavourfit.Recipes.Ingredients.IIngredientsService;
import com.flavourfit.Recipes.Ingredients.IngredientDto;
import com.flavourfit.Recipes.RecipeDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;


import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class FeedServiceImplTest {
    @Mock
    private IFeedDao feedDao;

    @InjectMocks
    private FeedServiceImpl feedService;

    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    IRecipeService recipeService;
    @Mock
    IIngredientsService ingredientsService;

    @Mock
    ICommentsDao commentsDao;


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
        //FeedDto result = feedService.recordPost(feedDto, userId);
        // Asserting a new post feed
        //assertThrows(NullPointerException.class, () -> feedService.recordPost(feedDto, 123));
        //assertNotNull(result);
        //assertEquals(userId, result.getUserId());

        //Asserting existing post feed
        feedDto.setFeedId(456);
        feedDto.setFeedContent("Updated post content");
        userId = 789;


        //Asserting invalid user id
        feedDto.setFeedId(0);
        feedDto.setFeedContent("Test post content");
        int invalidUserId = 0;

        //assertThrows(FeedsException.class, () -> feedService.recordPost(feedDto, invalidUserId));

        //Success case
        //Asserting valid recipeID and userID
//        int recipeId = 789;
//        userId = 123;

//        FeedDto result = feedService.postRecipe(recipeId, userId);
//
//        assertNotNull(result);
//        assertEquals(userId, result.getUserId());

    }

    @Test
    public void postRecipeTest() throws FeedsException, SQLException {
        //Invalid recipeID
        assertThrows(FeedsException.class, () -> feedService.postRecipe(0, 1));

        //Invalid userId
        assertThrows(UserNotFoundException.class, () -> feedService.postRecipe(1, 0));

        //Success Case
        int recipeId = 1;
        int userId = 1;

        FeedDto feedDto = new FeedDto();
        feedDto.setFeedId(1);
        feedDto.setUserId(1);

        when(feedDao.addPost(feedDto)).thenReturn(1);

        // Mock the behavior of commentsService
        when(commentService.getCommentsByFeeds(anyInt())).thenReturn(new ArrayList<>());

        // Mock the behavior of feedDao.getFeedsById
        when(feedDao.getFeedsById(1)).thenReturn(feedDto);

        //FeedDto result = feedService.recordPost(feedDto, 1);

//        assertNotNull(result);
//        assertEquals(1, result.getFeedId());

        /*CompleteRecipeDto completeRecipeDto = new CompleteRecipeDto();
        RecipeDto recipeDto = new RecipeDto();
        List<IngredientDto> ingredients = new ArrayList<>();
        completeRecipeDto.setRecipe(recipeDto);
        completeRecipeDto.setIngredients(ingredients);

        when(recipeService.fetchRecipeByRecipeId(recipeId)).thenReturn(completeRecipeDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setFeedId(1);
        commentDto.setUserId(userId);
        commentDto.setUsername("shweta");
        commentDto.setCommentContent("Wow!");

        CommentServiceImpl commentService = new CommentServiceImpl(commentsDao);
        commentsDao.addComment(commentDto);
        commentService.recordComment(commentDto, userId);
        when(feedService.recordPost(feedDto, userId)).thenReturn(feedDto);

        assertEquals(feedDto, result);*/


    }
}