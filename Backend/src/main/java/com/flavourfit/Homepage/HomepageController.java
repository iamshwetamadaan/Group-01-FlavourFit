package com.flavourfit.Homepage;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.ResponsesDTO.PutResponse;
import com.flavourfit.User.IUserService;
import com.flavourfit.User.UserController;
import com.flavourfit.User.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomepageController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    private IAuthService authService;

    private IHomepageService homepageService;

    @Autowired
    public HomepageController( IAuthService authService , IHomepageService homepageService) {
        this.authService = authService;
        this.homepageService = homepageService;
    }

    @GetMapping("/exercises")
    public ResponseEntity<PutResponse> getExerciseByUser(
            @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method getExcercise by user");
        int userId = this.authService.extractUserIdFromToken(token);
//        int userId=7;
        try {
            HashMap<String, Object> result = homepageService.getExerciseByUser(userId);
                return ResponseEntity.ok().body(new PutResponse(true, "Successfully obtained the routines", result));
        } catch (UserNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, e.getMessage()));
        }
        catch (SQLException e){
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, e.getMessage()));
        }
    }
}
