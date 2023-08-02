package com.flavourfit.User;

import com.flavourfit.Exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

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
    public void updateUserTest() throws SQLException{
        UserDto userDto = new UserDto();

        when(userDao.updateUser(userDto)).thenReturn(1); // Assuming the update is successful

        int result = userService.updateUser(userDto);

        assertEquals(1, result);
    }

    @Test
    void fetchUserByIdTest() throws Exception {
        int userId = 42;
        UserDto expectedUser = new UserDto(); // Populate with expected data
        when(userDao.getUserById(userId)).thenReturn(expectedUser);

        UserDto actualUser = userService.fetchUserById(userId);
        assertEquals(expectedUser, actualUser, "User must match the expected result");

        when(userDao.getUserById(userId)).thenThrow(SQLException.class);
        assertThrows(UserNotFoundException.class, () -> userService.fetchUserById(userId), "Must throw UserNotFoundException for SQLException");
    }

    @Test
    void fetchUserByEmailTest() throws Exception {
        String email = "test@example.com";
        UserDto expectedUser = new UserDto(); // Populate with expected data
        when(userDao.getUserByEmail(email)).thenReturn(expectedUser);

        UserDto actualUser = userService.fetchUserByEmail(email);
        assertEquals(expectedUser, actualUser, "User must match the expected result");

        when(userDao.getUserByEmail(email)).thenThrow(SQLException.class);
        assertThrows(UserNotFoundException.class, () -> userService.fetchUserByEmail(email), "Must throw UserNotFoundException for SQLException");
    }

    @Test
    void updateUserWeightTest() throws Exception {
        int userId = 42;
        double weight = 70.5;

        doNothing().when(userDao).updateUserWeight(weight, userId);
        assertDoesNotThrow(() -> userService.updateUserWeight(weight, userId), "Should not throw any exception for valid input");

        assertThrows(UserNotFoundException.class, () -> userService.updateUserWeight(weight, 0), "Must throw UserNotFoundException for invalid user ID");

        assertThrows(RuntimeException.class, () -> userService.updateUserWeight(0, userId), "Must throw RuntimeException for invalid weight value");

        doThrow(SQLException.class).when(userDao).updateUserWeight(weight, userId);
        assertThrows(UserNotFoundException.class, () -> userService.updateUserWeight(weight, userId), "Must throw UserNotFoundException for SQLException");
    }

    @Test
    void fetchUserCurrentWeightTest() throws Exception {
        int userId = 42;
        double expectedWeight = 70.5;

        when(userDao.getUserCurrentWeight(userId)).thenReturn(expectedWeight);
        double actualWeight = userService.fetchUserCurrentWeight(userId);
        assertEquals(expectedWeight, actualWeight, "Weight must match the expected result");

        assertThrows(UserNotFoundException.class, () -> userService.fetchUserCurrentWeight(0), "Must throw UserNotFoundException for invalid user ID");

        when(userDao.getUserCurrentWeight(userId)).thenThrow(SQLException.class);
        assertThrows(RuntimeException.class, () -> userService.fetchUserCurrentWeight(userId), "Must throw RuntimeException for SQLException");
    }

}

