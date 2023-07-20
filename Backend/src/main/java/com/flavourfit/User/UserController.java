package com.flavourfit.User;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import com.flavourfit.ResponsesDTO.PutResponse;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    private IUserService userService;
    private IAuthService authService;

    @Autowired
    public UserController(IUserService userService, IAuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PutMapping({"/reset-password"})
    public ResponseEntity<Object> resetPassword(
            @RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controlled method resetPassword()");
        int userId = authService.extractUserIdFromToken(token);
        ;
        String newPassword = (String) request.get("newPassword");
        try {
            logger.info("Updated controlled method resetPassword()");
            this.userService.resetPassword(userId, newPassword);
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully updated password"));
        } catch (Exception e) {
            logger.error("Bad api request during resetPassword()");
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to update password"));
        }
    }

    @PutMapping("/update-user")
    public ResponseEntity<Object> editUser(
            @RequestBody UserDto user, @RequestHeader("Authorization") String token
    ) throws SQLException {
        logger.info("Entered controller method editUser()");

        logger.info("Invoking method updatedUser() of userService");
        int userId = this.authService.extractUserIdFromToken(token);
        user.setUserId(userId);
        int count = userService.updateUser(user);

        if (count == 0) {
            logger.error("User details invalid");
            return ResponseEntity.badRequest().body(new PutResponse(false, "User details invalid"));
        } else {
            logger.info("User details successfully updated");
            return ResponseEntity.ok().body(new PutResponse(true, "User details successfully updated", user));
        }
    }

    @GetMapping("/get-current-user")
    public ResponseEntity<PutResponse> getUserByID(@RequestHeader("Authorization") String token) {
        logger.info("Entered controller method getUserById()");
        int userID = this.authService.extractUserIdFromToken(token);
        try {
            Map<String, Object> userdata = new HashMap<>();
            UserDto userDto = this.userService.fetchUserById(userID);
            if (userDto != null) {
                logger.info("Successfully loaded user details.");
                userdata.put("user details", userDto);
                return ResponseEntity.ok().body(new PutResponse(true, "Successfully loaded user details", userdata));
            } else {
                logger.error("Failed to load user details");
                return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to load user details"));
            }
        } catch (UserNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/get-premium-membership")
    public ResponseEntity<PutResponse> getUserByMembership(@RequestHeader("Authorization") String token) {
        logger.info("Entered controller method getUserByMembership()");
        int userID = this.authService.extractUserIdFromToken(token);
        try {
            Map<String, Object> userdata = new HashMap<>();
            PremiumUserDto premiumuserDto = this.userService.getUserBymembership(userID);
            if (premiumuserDto != null) {
                logger.info("Successfully loaded premium user details");
                userdata.put("Premium user details", premiumuserDto);
                return ResponseEntity.ok()
                                     .body(new PutResponse(true, "Successfully loaded premium user details", userdata));
            } else {
                logger.error("Failed to load user details");
                return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to load user details"));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to load user details"));
        }
    }

}
