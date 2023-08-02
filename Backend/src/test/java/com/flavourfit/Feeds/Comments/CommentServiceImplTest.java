package com.flavourfit.Feeds.Comments;

import com.flavourfit.Exceptions.FeedsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class CommentServiceImplTest {
    @Mock
    private ICommentsDao mockCommentsDao;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getCommentsByFeedsTest() throws SQLException {
        // Mock input data
        int feedId = 123;

        // Mocking behavior for CommentsDao
        List<CommentDto> expectedComments = new ArrayList<>();
        CommentDto commentDto =  new CommentDto();
        commentDto.setCommentId(123);
        commentDto.setUserId(1);
        commentDto.setCommentContent("Comment 1");
        expectedComments.add(commentDto);

        CommentDto commentDto2 =  new CommentDto();
        commentDto2.setUserId(2);
        commentDto2.setCommentId(456);
        commentDto2.setCommentContent("Comment 2");
        expectedComments.add(commentDto2);
        when(mockCommentsDao.getCommentsByFeedId(feedId)).thenReturn(expectedComments);

        List<CommentDto> actualComments = commentService.getCommentsByFeeds(feedId);
        assertEquals(expectedComments, actualComments);
    }

    @Test
    public void removeCommentFromFeedTest() throws SQLException, FeedsException {
        //Invalid User ID
        CommentDto commentDto = new CommentDto();
        commentDto.setUserId(0);
        commentDto.setCommentId(0);
        commentDto.setCommentContent("Invalid comment content");
        assertThrows(FeedsException.class, () ->{commentService.recordComment(commentDto, 0);});

        //Success Case
        int feedId = 123;
        int commentId = 456;

        when(mockCommentsDao.removeCommentInFeed(feedId, commentId)).thenReturn(true); // Assuming comment removal is successful
        boolean isCommentRemoved = commentService.removeCommentFromFeed(feedId, commentId);

        assertTrue(isCommentRemoved);

        //Update existing comment
        int userId=123;
        doNothing().when(mockCommentsDao).updateComment(commentDto); // Assuming the update is successful

        commentService.recordComment(commentDto, userId);
    }


    @Test
    public void recordCommentTest() throws FeedsException, SQLException {
        CommentDto commentDto = new CommentDto();
        commentDto.setUserId(123);
        commentDto.setCommentId(0);
        commentDto.setCommentContent("New comment content");

        doNothing().when(mockCommentsDao).addComment(commentDto); // Assuming the addition is successful


        commentService.recordComment(commentDto, 123);
    }

}