package com.flavourfit.Homepage;

import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.HealthCoach.HealthCoachDaoImpl;
import com.flavourfit.Trackers.Calories.CalorieHistoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HomePageDaoImplTest {

    @InjectMocks
    private HomepageDaoImpl homepageDao;


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
}



