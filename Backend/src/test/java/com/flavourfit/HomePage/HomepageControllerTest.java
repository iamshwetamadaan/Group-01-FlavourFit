package com.flavourfit.HomePage;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Homepage.HomepageController;
import com.flavourfit.Homepage.IHomepageService;
import com.flavourfit.ResponsesDTO.PutResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.HashMap;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomepageControllerTest {

    @InjectMocks
    private HomepageController homepageController;

    @Mock
    private IAuthService authService;

    @Mock
    private IHomepageService homePageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        //    trackersController = new TrackersController(calorieHistoryService, null, authService);
    }

    @Test
    public void getExerciseByUserTest() throws SQLException {
        String validToken = "valid_token_here";
        int userId = 123; // The expected user ID associated with the token
        HashMap<String, Object> mockExerciseData = new HashMap<>();
        mockExerciseData.put("key", "value"); // Mocked exercise data
        when(authService.extractUserIdFromToken(validToken)).thenReturn(userId);
        when(homePageService.getExerciseByUser(userId)).thenReturn(mockExerciseData);

        ResponseEntity<PutResponse> response = homepageController.getExerciseByUser(validToken);

        // Assert that the response is successful and contains the expected exercise data
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Successfully obtained the routines", response.getBody().getMessage());
        assertEquals(mockExerciseData, response.getBody().getData());
    }
}
