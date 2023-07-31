package com.flavourfit.HealthCoach;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.HealthCoach.Appointments.AppointmentsDto;
import com.flavourfit.HealthCoach.Appointments.IAppointmentsService;
import com.flavourfit.ResponsesDTO.GetResponse;
import com.flavourfit.ResponsesDTO.PutResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class HealthCoachControllerTest {

    @InjectMocks
    private HealthCoachController healthCoachController;

    @Mock
    private IHealthCoachService healthCoachService;

    @Mock
    private IAppointmentsService appointmentsService;

    @Mock
    private IAuthService authService;

    @BeforeEach
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllHealthCoachesTest() throws SQLException {
        ArrayList<HealthCoachDto> mockHealthCoaches = new ArrayList<>();
        HealthCoachDto healthCoach = new HealthCoachDto();
        healthCoach.setYears_of_experience("1");
        healthCoach.setCoach_id(1);
        healthCoach.setSpeciality("Yoga");
        healthCoach.setDescription("Desc");
        healthCoach.setFirst_name("ABC");
        healthCoach.setLast_name("DEF");
        mockHealthCoaches.add(healthCoach);

        when(healthCoachService.getAllHealthCoaches()).thenReturn(mockHealthCoaches);

        // Call the controller method
        ResponseEntity<GetResponse> responseEntity = healthCoachController.getAllHealthCoaches();

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals("Successfully retrieved health Coaches information", responseEntity.getBody().getMessage());
        assertEquals(mockHealthCoaches, responseEntity.getBody().getData());
    }

    @Test
    public void bookAppointmentsTest() throws SQLException{
        AppointmentsDto appointmentDto = new AppointmentsDto();

        String token = "valid_token";
        int userId = 1;
        when(authService.extractUserIdFromToken(token)).thenReturn(userId);
        when(appointmentsService.bookAppointments(appointmentDto, userId)).thenThrow(new SQLException("Test Exception"));

        ResponseEntity<PutResponse> response = healthCoachController.bookAppointments(appointmentDto, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof PutResponse);
        PutResponse putResponse = response.getBody();
        assertFalse(putResponse.isSuccess());
        assertEquals("Failed to book the appointment :Test Exception", putResponse.getMessage());
    }
}
