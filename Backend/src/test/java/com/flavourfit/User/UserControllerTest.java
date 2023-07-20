package com.flavourfit.User;

import com.flavourfit.Authentication.AuthController;
import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.PaymentException;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.ResponsesDTO.AuthResponse;
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
        // Pass Case
        Map<String, Object> requestValid = new HashMap<>();
        requestValid.put("userID", "32");
        requestValid.put("cardNumber", "9876543210123456");
        requestValid.put("mmyy","0223");
        requestValid.put("cvv", "667");

        ResponseEntity responseEntity1 = userController.getUserPaymentForPremium((String) requestValid.get("userID"),requestValid);

        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals((String) requestValid.get("userID"), responseEntity1.getBody());

        // Fail Case
        Map<String, Object> requestInvalid = new HashMap<>();
        requestInvalid.put("userID", "32");
        requestInvalid.put("cardNumber", "9873210123456");
        requestInvalid.put("mmyy","1523");
        requestInvalid.put("cvv", "667");

        ResponseEntity responseEntity2 = userController.getUserPaymentForPremium((String) requestInvalid.get("userID"),requestInvalid);

        assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
        assertEquals((String) requestInvalid.get("userID"), responseEntity2.getBody());
    }

}
