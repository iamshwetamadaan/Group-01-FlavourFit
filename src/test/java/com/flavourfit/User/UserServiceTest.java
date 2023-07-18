package com.flavourfit.User;

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
    public void fetchAllUsersTest() throws SQLException {
        UserDto user1 = new UserDto(); // populate with your test data
        UserDto user2 = new UserDto(); // populate with your test data
        List<UserDto> mockUsers = Arrays.asList(user1, user2);

        when(userDao.getAllUsers()).thenReturn(mockUsers);

        // Happy path
        String result = null;
        try {
            result = userService.fetchAllUsers();
            String expected = user1.toString() + "\n" + user2.toString() + "\n";
            assertEquals(expected, result);
        } catch (SQLException ex) {
            fail("SQLException was not expected here.");
        }

        // Reset the mock and simulate an exception.
        reset(userDao);
        when(userDao.getAllUsers()).thenThrow(SQLException.class);

        // Exception path
        assertThrows(SQLException.class, () -> userService.fetchAllUsers());
    }

}

