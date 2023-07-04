package com.flavourfit.Trackers;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Helpers.DateHelpers;
import com.flavourfit.ResponsesDTO.PutResponse;
import com.flavourfit.Trackers.Calories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/trackers")
public class TrackersController {
    private static Logger logger = LoggerFactory.getLogger(TrackersController.class);

    private ICalorieHistoryService calorieHistoryService;

    @Autowired
    public TrackersController(ICalorieHistoryService calorieHistoryService) {
        this.calorieHistoryService = calorieHistoryService;
    }


    @PutMapping("/record-calories")
    public ResponseEntity<Object> recordCalories(@RequestBody Map<String, Object> request) {
        logger.info("Entered controlled method recordCalories()");
        double calorieCount = (Double) request.get("calorieCount");
        try {
            logger.info("Updating calorie count through calorieHistoryService.");
            this.calorieHistoryService.recordCalorieUpdate(calorieCount, 1);
            Map<String, Object> data = new HashMap<>();

            logger.info("Fetching the total calorie count for current date.");
            CalorieHistoryDto todaysCalorieCount = this.calorieHistoryService.fetchCalorieByUserIdDate(DateHelpers.getCurrentDateString(), 1);
            data.put("todaysCalorieCount", todaysCalorieCount.getCalorieCount());

            logger.info("Updated record count. Returning response through api");
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully recorded calorie count", data));
        } catch (SQLException e) {
            logger.error("Bad api request during recordCalorieCount()");
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to record calorieCount"));
        }
    }
}
