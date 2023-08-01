package com.flavourfit.User;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.Trackers.Weights.IWeightHistoryService;
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
    private final IUserService userService;
    private final IAuthService authService;

    private final IWeightHistoryService weightHistoryService;

    @Autowired
    public UserController(
            IUserService userService, IAuthService authService,
            IWeightHistoryService weightHistoryService
    ) {
        this.userService = userService;
        this.authService = authService;
        this.weightHistoryService = weightHistoryService;
    }

    @PutMapping({"/reset-password"})
    public ResponseEntity<Object> resetPassword(
            @RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controlled method resetPassword()");
        int userId = authService.extractUserIdFromToken(token);
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
                userdata.put("userDetails", userDto);
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

    @PostMapping("/make-payment")
    public ResponseEntity<PutResponse> getUserPaymentForPremium(
            @RequestHeader("Authorization") String token, @RequestBody PremiumUserPaymentDetailsDto request
    ) {
        logger.info("Entered controller method getUserPaymentForPremium()");
        int userID = this.authService.extractUserIdFromToken(token);
        try {
            if (request != null) {
                logger.info("Successfully loaded premium user payment details");
                int paymentID = this.userService.paymentForPremium(userID, request);

                if (paymentID != 0) {
                    return ResponseEntity.ok()
                            .body(new PutResponse(true, "Successfully completed user premium membership payment"));
                } else {
                    return ResponseEntity.badRequest().body(new PutResponse(false, "Failed payment for user"));
                }
            } else {
                logger.error("Payment for user not required");
                return ResponseEntity.badRequest().body(new PutResponse(false, "Failed payment for user since payment for user not required "));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed payment for user"));
        }
    }

    @PostMapping("/get-premium")
    public ResponseEntity<PutResponse> startPremiumMembership(
            @RequestHeader("Authorization") String token, @RequestBody int paymentID
    ) {
        logger.info("Entered controller method startPremiumMembership()");
        int userID = this.authService.extractUserIdFromToken(token);
        try {
            if (paymentID != 0) {
                logger.info("Successfully loaded premium user payment details");
                boolean startExtendMembership = this.userService.startExtendPremium(userID, paymentID);

                if (startExtendMembership) {
                    return ResponseEntity.ok()
                            .body(new PutResponse(true, "Successfully completed user premium membership initialization/extension"));
                } else {
                    return ResponseEntity.badRequest().body(new PutResponse(false, "Failed membership initialization/extension for user"));
                }
            } else {
                logger.error("Payment for user not required");
                return ResponseEntity.badRequest().body(new PutResponse(false, "Failed membership initialization/extension for user "));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed payment for user"));
        }
    }

    @PutMapping("/update-weight")
    public ResponseEntity<PutResponse> updateUserWeight(
            @RequestBody Map<String, Double> body, @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method updateUserWeight()");
        int userId;

        try {
            userId = authService.extractUserIdFromToken(token);
        } catch (Exception e) {
            logger.error("Failed to record comment: ", e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, "Token not valid" + e.getMessage()));
        }

        if (body.get("weight") == null) {
            logger.error("Weight not sent in body");
            return ResponseEntity.badRequest().body(new PutResponse(false, "Weight not sent in body"));
        }

        try {
            double weight = body.get("weight");
            this.userService.updateUserWeight(weight, userId);
            this.weightHistoryService.recordWeight(weight, userId);
            logger.info("Updated weight of user {} successfully", userId);
            return ResponseEntity.ok().body(new PutResponse(true, "Updated weight of user successfully"));
        } catch (Exception e) {
            logger.error("Failed to record comment: ", e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, "Token not valid" + e.getMessage()));
        }
    }

}
