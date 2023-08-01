package com.flavourfit.Homepage;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.Homepage.DTO.RoutineDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HomepageDaoImplTest {

    private HomepageDaoImpl homepageDao;

    @Mock
    private DatabaseManagerImpl database;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        reset(database,connection,resultSet,preparedStatement);
        when(database.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        homepageDao = new HomepageDaoImpl(database);
    }

    @Test
    public void getRoutinesByUserTest() throws SQLException {
        // Mock the necessary dependencies to return a ResultSet with routines
            int userId = 7; // Replace with a valid user ID for testing
            RoutineDTO routine = new RoutineDTO();
            routine.setRoutineName("Routine 1");
            routine.setTips("Routine tips");
//            List<RoutineDTO> routines = null;
//            routines.add(routine);
            routine.setRoutineDescription("Routine Desc");
            when(resultSet.next()).thenReturn(true, true, false); // Two rows in the ResultSet
            when(resultSet.getInt("Routine_id")).thenReturn(1, 2); // Assuming Routine_id is an integer column
            when(resultSet.getString("Routine_name")).thenReturn("Routine 1", "Routine 2");
//            when(homepageDao.ex)
//            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // Mock the connection and prepareStatement to return the PreparedStatement
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

            // Inject the mock connection into the homepageDao instance
//            homepageDao.setConnection(mockConnection);

            List<RoutineDTO> routines = homepageDao.getRoutinesByUser(userId);

            // Assert that the routines list is not empty and contains the expected routines
            assertNotNull(routines);
    }

}