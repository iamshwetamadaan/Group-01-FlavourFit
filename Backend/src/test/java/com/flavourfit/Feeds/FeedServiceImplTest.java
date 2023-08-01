package com.flavourfit.Feeds;

import com.flavourfit.Feeds.Comments.CommentDto;
import com.flavourfit.Feeds.Comments.CommentServiceImpl;
import com.flavourfit.Feeds.Comments.ICommentsDao;
import com.flavourfit.Feeds.Comments.ICommentsService;
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
    @Mock
    private ICommentsService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
    public void getFeedsByIdTest() throws Exception {
        // Arrange
        int feedId = 9;
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
        //when(commentsDao.getCommentsByFeedId(feedId)).thenReturn(comments);
        when(commentService.getCommentsByFeeds(feedId)).thenReturn(feed.getComments());

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

    @Test
    public void updateLikesForFeedTest() throws Exception {
        // Arrange
        int feedId = 1;
        int initialLikes = 10;
        int expectedUpdatedLikes = initialLikes + 1;

        when(feedDao.updateFeedLikes(feedId)).thenReturn(expectedUpdatedLikes);

        // Act
        int result = feedService.increaseFeedLikes(feedId);

        // Assert
        assertEquals(expectedUpdatedLikes, result);

        verify(feedDao).updateFeedLikes(feedId);
    }

    @Test
    public void commentDeletionForFeedTest() throws Exception {
        // Arrange
        int feedId = 2;
        int commentId = 20;

        FeedDto feedDto = new FeedDto();
        feedDto.setFeedId(feedId);
        feedDto.setFeedContent("Test feed content");

        List<CommentDto> comments = new ArrayList<>();
        CommentDto comment1 = new CommentDto();
        comment1.setCommentId(20);
        comment1.setCommentContent("Comment 1");
        CommentDto comment2 = new CommentDto();
        comment2.setCommentId(21);
        comment2.setCommentContent("Comment 2");
        comments.add(comment1);
        comments.add(comment2);

        when(feedDao.getFeedsById(feedId)).thenReturn(feedDto);
        when(commentService.removeCommentFromFeed(feedId, commentId)).thenReturn(true);
        when(commentService.getCommentsByFeeds(feedId)).thenReturn(comments);

        // Act
        FeedDto result = feedService.removeCommentFromFeed(feedId, commentId);

        // Assert
        assertNull(result);
        //assertEquals(feedId, result.getFeedId());
        //assertEquals("Test feed content", result.getFeedContent());
        //assertEquals(comments, result.getComments());

        verify(feedDao).getFeedsById(feedId);
        verify(commentService).removeCommentFromFeed(feedId, commentId);
        verify(commentService).getCommentsByFeeds(feedId);
    }
}
