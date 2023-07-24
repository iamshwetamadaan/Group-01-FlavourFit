package com.flavourfit.HealthCoach;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Feeds.FeedDto;
import com.flavourfit.ResponsesDTO.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/health-coach")
public class HealthCoachController {
    private static Logger logger = LoggerFactory.getLogger(HealthCoachController.class);

    private IHealthCoachService healthCoachService;
    private IAuthService authService;
    @Autowired
    public HealthCoachController(IHealthCoachService healthCoachService, IAuthService authService) {
        this.healthCoachService = healthCoachService;
        this.authService = authService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<GetResponse> getAllHealthCoaches() {
        logger.info("Entered controller method getAllHealthCoaches()");
        try {
            ArrayList<HealthCoachDto> coaches = this.healthCoachService.getAllHealthCoaches();
            logger.info("Retrieved list of health coaches");
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully retrieved health Coaches information", coaches));
        } catch (Exception e) {
            logger.error("Failed to retrieve health coaches information");
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to retrieve health coaches information:" + e.getMessage()));
        }
    }
}
