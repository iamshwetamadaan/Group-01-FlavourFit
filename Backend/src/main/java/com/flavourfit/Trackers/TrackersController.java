package com.flavourfit.Trackers;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.CalorieHistoryException;
import com.flavourfit.Helpers.DateHelpers;
import com.flavourfit.ResponsesDTO.GetResponse;
import com.flavourfit.ResponsesDTO.PutResponse;
import com.flavourfit.Security.JwtService;
import com.flavourfit.Trackers.Calories.*;
import com.flavourfit.Trackers.Water.IWaterHistoryService;
import com.flavourfit.Trackers.Water.WaterHistoryDto;
import com.flavourfit.User.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trackers")
@CrossOrigin
public class TrackersController {
    private static Logger logger = LoggerFactory.getLogger(TrackersController.class);

    private ICalorieHistoryService calorieHistoryService;
    private IWaterHistoryService waterHistoryService;
    private IAuthService authService;


    @Autowired
    public TrackersController(
            ICalorieHistoryService calorieHistoryService, IWaterHistoryService waterHistoryService,
            IAuthService authService
    ) {
        this.calorieHistoryService = calorieHistoryService;
        this.waterHistoryService = waterHistoryService;
        this.authService = authService;
    }

    @Autowired
    public void setCalorieHistoryService(ICalorieHistoryService calorieHistoryService) {
        this.calorieHistoryService = calorieHistoryService;
    }

    @Autowired
    public void setWaterHistoryService(IWaterHistoryService waterHistoryService) {
        this.waterHistoryService = waterHistoryService;
    }


    @PutMapping("/record-calories")
    public ResponseEntity<Object> recordCalories(
            @RequestBody Map<String, Object> requestBody,
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controlled method recordCalories()");

        double calorieCount = (double) requestBody.get("calorieCount");
        int userId = authService.extractUserIdFromToken(token);

        try {
            logger.info("Updating calorie count through calorieHistoryService.");
            this.calorieHistoryService.recordCalorieUpdate(calorieCount, userId);
            Map<String, Object> data = new HashMap<>();

            logger.info("Fetching the total calorie count for current date.");
            CalorieHistoryDto todaysCalorieCount = this.calorieHistoryService.fetchCalorieByUserIdDate(
                    DateHelpers.getCurrentDateString(), userId);
            data.put("todaysCalorieCount", todaysCalorieCount.getCalorieCount());

            logger.info("Updated record count for User #" + userId + ". Returning response through api");
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully recorded calorie count", data));
        } catch (CalorieHistoryException e) {
            logger.error("Bad api request during recordCalorieCount()");
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to record calorieCount"));
        }
    }

    @PutMapping("/record-waterIntake")
    public ResponseEntity<Object> recordWaterIntake(
            @RequestBody Map<String, Object> request,
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method recordWaterIntake()");
        double waterIntake = (Double) request.get("waterIntake");
        int userId = authService.extractUserIdFromToken(token);

        try {
            logger.info("Updating water intake through waterHistoryService.");
            this.waterHistoryService.recordWaterIntake(waterIntake, userId);
            Map<String, Object> data = new HashMap<>();

            logger.info("Fetching the total water intake for current date.");
            WaterHistoryDto todaysWaterIntake = this.waterHistoryService.fetchWaterIntakeByUserIdDate(
                    DateHelpers.getCurrentDateString(), userId);
            data.put("todaysWaterIntake", todaysWaterIntake.getWaterIntake());

            logger.info("Updated record count. Returning response through api");
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully recorded Water intake", data));
        } catch (SQLException e) {
            logger.error("Bad api request during recordWaterIntake()");
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to record Water intake"));
        }
    }

    @GetMapping("/calorie-history")
    public ResponseEntity<GetResponse> fetchCalorieHistory(
            @RequestBody Map<String, Object> request,
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method recordWaterIntake()");
        String startDate = (String) request.get("startDate");
        String endDate = (String) request.get("endDate");
        int userId = authService.extractUserIdFromToken(token);

        try {
            List<CalorieGraphDto> calories = this.calorieHistoryService.fetchCalorieHistoryByPeriod(startDate, endDate, userId);
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully retrieved calorie history", calories));
        } catch (CalorieHistoryException e) {
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to retrieved calorie history:" + e.getMessage()));
        }
    }
}