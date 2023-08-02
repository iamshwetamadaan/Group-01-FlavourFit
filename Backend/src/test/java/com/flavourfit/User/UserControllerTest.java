package com.flavourfit.User;

import com.flavourfit.Authentication.AuthController;
import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.PaymentException;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.ResponsesDTO.AuthResponse;
import com.flavourfit.ResponsesDTO.PutResponse;
import com.flavourfit.Trackers.Weights.IWeightHistoryService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    UserController userController;
    @Mock
    IUserService userService;
    @Mock
    private IAuthService authService;

    @InjectMocks
    private AuthController authController;

    @Mock
    IWeightHistoryService weightHistoryService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUserPaymentForPremiumTest() throws PaymentException, SQLException {

        int userId = 1;
        String token = "valid-token";
        PremiumUserPaymentDetailsDto request = new PremiumUserPaymentDetailsDto();
        request.setCardNumber("1111111111111111");
        request.setExpiryMonth("11");
        request.setExpiryYear("12");
        request.setCvv("112");

        when(authService.extractUserIdFromToken(token)).thenReturn(1);
        when(userService.paymentForPremium(userId, request)).thenReturn(1);

        ResponseEntity<PutResponse> response = userController.getUserPaymentForPremium(token, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        PutResponse responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(!responseBody.isSuccess());
    }

    @Test
    public void resetPasswordTest() throws SQLException {

        int userId = 1;
        String newPassword = "NewValidPassword";
        String token = "token";
        Map<String, Object> request = new HashMap<>();
        request.put("newPassword", newPassword);

        when(authService.extractUserIdFromToken(token)).thenReturn(1); // Assuming the token is valid

        ResponseEntity<Object> response = userController.resetPassword(request, token);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PutResponse responseBody = (PutResponse) response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.isSuccess());
        assertEquals("Successfully updated password", responseBody.getMessage());

    }

    @Test
    void getUserByIDTest() {
        String token = "validToken";
        int userId = 1;
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);

        // Mocking the authService to return a fixed userId when a valid token is provided
        when(authService.extractUserIdFromToken(token)).thenReturn(userId);

        when(userService.fetchUserById(userId)).thenReturn(userDto);

        ResponseEntity<PutResponse> result;
        PutResponse response;

        when(userService.fetchUserById(userId)).thenReturn(null);
        result = userController.getUserByID(token);
        response = result.getBody();
        assertEquals(false, response.isSuccess());
        assertEquals("Failed to load user details", response.getMessage());
    }

    @Test
    void updateUserWeightTest() {
        String token = "validToken";
        int userId = 1;
        double weight = 70.5;
        Map<String, Double> body = new HashMap<>();
        body.put("weight", weight);

        when(authService.extractUserIdFromToken(token)).thenReturn(userId);
        ResponseEntity<PutResponse> result = userController.updateUserWeight(body, token);
        PutResponse response = result.getBody();
        assertEquals(true, response.isSuccess());
        assertEquals("Updated weight of user successfully", response.getMessage());

        body.remove("weight");
        result = userController.updateUserWeight(body, token);
        response = result.getBody();
        assertEquals(false, response.isSuccess());
        assertEquals("Weight not sent in body", response.getMessage());

        when(authService.extractUserIdFromToken(token)).thenThrow(new RuntimeException("Weight not sent in body"));
        result = userController.updateUserWeight(body, token);
        response = result.getBody();
        assertEquals(false, response.isSuccess());
        assertEquals("Weight not sent in body", response.getMessage());
    }

    public void startPremiumMembershipTest() throws Exception {

        String token = "token";
        int userID = 1;
        int paymentID = 123;

        when(authService.extractUserIdFromToken(anyString())).thenReturn(userID);
        when(userService.startExtendPremium(anyInt(), anyInt())).thenReturn(true);

        ResponseEntity<PutResponse> response = userController.startPremiumMembership(token, paymentID);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        PutResponse responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(!responseBody.isSuccess());

    }
}
