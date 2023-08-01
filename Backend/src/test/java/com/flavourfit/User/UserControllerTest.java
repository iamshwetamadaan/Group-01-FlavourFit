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
    public void userPremiumPaymentTest() throws PaymentException {
        /**
        // Pass Case
        PremiumUserPaymentDetailsDto requestValid = new PremiumUserPaymentDetailsDto();
        int user_id = 1;
        requestValid.setCardNumber("9876543210123456");
        requestValid.setExpiryMonth("02");
        requestValid.setExpiryYear("23");
        requestValid.setCvv("667");
        requestValid.setStartDate();
        requestValid.setEndDate();

        ResponseEntity responseEntity1 = userController.getUserPaymentForPremium(, requestValid);

        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals((String) requestValid.get("userID"), responseEntity1.getBody());
         **/
    }

    @Test
    public void editUserTest() throws SQLException{
        UserDto userDto = new UserDto();
        userDto.setUserId(1);
        String token = "in valid token";
        when(authService.extractUserIdFromToken(token)).thenReturn(1); // Assuming the token is valid
        when(userService.updateUser(any(UserDto.class))).thenReturn(1); // Assuming the update is successful

        // Act
        ResponseEntity<Object> response = userController.editUser(userDto, token);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof PutResponse);
        PutResponse putResponse = (PutResponse) response.getBody();
        assertFalse(putResponse.isSuccess());
    }

}
