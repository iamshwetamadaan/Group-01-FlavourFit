package com.flavourfit.HealthCoach;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Feeds.FeedDaoImpl;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class HealthCoachDaoImplTest {
    private HealthCoachDaoImpl healthCoachDao;

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
        healthCoachDao = new HealthCoachDaoImpl(database);
    }

    @Test
    public void getAllHealthCoachesTest() throws SQLException{
        ArrayList<HealthCoachDto> mockCoaches = new ArrayList<>();

        when(resultSet.next()).thenReturn(false);

        ArrayList<HealthCoachDto> resultCoaches = healthCoachDao.getAllHealthCoaches();

        
        assertTrue(resultCoaches.isEmpty());
    }
}
