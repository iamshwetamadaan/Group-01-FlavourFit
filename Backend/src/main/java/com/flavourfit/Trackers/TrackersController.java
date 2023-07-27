package com.flavourfit.Trackers;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.CalorieHistoryException;
import com.flavourfit.Exceptions.WaterHistoryException;
import com.flavourfit.Exceptions.WeightHistoryException;
import com.flavourfit.Helpers.DateHelpers;
import com.flavourfit.ResponsesDTO.GetResponse;
import com.flavourfit.ResponsesDTO.PutResponse;
import com.flavourfit.ResponsesDTO.TrackersResponse;
import com.flavourfit.Trackers.Calories.*;
import com.flavourfit.Trackers.Water.IWaterHistoryService;
import com.flavourfit.Trackers.Water.WaterGraphDto;
import com.flavourfit.Trackers.Water.WaterHistoryDto;
import com.flavourfit.Trackers.Weights.IWeightHistoryService;
import com.flavourfit.Trackers.Weights.WeightGraphDto;
import com.flavourfit.Trackers.Weights.WeightHistoryDto;
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

    private IWeightHistoryService weightHistoryService;
    private IAuthService authService;

    private final IUserService userService;


    @Autowired
    public TrackersController(
            ICalorieHistoryService calorieHistoryService, IWaterHistoryService waterHistoryService,
            IWeightHistoryService weightHistoryService,
            IAuthService authService,
            IUserService userService
    ) {
        this.calorieHistoryService = calorieHistoryService;
        this.waterHistoryService = waterHistoryService;
        this.weightHistoryService = weightHistoryService;
        this.authService = authService;
        this.userService = userService;
    }

    @PutMapping("/record-calories")
    public ResponseEntity<Object> recordCalories(
            @RequestBody Map<String, String> requestBody,
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controlled method recordCalories()");

        double calorieCount = Double.parseDouble(requestBody.get("calorieCount"));
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
            @RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method recordWaterIntake()");
        double waterIntake = Double.parseDouble(request.get("waterIntake"));
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


    @PutMapping("/record-weight")
    public ResponseEntity<Object> recordWeight(
            @RequestBody Map<String, Object> request,
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method recordWeight()");
        double weight = (Double) request.get("weight");
        int userId = authService.extractUserIdFromToken(token);

        try {
            logger.info("Updating water intake through weightHistoryService.");
            this.weightHistoryService.recordWeight(weight, userId);
            Map<String, Object> data = new HashMap<>();

            logger.info("Fetching the total weight for current date.");
            WeightHistoryDto todaysWeight = this.weightHistoryService.fetchWeightByUserIdDate(
                    DateHelpers.getCurrentDateString(), userId);
            data.put("todaysWeight", todaysWeight.getWeight());

            logger.info("Updated record count. Returning response through api");
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully recorded Weight", data));
        } catch (SQLException e) {
            logger.error("Bad api request during recordWaterIntake()");
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to record Weight"));
        }
    }

    @GetMapping("/calorie-history")
    public ResponseEntity<GetResponse> fetchCalorieHistory(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method recordWaterIntake()");
        int userId = authService.extractUserIdFromToken(token);

        try {
            List<CalorieGraphDto> calories = this.calorieHistoryService.fetchCalorieHistoryByPeriod(startDate, endDate, userId);
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully retrieved calorie history", calories));
        } catch (CalorieHistoryException e) {
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to retrieved calorie history:" + e.getMessage()));
        }
    }

    @GetMapping("/water-history")
    public ResponseEntity<GetResponse> fetchWaterHistory(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method recordWaterIntake()");
        int userId = authService.extractUserIdFromToken(token);

        try {
            List<WaterGraphDto> calories = this.waterHistoryService.fetchWaterHistoryByPeriod(startDate, endDate, userId);
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully retrieved water history", calories));
        } catch (CalorieHistoryException e) {
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to retrieved water history:" + e.getMessage()));
        }
    }

    @GetMapping("/weight-history")
    public ResponseEntity<GetResponse> fetchWeightHistory(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method fetchWeightHistory()");
        int userId = authService.extractUserIdFromToken(token);

        try {
            List<WeightGraphDto> calories = this.weightHistoryService.fetchWeightHistoryByPeriod(startDate, endDate, userId);
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully retrieved weight history", calories));
        } catch (CalorieHistoryException e) {
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to retrieved weight history:" + e.getMessage()));
        }
    }

    @GetMapping("/getcalories-byuserIDDate")
    public ResponseEntity<GetResponse> fetchCaloriesbyUserIDDate(
            @RequestParam("Date") String Date,
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method fetchWaterIntakebyUserIDDate()");
        int userId = authService.extractUserIdFromToken(token);

        try {
            CalorieHistoryDto calories = this.calorieHistoryService.fetchCalorieByUserIdDate(Date, userId);
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully retrieved calories for the date ", calories));
        } catch (CalorieHistoryException e) {
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to retrieve calories for the date :" + e.getMessage()));
        }
    }

    @GetMapping("/getweight-byuserIDDate")
    public ResponseEntity<GetResponse> fetchWeightbyUserIDDate(
            @RequestParam("Date") String Date,
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method fetchWeightbyUserIDDate()");
        int userId = authService.extractUserIdFromToken(token);

        try {
            WeightHistoryDto weight = this.weightHistoryService.fetchWeightByUserIdDate(Date, userId);
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully retrieved weight for the date ", weight));
        } catch (WeightHistoryException e) {
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to retrieve weight for the date :" + e.getMessage()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getwaterIntake-byuserIDDate")
    public ResponseEntity<GetResponse> fetchWaterIntakebyUserIDDate(
            @RequestParam("Date") String Date,
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method fetchWaterIntakebyUserIDDate()");
        int userId = authService.extractUserIdFromToken(token);

        try {
            WaterHistoryDto waterIntake = this.waterHistoryService.fetchWaterIntakeByUserIdDate(Date, userId);
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully retrieved water intake for the date ", waterIntake));
        } catch (WaterHistoryException e) {
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to retrieve water intake for the date :" + e.getMessage()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<TrackersResponse> fetchTrackersByUserIdCurrent(
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method fetchWaterIntakebyUserIDDate()");
        int userId = authService.extractUserIdFromToken(token);

        try {
            WaterHistoryDto waterIntake = this.waterHistoryService.fetchWaterIntakeByUserIdCurrent(userId);
            CalorieHistoryDto calories = this.calorieHistoryService.fetchCalorieByUserIdCurrent(userId);
            double weight = this.userService.fetchUserCurrentWeight(userId);

            TrackersResponse response = new TrackersResponse(true, "Successfully retrieved water intake and calories", waterIntake, calories, weight);

            // Return the combined response
            return ResponseEntity.ok().body(response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}