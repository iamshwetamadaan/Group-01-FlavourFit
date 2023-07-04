package com.flavourfit.Trackers;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Helpers.DateHelpers;
import com.flavourfit.Responses.PutResponse;
import com.flavourfit.Trackers.Calories.CalorieHistoryDaoImpl;
import com.flavourfit.Trackers.Calories.CalorieHistoryServiceImpl;
import com.flavourfit.Trackers.Calories.ICalorieHistoryDao;
import com.flavourfit.Trackers.Calories.ICalorieHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/trackers")
public class TrackersController {

    private ICalorieHistoryService calorieHistoryService;
    private ICalorieHistoryDao calorieHistoryDao;
    private IDatabaseManager database;

    public TrackersController() {
        this.database = new DatabaseManagerImpl();
        this.database.connect();
        this.calorieHistoryDao = new CalorieHistoryDaoImpl(this.database);
        this.calorieHistoryService = new CalorieHistoryServiceImpl(this.calorieHistoryDao);
    }

    @PutMapping("/record-calories")
    public ResponseEntity<Object> recordCalories(@RequestBody Map<String, Object> request) {
        double calorieCount = (Double) request.get("calorieCount");
        try {
            this.calorieHistoryService.recordCalorieUpdate(calorieCount, 1);
            Map<String, Object> data = new HashMap<>();
            double todaysCalorieCount = this.calorieHistoryService.fetchCalorieCountByDate(DateHelpers.getCurrentDateString(), 1);
            data.put("todaysCalorieCount", todaysCalorieCount);
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully recorded calorie count", data));
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to record calorieCount"));
        }
    }
}
