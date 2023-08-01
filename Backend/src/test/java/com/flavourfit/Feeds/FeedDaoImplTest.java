package com.flavourfit.Feeds;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.User.UserDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    public void testGetFeedsById() throws SQLException {
        // Arrange
        int feedId = 1;
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        FeedDto expectedFeedDto = new FeedDto();
        expectedFeedDto.setFeedId(feedId);
        expectedFeedDto.setFeedContent("Test feed content");

        when(database.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(feedDao.extractUserFeedsFromResult(resultSet)).thenReturn(expectedFeedDto);

        // Act
        FeedDto result = feedDao.getFeedsById(feedId);

        // Assert
        assertNotNull(result);
        assertEquals(feedId, result.getFeedId());
        assertEquals("Test feed content", result.getFeedContent());

        verify(database).getConnection();
        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setInt(1, feedId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        verify(feedDao).extractUserFeedsFromResult(resultSet);
    }

    @Test
    public void testForLikesUpdate() throws Exception{
        // Arrange
        int feedId = 1;
        int initialLikes = 10;
        int expectedUpdatedLikes = initialLikes + 1;

        FeedDto feed = new FeedDto();
        feed.setFeedId(feedId);
        feed.setLikeCount(initialLikes);

        // Act
        int result = feedDao.updateFeedLikes(feedId);

        // Assert
        assertEquals(feedId, result);
    }

}
