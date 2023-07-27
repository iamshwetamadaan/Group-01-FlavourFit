package com.flavourfit.Feeds;

import com.flavourfit.Feeds.Comments.CommentServiceImpl;
import com.flavourfit.Trackers.Calories.CalorieHistoryServiceImpl;
import com.flavourfit.Trackers.Calories.ICalorieHistoryDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
}
