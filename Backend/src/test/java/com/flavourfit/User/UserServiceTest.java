package com.flavourfit.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;
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
}

