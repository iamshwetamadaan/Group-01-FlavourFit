package com.flavourfit.Homepage;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.Homepage.DTO.FitnessStreakDTO;
import com.flavourfit.Homepage.DTO.RoutineDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
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
    CallableStatement callableStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        reset(database,connection,preparedStatement,resultSet);
        when(database.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareCall(anyString())).thenReturn(callableStatement);
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

    @Test
    public void testGetEventList() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        // Create mock data for the ResultSet
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("Event_id")).thenReturn(1).thenReturn(2);
        when(resultSet.getString("Event_name")).thenReturn("Halifax : Free Fitness for Mind and Soul in 3 weeks course").thenReturn("Wellness 1 Day Training in Halifax");
        when(resultSet.getString("Start_date")).thenReturn("2023-09-01").thenReturn("2023-09-07");
        when(resultSet.getString("End_date")).thenReturn("2023-09-01").thenReturn("2023-09-07");
        when(resultSet.getString("Capacity")).thenReturn("100").thenReturn("100");
        when(resultSet.getString("Host_Name")).thenReturn("Sasha Berkley").thenReturn("John Mendow");
        when(resultSet.getString("Event_description")).thenReturn("Yoga and Pillates event").thenReturn("HIIT Workout session");

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        List<HomepageEventDto> eventList = homepageDao.getEventList();

        // Assertions
        // assertEquals(2, eventList.size()); // Check if two events are returned as expected
        assertEquals("Halifax : Free Fitness for Mind and Soul in 3 weeks course", eventList.get(0).getEvent_name());
        assertEquals("Wellness 1 Day Training in Halifax", eventList.get(1).getEvent_name());
        // Add more assertions for other properties if needed
    }

    @Test
    void getFitnessStreakTest() throws Exception {
        // Happy path
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getDouble(1)).thenReturn(100.0).thenReturn(50.0);
        when(resultSet.getInt(2)).thenReturn(5).thenReturn(3);

        int userId = 1;
        FitnessStreakDTO fitnessStreak = homepageDao.getFitnessStreak(userId);

        assertEquals(20.0, fitnessStreak.getAvgCalorie());
        assertEquals(0.0 , fitnessStreak.getAvgWaterIntake());
        assertEquals(5, fitnessStreak.getStreak());
    }


}
