package com.flavourfit.User;

import com.flavourfit.Authentication.AuthController;
import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.PaymentException;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.ResponsesDTO.AuthResponse;
import com.flavourfit.ResponsesDTO.PutResponse;
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

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void userPremiumPaymentTest() throws PaymentException, SQLException {
        int userId = 1;
        String token = "valid-token";
        PremiumUserPaymentDetailsDto request = new PremiumUserPaymentDetailsDto();
        request.setCardNumber("1111111111111111");
        request.setExpiryMonth("11");
        request.setExpiryYear("12");
        request.setCvv("112");

        when(authService.extractUserIdFromToken(token)).thenReturn(1);
        when(userService.paymentForPremium(userId, request)).thenReturn(1);

        // Act
        ResponseEntity<PutResponse> response = userController.getUserPaymentForPremium(token, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        PutResponse responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(!responseBody.isSuccess());
        //assertEquals("Successfully completed user premium membership payment", responseBody.getMessage());

        //verify(authService).extractUserIdFromToken(token);
        //verify(userService).paymentForPremium(userId, request);
    }
    @Test
    public void testResetPasswordResponse() throws SQLException {
        // Arrange
        int userId = 1;
        String newPassword = "NewValidPassword";
        String token = "token";
        Map<String, Object> request = new HashMap<>();
        request.put("newPassword", newPassword);

        when(authService.extractUserIdFromToken(token)).thenReturn(1); // Assuming the token is valid

        // Act
        ResponseEntity<Object> response = userController.resetPassword(request, token);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PutResponse responseBody = (PutResponse) response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.isSuccess());
        assertEquals("Successfully updated password", responseBody.getMessage());

    }

    @Test
    public void startExtendPremiumMembershipTest() throws Exception {
        // Arrange
        String token = "token";
        int userID = 1;
        int paymentID = 123;

        when(authService.extractUserIdFromToken(anyString())).thenReturn(userID);
        when(userService.startExtendPremium(anyInt(), anyInt())).thenReturn(true);

        // Act
        ResponseEntity<PutResponse> response = userController.startPremiumMembership(token, paymentID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        PutResponse responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(!responseBody.isSuccess());

    }
}
