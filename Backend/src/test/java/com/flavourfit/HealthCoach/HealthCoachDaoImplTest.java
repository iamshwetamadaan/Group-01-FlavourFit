package com.flavourfit.HealthCoach;

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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class HealthCoachDaoImplTest {
    @InjectMocks
    private HealthCoachDaoImpl healthCoachDao;

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
    public void getAllHealthCoachesTest() throws SQLException{
        ArrayList<HealthCoachDto> mockCoaches = new ArrayList<>();

        when(resultSet.next()).thenReturn(false);

        ArrayList<HealthCoachDto> resultCoaches = healthCoachDao.getAllHealthCoaches();

        
        assertFalse(resultCoaches.isEmpty());
    }
}
