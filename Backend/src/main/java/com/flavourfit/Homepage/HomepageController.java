package com.flavourfit.Homepage;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.CalorieHistoryException;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.ResponsesDTO.GetResponse;
import com.flavourfit.ResponsesDTO.PutResponse;
import com.flavourfit.Trackers.Calories.CalorieGraphDto;
import com.flavourfit.User.IUserService;
import com.flavourfit.User.UserController;
import com.flavourfit.User.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomepageController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    private IAuthService authService;

    private IHomepageService homepageService;

    @Autowired
    public HomepageController(IAuthService authService, IHomepageService homepageService) {
        this.authService = authService;
        this.homepageService = homepageService;
    }

    @GetMapping("/exercises")
    public ResponseEntity<PutResponse> getExerciseByUser(
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method getExcercise by user");
        int userId = this.authService.extractUserIdFromToken(token);
        try {
            HashMap<String, Object> result = homepageService.getExerciseByUser(userId);
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully obtained the routines", result));
        } catch (UserNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, e.getMessage()));
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/tracker-summary")
    public ResponseEntity<GetResponse> fetchTrackerSummary(@RequestHeader("Authorization") String token) {
        logger.info("Entered controller method fetchTrackerSummary()");
        int userId;

        try {
            userId = authService.extractUserIdFromToken(token);
        } catch (Exception e) {
            logger.error("Failed to record comment: ", e.getMessage());
            return ResponseEntity.badRequest().body(new GetResponse(false, "Token not valid" + e.getMessage()));
        }

        try {
            Map trackerSummary = this.homepageService.fetchTrackerSummary(userId);
            logger.info("Fetched tracker summary for user {}", userId);
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully fetched trackerSummary", trackerSummary));
        } catch (Exception e) {
            logger.error("Failed to fetch tracker summary : {}", e.getMessage());
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to fetch tracker summary" + e.getMessage()));
        }
    }

    @GetMapping("/event-list")
    public ResponseEntity<GetResponse> fetcheventlist() {
        logger.info("Entered controller method fetcheventlist()");
        try {
            List<HomepageEventDto> eventlist = this.homepageService.fetcheventlist();
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully retrieved  eventlist", eventlist));
        } catch (CalorieHistoryException e) {
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to retrieved eventlist:" + e.getMessage()));
        }
    }
}
