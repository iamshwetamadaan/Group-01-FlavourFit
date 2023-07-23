package com.flavourfit.Trackers;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.CalorieHistoryException;
import com.flavourfit.Helpers.DateHelpers;
import com.flavourfit.ResponsesDTO.GetResponse;
import com.flavourfit.Trackers.Calories.CalorieGraphDto;
import com.flavourfit.Trackers.Calories.CalorieHistoryDto;
import com.flavourfit.Trackers.Calories.ICalorieHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class TrackersControllerTest {
    private TrackersController trackersController;

    @Mock
    private ICalorieHistoryService calorieHistoryService;

    @Mock
    private IAuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        trackersController = new TrackersController(calorieHistoryService, null, authService);
    }

    @Test
    public void recordCaloriesTest() throws CalorieHistoryException {
        double calorieCount = 2000;
        int userId = 1;
        String date = DateHelpers.getCurrentDateString();

        CalorieHistoryDto calorieHistoryDto = new CalorieHistoryDto(calorieCount, date, userId);

        when(authService.extractUserIdFromToken("Bearer token")).thenReturn(userId);
        when(calorieHistoryService.fetchCalorieByUserIdDate(date, userId)).thenReturn(calorieHistoryDto);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("calorieCount", calorieCount);
        ResponseEntity<Object> responseEntity = trackersController.recordCalories(requestBody, "Bearer token");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // CalorieHistoryException is thrown
        when(calorieHistoryService.fetchCalorieByUserIdDate(date, userId)).thenThrow(
                new CalorieHistoryException("Failed to record calorieCount"));
        ResponseEntity<Object> responseEntity2 = trackersController.recordCalories(requestBody, "Bearer token");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
    }

    @Test
    public void fetchCalorieHistoryTest() throws CalorieHistoryException {
        String startDate = "2023-07-15";
        String endDate = "2023-07-20";
        int userId = 1;

        CalorieGraphDto calorieGraphDto = new CalorieGraphDto("2023-07-20", 2000d);
        List<CalorieGraphDto> calorieGraphDtoList = new ArrayList<>();
        calorieGraphDtoList.add(calorieGraphDto);

        // Success case
        when(authService.extractUserIdFromToken("Bearer token")).thenReturn(userId);
        when(calorieHistoryService.fetchCalorieHistoryByPeriod(startDate, endDate, userId)).thenReturn(
                calorieGraphDtoList);
        Map<String, Object> requestBody = new HashMap<>();
        ResponseEntity<GetResponse> responseEntity = trackersController.fetchCalorieHistory(
                startDate, endDate, "Bearer token");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // CalorieHistoryException is thrown
        when(calorieHistoryService.fetchCalorieHistoryByPeriod(startDate, endDate, userId))
                .thenThrow(new CalorieHistoryException("Failed to retrieved calorie history"));
        ResponseEntity<GetResponse> responseEntity2 = trackersController.fetchCalorieHistory(
                startDate, endDate, "Bearer token");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
    }


}
