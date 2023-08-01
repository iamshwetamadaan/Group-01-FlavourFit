package com.flavourfit.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private IUserDao userDao;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testgetUserBymembership() throws Exception {
        int testUserId = 1;
        PremiumUserDto testUser = new PremiumUserDto();
        testUser.setUserId(testUserId);

        when(userDao.getUserBymembership(testUserId)).thenReturn(testUser);

        PremiumUserDto user = userService.getUserBymembership(testUserId);
        assertEquals(testUser.getUserId(), user.getUserId());

        when(userDao.getUserBymembership(testUserId)).thenReturn(null);
        user = userService.getUserBymembership(testUserId);
        assertNull(user);
    }
    @Test
    public void testResetPassword() throws Exception {
        int userID = 1;
        String newPassword = "ValidPassword";
        String encodedPassword = "EncodedPassword";

        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(userDao.resetUserPassword(userID, encodedPassword)).thenReturn(true);

        boolean result = userService.resetPassword(userID, newPassword);

        // Assert
        assertTrue(result);
        verify(passwordEncoder).encode(newPassword);
    }

    @Test
    public void testPaymentForPremium() throws Exception {
        // Arrange
        int userID = 1;
        PremiumUserPaymentDetailsDto details = new PremiumUserPaymentDetailsDto();
        details.setCardNumber("1234567812345678");
        details.setCvv("123");
        details.setExpiryMonth("12");
        details.setExpiryYear("30");

        when(userDao.userToPremiumPayment(userID, details)).thenReturn(1234);

        // Act
        int paymentID = userService.paymentForPremium(userID, details);

        // Assert
        assertEquals(1234, paymentID);

        verify(userDao).userToPremiumPayment(userID, details);
    }

    //needs to be fixed
    @Test
    public void testStartExtendPremium() throws Exception {
        // Arrange
        int userID = 1;
        int paymentID = 123;

        String startDate = "2007-06-27";
        String expiryDate = "2017-06-23";

        when(userDao.startExtendPremiumMembership(userID, startDate, expiryDate, paymentID)).thenReturn(567);
        when(userDao.updateUserPayment(userID, paymentID, 567)).thenReturn(true);

        // Act
        boolean result = userService.startExtendPremium(userID, paymentID);

        // Assert
        assertTrue(result);
        verify(userDao).startExtendPremiumMembership(userID, startDate, expiryDate, paymentID);
        verify(userDao).updateUserPayment(userID, paymentID, 567);
    }
}

