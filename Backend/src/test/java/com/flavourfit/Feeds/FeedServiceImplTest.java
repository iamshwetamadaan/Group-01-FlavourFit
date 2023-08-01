package com.flavourfit.Feeds;

import com.flavourfit.Feeds.Comments.CommentDto;
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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
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
    @Test
    public void getFeedsByIdTest() throws Exception {
        // Arrange
        int feedId = 1;
        FeedDto feed = new FeedDto();
        feed.setFeedId(feedId);
        feed.setFeedContent("Test feed content");

        List<CommentDto> comments = new ArrayList<>();
        CommentDto comment1 = new CommentDto();
        comment1.setCommentId(101);
        comment1.setCommentContent("Comment 1");

        CommentDto comment2 = new CommentDto();
        comment2.setCommentId(102);
        comment2.setCommentContent("Comment 2");

        comments.add(comment1);
        comments.add(comment2);
        feed.setComments(comments);

        when(feedDao.getFeedsById(feedId)).thenReturn(feed);
        when(commentService.getCommentsByFeeds(feedId)).thenReturn(comments);

        // Act
        FeedDto result = feedService.getFeedsByID(feedId);

        // Assert
        assertNotNull(result);
        assertEquals(feedId, result.getFeedId());
        assertEquals("Test feed content", result.getFeedContent());
        assertEquals(comments, result.getComments());

        verify(feedDao).getFeedsById(feedId);
        verify(commentService).getCommentsByFeeds(feedId);
    }
}
