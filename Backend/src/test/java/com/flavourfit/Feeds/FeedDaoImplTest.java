package com.flavourfit.Feeds;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.User.UserDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
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

    //error
    @Test
    public void getFeedsByIdTest() throws SQLException {
        // Arrange
        int feedId = 1;
        FeedDto expectedFeedDto = new FeedDto();
        expectedFeedDto.setFeedId(feedId);
        expectedFeedDto.setFeedContent("Test feed content");

        when(resultSet.next()).thenReturn(true,false);
        when(resultSet.getInt("Feed_id")).thenReturn(feedId);
        when(resultSet.getString("Feed_content")).thenReturn("Test feed content");
        when(resultSet.getInt("Like_count")).thenReturn(4);
        //when(feedDao.extractUserFeedsFromResult(resultSet)).thenReturn(expectedFeedDto);

        // Act
        FeedDto result = feedDao.getFeedsById(feedId);

        // Assert
        assertNotNull(result);
        assertEquals(feedId, result.getFeedId());
        assertEquals("Test feed content", result.getFeedContent());
    }

    //error
    @Test
    public void updateFeedLikesTest() throws Exception{
        int feedId = 1;
        int likesUpdated = 10;
        when(database.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(connection.prepareStatement("UPDATE Feeds SET like_count = ? where Feed_id = ?", Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement); // Specific query
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("Like_count")).thenReturn(likesUpdated - 1); // Specific column name
        feedDao = new FeedDaoImpl(database);
        // When
        int result = feedDao.updateFeedLikes(feedId);

        // Then
        assertEquals(feedId, result);

    }

}
