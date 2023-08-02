package com.flavourfit.Feeds.Comments;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flavourfit.Exceptions.FeedsException;
import com.flavourfit.Feeds.Comments.CommentDaoImpl;
import com.flavourfit.Feeds.Comments.CommentDto;
import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.mockito.MockitoAnnotations;

public class CommentDaoImplTest {

    private CommentDaoImpl commentDao;
    private DatabaseManagerImpl database;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        database = mock(DatabaseManagerImpl.class);
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(database.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        commentDao = new CommentDaoImpl(database);
    }

    @Test
    public void getCommentsByFeedIdTest() throws SQLException {
        int feedId = 123;
        CommentDto commentDto = new CommentDto();
        commentDto.setUserId(456);
        commentDto.setCommentId(1);
        commentDto.setFeedId(feedId);
        commentDto.setCommentContent("Comment 1");
        commentDto.setUsername("user1");

        CommentDto commentDto2 = new CommentDto();
        commentDto2.setUserId(789);
        commentDto2.setCommentId(0);
        commentDto2.setCommentContent("Comment 2");
        commentDto2.setUsername("user2");
        commentDto.setFeedId(feedId);

        List<CommentDto> expectedComments = new ArrayList<>();
        expectedComments.add(commentDto);
        expectedComments.add(commentDto2);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("Comment_id")).thenReturn(1, 2);
        when(resultSet.getString("Comment_content")).thenReturn("Comment 1", "Comment 2");
        when(resultSet.getInt("User_id")).thenReturn(456, 789);
        when(resultSet.getInt("Feed_id")).thenReturn(feedId, feedId);
        when(resultSet.getString("Comment_username")).thenReturn("user1", "user2");

        String query = "SELECT * from Comments WHERE Feed_id=? ORDER BY Comment_id DESC";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        List<CommentDto> comments = commentDao.getCommentsByFeedId(feedId);

        assertNotNull(comments);
        assertEquals(1, comments.size());

    }

    @Test
    public void removeCommentInFeedTest() throws SQLException {
        int feedId = 123;
        int commentId = 456;
        when(preparedStatement.executeUpdate()).thenReturn(1);
        boolean commentDeleted = commentDao.removeCommentInFeed(feedId, commentId);

        assertTrue(commentDeleted);
    }

    @Test
    public void addCommentTest() throws SQLException {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentContent("Test comment");
        commentDto.setFeedId(123);
        commentDto.setUserId(456);
        commentDto.setUsername("testUser");

        String insertQuery = "INSERT INTO Comments (Comment_content,Feed_id,User_id,Comment_username) VALUES (?,?,?,?)";
        when(connection.prepareStatement(eq(insertQuery))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        commentDao.addComment(commentDto);

        verify(preparedStatement).setString(1, commentDto.getCommentContent());
        verify(preparedStatement).setInt(2, commentDto.getFeedId());
        verify(preparedStatement).setInt(3, commentDto.getUserId());
        verify(preparedStatement).setString(4, commentDto.getUsername());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void updateCommentTest() throws SQLException {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(789);
        commentDto.setCommentContent("Updated comment");
        when(preparedStatement.executeUpdate()).thenReturn(1);
        commentDao.updateComment(commentDto);
    }

    @Test
    public void addCommentWithInvalidCommentContentTest() {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentContent(null); // Invalid comment content
        commentDto.setFeedId(123);
        commentDto.setUserId(456);
        commentDto.setUsername("testUser");

        assertThrows(FeedsException.class, () -> commentDao.addComment(commentDto));
    }

    @Test
    public void addCommentWithInvalidFeedOrUserIdTest() {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentContent("Test comment");
        commentDto.setFeedId(0); // Invalid feed ID
        commentDto.setUserId(0); // Invalid user ID
        commentDto.setUsername("testUser");

        assertThrows(FeedsException.class, () -> commentDao.addComment(commentDto));
    }

    @Test
    public void updateCommentWithInvalidCommentIdTest() {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(0); // Invalid comment ID
        commentDto.setCommentContent("Updated comment");
        assertThrows(FeedsException.class, () -> commentDao.updateComment(commentDto));
    }
}
