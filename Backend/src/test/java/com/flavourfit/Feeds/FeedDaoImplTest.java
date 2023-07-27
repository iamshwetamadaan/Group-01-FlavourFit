package com.flavourfit.Feeds;

import com.flavourfit.DatabaseManager.IDatabaseManager;
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
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class FeedDaoImplTest {

    @InjectMocks
    private FeedDaoImpl feedDao;

    @Mock
    private IDatabaseManager database;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    public void initMocks() throws SQLException {
        MockitoAnnotations.initMocks(this);
        when(database.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    public void getFeedsByUserTest() throws SQLException{
        // Arrange
        int userId = 7;
        int offset = 0;

        // Mock ResultSet to return a valid feed
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("feed_id")).thenReturn(1);
        when(resultSet.getString("feed_content")).thenReturn("Test feed content");

        // Act
        List<FeedDto> userFeeds = feedDao.getFeedsByUser(userId, offset);

        // Assert
        assertEquals(7, userFeeds.get(0).getUserId());
    }
}
