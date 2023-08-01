package com.flavourfit.HealthCoach.Appointments;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Feeds.FeedDaoImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AppointmentsDaoImplTest {

    private AppointmentsDaoImpl appointmentsDao;

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
        appointmentsDao = new AppointmentsDaoImpl(database);
    }

    @Test
    public void bookAppointmentsTest() throws SQLException {
        // Arrange
        AppointmentsDto appointmentDto = new AppointmentsDto();
        appointmentDto.setStartDate("2023-08-01");
        appointmentDto.setEndDate("2023-08-02");
        appointmentDto.setCoach_id(1);

        int userId = 1;

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1); // Assuming one row is inserted

        // Act
        int result = appointmentsDao.bookAppointments(appointmentDto, userId);

        // Assert
        assertEquals(1, result);
    }



}
