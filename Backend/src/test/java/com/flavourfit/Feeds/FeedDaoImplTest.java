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
        FeedDto feedDtoInvalid = new FeedDto();
        //Testing if feed updated has null content
        feedDtoInvalid.setFeedContent(null);
        feedDtoInvalid.setLikeCount(10);
        feedDtoInvalid.setUserId(1);
        feedDtoInvalid.setFeedId(2);
        assertThrows(SQLException.class, () -> {
            feedDao.updatePost(feedDtoInvalid);
        });

        //Testing if feed updated has userId=0
        feedDtoInvalid.setFeedContent("Test Content");
        feedDtoInvalid.setLikeCount(10);
        feedDtoInvalid.setUserId(0);
        feedDtoInvalid.setFeedId(2);
        assertThrows(SQLException.class, () -> {
            feedDao.updatePost(feedDtoInvalid);
        });

        //Testing when feed is null
        FeedDto invalidFeedDto = null;
        assertThrows(SQLException.class, () -> {
            feedDao.addPost(invalidFeedDto);
        });

        //Success Case
        FeedDto feedDto = new FeedDto();
        feedDto.setFeedContent("Test Content");
        feedDto.setUserId(1);

        when(database.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong(1)).thenReturn(1L);

        int result = feedDao.addPost(feedDto);

        assertEquals(1, result);
        verify(preparedStatement, times(1)).executeUpdate();

    }


    @Test
    public void updatePostTest() throws SQLException {
        FeedDto feedDtoInvalid = new FeedDto();

        //Testing if feed updated has null content
        feedDtoInvalid.setFeedContent(null);
        feedDtoInvalid.setLikeCount(10);
        feedDtoInvalid.setUserId(1);
        feedDtoInvalid.setFeedId(2);
        assertThrows(SQLException.class, () -> {
            feedDao.updatePost(feedDtoInvalid);
        });

        //Testing if feed updated has feedId=0
        feedDtoInvalid.setFeedContent("Test Content");
        feedDtoInvalid.setLikeCount(10);
        feedDtoInvalid.setUserId(1);
        feedDtoInvalid.setFeedId(0);
        assertThrows(SQLException.class, () -> {
            feedDao.updatePost(feedDtoInvalid);
        });

        //Testing if feed updated has userId=0
        feedDtoInvalid.setFeedContent("Test Content");
        feedDtoInvalid.setLikeCount(10);
        feedDtoInvalid.setUserId(0);
        feedDtoInvalid.setFeedId(2);
        assertThrows(SQLException.class, () -> {
            feedDao.updatePost(feedDtoInvalid);
        });

        //Testing if updated feed is null
        FeedDto invalidFeedDto = null;
        assertThrows(SQLException.class, () -> {
            feedDao.updatePost(invalidFeedDto);
        });

        //Success case
        FeedDto feedDto = new FeedDto();
        feedDto.setFeedContent("Test Content");
        feedDto.setLikeCount(10);
        feedDto.setUserId(1);
        feedDto.setFeedId(2);

        when(database.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        feedDao.updatePost(feedDto);

        verify(preparedStatement, times(1)).executeUpdate();

    }
}
