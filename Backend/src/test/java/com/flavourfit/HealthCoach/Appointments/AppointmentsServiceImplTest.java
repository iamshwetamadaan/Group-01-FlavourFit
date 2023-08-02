package com.flavourfit.HealthCoach.Appointments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class AppointmentsServiceImplTest {
    @Mock
    private IAppointmentsDao appointmentsDao;

    @InjectMocks
    private AppointmentsServiceImpl appointmentsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void bookAppointmentsTest() throws SQLException {
        AppointmentsDto appointmentDto = new AppointmentsDto();
        // Set the appointmentDto properties for booking an appointment
        appointmentDto.setCoach_id(1); // Set a valid coach_id
        appointmentDto.setStartDate("2023-08-01");
        appointmentDto.setEndDate("2023-08-02");

        int userID = 1; // Set a valid userID

        // Assuming the appointmentDao.bookAppointments method returns a positive count value
        int count = 1;
        when(appointmentsDao.bookAppointments(appointmentDto, userID)).thenReturn(count);

        // Act
        int result = appointmentsService.bookAppointments(appointmentDto, userID);

        // Assert
        assertEquals(count, result);
    }
}
