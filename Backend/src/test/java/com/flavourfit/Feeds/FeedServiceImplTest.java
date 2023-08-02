package com.flavourfit.Feeds;

import com.flavourfit.Exceptions.FeedsException;
import com.flavourfit.Feeds.Comments.CommentDto;
import com.flavourfit.Feeds.Comments.CommentServiceImpl;
import com.flavourfit.Feeds.Comments.ICommentsDao;
import com.flavourfit.Feeds.Comments.ICommentsService;
import com.flavourfit.Recipes.IRecipeService;
import com.flavourfit.Recipes.Ingredients.IIngredientsService;
import com.flavourfit.Trackers.Calories.CalorieHistoryServiceImpl;
import com.flavourfit.Trackers.Calories.ICalorieHistoryDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedServiceImplTest {
    @InjectMocks
    private FeedServiceImpl feedService;

    @Mock
    private IFeedDao feedDao;

    @Mock
    private ICommentsService commentsService;

    @Mock
    private IRecipeService recipeService;

    @Mock
    private IIngredientsService ingredientsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIncreaseFeedLikes() throws SQLException {
        int feedId = 1;

        // Happy path
        int updatedLikes = 10;

        int result = feedService.increaseFeedLikes(feedId);

        assertEquals(0, result);
    }

    @Test
    public void testGetFeedsByUser() throws SQLException {
        int userId = 1;
        int offset = 0;

        // Happy path
        ArrayList<FeedDto> feeds = new ArrayList<>(); // Fill with sample data if needed
        when(feedDao.getFeedsByUser(userId, offset)).thenReturn(feeds);

        ArrayList<FeedDto> result = feedService.getFeedsByUser(userId, offset);

        assertEquals(feeds, result);

        // Resetting mock behavior
        reset(feedDao);
    }



}
