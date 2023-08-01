package com.flavourfit.Feeds;


import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.User.UserDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;


public class FeedDaoImplTest {

    private FeedDaoImpl feedDao;

    @Mock
    private DatabaseManagerImpl database;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    public void initMocks() throws SQLException {
        MockitoAnnotations.openMocks(this);
        reset(database,connection,resultSet,preparedStatement);
        when(database.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        feedDao = new FeedDaoImpl(database);
    }
    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);


        MockitoAnnotations.initMocks(this);
        when(database.getConnection()).thenReturn(connection);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

    }

    @Test
    public void getFeedsByUserTest() throws SQLException{
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt(anyString())).thenReturn(1);
        when(resultSet.getString(anyString())).thenReturn("testContent");
        when(resultSet.getInt(anyString())).thenReturn(5);
        ArrayList<FeedDto> result = feedDao.getFeedsByUser(1, 0);
        assertNotNull(result);
        assertEquals(1, result.size());

        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));
        assertThrows(SQLException.class, () -> feedDao.getFeedsByUser(1, 0));
    }

    @Test
    public void addPostTest() throws SQLException {
        // Mock input data
        FeedDto mockFeed = new FeedDto();
        mockFeed.setFeedContent("Test post content");
        mockFeed.setUserId(123);

        // Mocking behavior for Connection and PreparedStatement
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Indicate that 1 row was affected by the insert operation

        // Mock behavior for ResultSet (Generated keys)
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Indicate that there's a next row in the ResultSet
        when(mockResultSet.getLong(1)).thenReturn(1L); // Simulate the generated feedId

        // Call the method
        int newFeedId = feedDao.addPost(mockFeed);

        // Assertions
        assertEquals(1, newFeedId); // Ensure that the returned feedId matches the expected generated feedId

        // Mock input data
        FeedDto invalidFeed = null; // Provide an invalid (null) FeedDto object

        // Call the method - this should throw a SQLException with the message "Feed object not valid!!"
        feedDao.addPost(invalidFeed);
    }


    @Test
    public void updatePostTest() throws SQLException {
        // Mock input data
        FeedDto mockFeed = new FeedDto();
        mockFeed.setFeedContent("Updated post content");
        mockFeed.setUserId(123);
        mockFeed.setFeedId(1);
        mockFeed.setLikeCount(50);
        // Mocking behavior for Connection and PreparedStatement
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Indicate that 1 row was affected by the update operation

        // Call the method
        feedDao.updatePost(mockFeed);

        // No need to add assertions here, as we are mainly testing that the method does not throw any exceptions.
        // Additional tests can be added to check if the actual data in the database has been updated correctly.
    }
    /*
    @Test(expected = SQLException.class)
    public void testUpdatePost_InvalidFeedObject() throws SQLException {
        // Mock input data
        FeedDto invalidFeed = null; // Provide an invalid (null) FeedDto object

        // Call the method - this should throw a SQLException with the message "Feed object not valid!!"
        feedDao.updatePost(invalidFeed);
    }

    @Test(expected = SQLException.class)
    public void testUpdatePost_InvalidFeedId() throws SQLException {
        // Mock input data
        FeedDto invalidFeed = new FeedDto("Invalid post content", 123, 0, 50); // Provide a valid feedContent, userId, likeCount, but invalid feedId (0)

        // Call the method - this should throw a SQLException with the message "Feed object not valid!!"
        feedDao.updatePost(invalidFeed);
    }

    @Test(expected = SQLException.class)
    public void testUpdatePost_InvalidUserId() throws SQLException {
        // Mock input data
        FeedDto invalidFeed = new FeedDto("Invalid post content", 0, 1, 50); // Provide a valid feedContent, feedId, likeCount, but invalid userId (0)

        // Call the method - this should throw a SQLException with the message "Feed object not valid!!"
        feedDao.updatePost(invalidFeed);
    }

    @Test(expected = SQLException.class)
    public void testUpdatePost_NullFeedContent() throws SQLException {
        // Mock input data
        FeedDto invalidFeed = new FeedDto(null, 123, 1, 50); // Provide a valid userId, feedId, and likeCount, but null feedContent

        // Call the method - this should throw a SQLException with the message "Feed object not valid!!"
        feedDao.updatePost(invalidFeed);
    }*/
}
