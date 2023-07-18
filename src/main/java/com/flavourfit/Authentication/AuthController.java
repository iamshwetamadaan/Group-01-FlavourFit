package com.flavourfit.Authentication;

import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.ResponsesDTO.AuthResponse;
import com.flavourfit.User.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final IAuthService authService;
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    /**
     * API to login a user
     *
     * @param user -- User to be logged in
     * @return -- Response with token if login successful
     */
    @PostMapping(path = "/login")
    public ResponseEntity authenticateUser(@RequestBody UserDto user) {
        try {
            AuthResponse response = this.authService.authenticateUser(user);
            return ResponseEntity.ok().body(response);
        } catch (UserNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body("Invalid credentials");
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }
}
