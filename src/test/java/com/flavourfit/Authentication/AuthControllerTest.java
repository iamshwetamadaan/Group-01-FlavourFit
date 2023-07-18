package com.flavourfit.Authentication;

import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.ResponsesDTO.AuthResponse;
import com.flavourfit.User.UserDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    @Mock
    private IAuthService authService;

    @InjectMocks
    private AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void authenticateUserTest() throws UserNotFoundException {
        // Happy case
        UserDto user = new UserDto();
        user.setEmail("username");
        user.setPassword("password");
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken("token");

        when(authService.authenticateUser(user)).thenReturn(authResponse);

        ResponseEntity responseEntity = authController.authenticateUser(user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(authResponse, responseEntity.getBody());

        // Exception case
        when(authService.authenticateUser(user)).thenThrow(new UserNotFoundException("User not found"));

        responseEntity = authController.authenticateUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid credentials", responseEntity.getBody());
    }

}