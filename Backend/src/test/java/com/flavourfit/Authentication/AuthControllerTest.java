package com.flavourfit.Authentication;

import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.ResponsesDTO.AuthResponse;
import com.flavourfit.ResponsesDTO.PutResponse;
import com.flavourfit.User.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    private IAuthService authService;
    private AuthController authController;

    UserDto user;

    @BeforeEach
    void setUp() {
        authService = Mockito.mock(IAuthService.class);
        authController = new AuthController(authService);

        user = new UserDto();
        user.setEmail("test@email.com");
        user.setPassword("testpassword");
    }

    @Test
    void authenticateUserTest() {
        AuthResponse response = new AuthResponse();
        response.setEmail(user.getEmail());
        response.setSuccess(true);
        response.setToken("testToken");

        when(authService.authenticateUser(any(UserDto.class))).thenReturn(response);

        ResponseEntity result = authController.authenticateUser(user);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());

        when(authService.authenticateUser(any(UserDto.class))).thenThrow(new UserNotFoundException("User not found"));

        result = authController.authenticateUser(user);
        assertEquals(400, result.getStatusCodeValue());
        assertEquals("Invalid credentials", result.getBody());

        when(authService.authenticateUser(any(UserDto.class))).thenThrow(new RuntimeException("Some error"));

        result = authController.authenticateUser(user);
        assertEquals(400, result.getStatusCodeValue());
        assertEquals("Invalid credentials", result.getBody());
    }

    @Test
    void registerUserTest() {
        user.setUserId(1);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setEmail(user.getEmail());
        authResponse.setSuccess(true);
        authResponse.setToken("testToken");

        when(authService.registerUser(any(UserDto.class))).thenReturn(authResponse);

        ResponseEntity<Object> result = authController.registerUser(user);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(authResponse, result.getBody());

        reset(authService);

        when(authService.registerUser(any(UserDto.class))).thenThrow(new RuntimeException("Registration error"));

        result = authController.registerUser(user);
        assertEquals(400, result.getStatusCodeValue());
        assertTrue(result.getBody() instanceof PutResponse);
        PutResponse putResponse = (PutResponse) result.getBody();
        assertFalse(putResponse.isSuccess());
        assertEquals("Failed to register user", putResponse.getMessage());
    }
}
