package com.flavourfit.Trackers;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.CalorieHistoryException;
import com.flavourfit.Exceptions.WaterHistoryException;
import com.flavourfit.Exceptions.WeightHistoryException;
import com.flavourfit.Helpers.DateHelpers;
import com.flavourfit.ResponsesDTO.GetResponse;
import com.flavourfit.Trackers.Calories.CalorieGraphDto;
import com.flavourfit.Trackers.Calories.CalorieHistoryDto;
import com.flavourfit.Trackers.Calories.ICalorieHistoryService;
import com.flavourfit.Trackers.Water.IWaterHistoryService;
import com.flavourfit.Trackers.Water.WaterGraphDto;
import com.flavourfit.Trackers.Water.WaterHistoryDto;
import com.flavourfit.Trackers.Weights.IWeightHistoryService;
import com.flavourfit.Trackers.Weights.WeightGraphDto;
import com.flavourfit.Trackers.Weights.WeightHistoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TrackersControllerTest {
    @InjectMocks
    private TrackersController trackersController;

    @Mock
    private ICalorieHistoryService calorieHistoryService;

    @Mock
    private IWaterHistoryService waterHistoryService;

    @Mock
    private IWeightHistoryService weightHistoryService;

    @Mock
    private IAuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    //    trackersController = new TrackersController(calorieHistoryService, null, authService);
    }

    @Test
    public void recordCaloriesTest() throws CalorieHistoryException {
        double calorieCount = 2000;
        int userId = 1;
        String date = DateHelpers.getCurrentDateString();

        CalorieHistoryDto calorieHistoryDto = new CalorieHistoryDto(calorieCount, date, userId);

        when(authService.extractUserIdFromToken("Bearer token")).thenReturn(userId);
        when(calorieHistoryService.fetchCalorieByUserIdDate(date, userId)).thenReturn(calorieHistoryDto);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("calorieCount", String.valueOf(calorieCount));
        ResponseEntity<Object> responseEntity = trackersController.recordCalories(requestBody, "Bearer token");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // CalorieHistoryException is thrown
        when(calorieHistoryService.fetchCalorieByUserIdDate(date, userId)).thenThrow(
                new CalorieHistoryException("Failed to record calorieCount"));
        ResponseEntity<Object> responseEntity2 = trackersController.recordCalories(requestBody, "Bearer token");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
    }

    @Test
    public void recordWaterIntakeTest() throws WaterHistoryException, SQLException {
        double waterIntake = 1500;
        int userId = 1;
        String date = DateHelpers.getCurrentDateString();

        WaterHistoryDto waterHistoryDto = new WaterHistoryDto(waterIntake, date, userId);

        when(authService.extractUserIdFromToken("Bearer token")).thenReturn(userId);
        when(waterHistoryService.fetchWaterIntakeByUserIdDate(date, userId)).thenReturn(waterHistoryDto);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("waterIntake", String.valueOf(waterIntake));

        ResponseEntity<Object> responseEntity = trackersController.recordWaterIntake(requestBody, "Bearer token");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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



    @Test
    public void fetchWeightHistoryTest() throws WeightHistoryException {
        String startDate = "2023-07-10";
        String endDate = "2023-07-22";
        int userId = 1;

        WeightGraphDto weightGraphDto = new WeightGraphDto("2023-07-20", 2000d);
        List<WeightGraphDto> weightGraphDtoList = new ArrayList<>();
        weightGraphDtoList.add(weightGraphDto);

        // Success case
        when(authService.extractUserIdFromToken("Bearer token")).thenReturn(userId);
        when(weightHistoryService.fetchWeightHistoryByPeriod(startDate, endDate, userId)).thenReturn(
                weightGraphDtoList);
        Map<String, Object> requestBody = new HashMap<>();
        ResponseEntity<GetResponse> responseEntity = trackersController.fetchWeightHistory(
                startDate, endDate, "Bearer token");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    public void fetchWaterHistoryTest() throws WaterHistoryException {
        String startDate = "2023-07-10";
        String endDate = "2023-07-22";
        int userId = 1;

        WaterGraphDto waterGraphDto = new WaterGraphDto("2023-07-20", 2000d);
        List<WaterGraphDto> waterGraphDtoList = new ArrayList<>();
        waterGraphDtoList.add(waterGraphDto);

        // Success case
        when(authService.extractUserIdFromToken("Bearer token")).thenReturn(userId);
        when(waterHistoryService.fetchWaterHistoryByPeriod(startDate, endDate, userId)).thenReturn(
                waterGraphDtoList);
        Map<String, Object> requestBody = new HashMap<>();
        ResponseEntity<GetResponse> responseEntity = trackersController.fetchWaterHistory(
                startDate, endDate, "Bearer token");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        
    }
}
