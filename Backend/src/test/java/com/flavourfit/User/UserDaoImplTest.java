package com.flavourfit.User;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.Exceptions.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.List;

public class UserDaoImplTest {
    @Mock
    private DatabaseManagerImpl database;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    Statement statement;

    @Mock
    private ResultSet resultSet;

    private UserDaoImpl userDao;

    @Before
    public void initMocks() throws SQLException {
        MockitoAnnotations.openMocks(this);
        reset(database, connection, resultSet, preparedStatement);
        when(database.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(connection.createStatement()).thenReturn(statement);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        userDao = new UserDaoImpl(database);
    }

    @BeforeEach
    public void resetMocks() throws SQLException {
        reset(resultSet);
    }

    @Test
    public void testGetAllUsers() throws SQLException {
        UserDto user = new UserDto();
        user.setUserId(1);
        user.setEmail("test@test.com");
        user.setFirstName("Fname");
        user.setLastName("Lname");

        when(statement.executeQuery("SELECT * from Users;")).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("User_id")).thenReturn(user.getUserId()); // You can add more mock return values based on the user fields
        when(resultSet.getString("Email")).thenReturn(user.getEmail());
        when(resultSet.getString("First_name")).thenReturn(user.getFirstName());
        when(resultSet.getString("Last_name")).thenReturn(user.getLastName());

        // Test the happy path
        List<UserDto> users = userDao.getAllUsers();
        assertEquals(1, users.size());
        assertEquals(user.getUserId(), users.get(0).getUserId());
        assertEquals(user.getEmail(), users.get(0).getEmail());
        assertEquals(user.getFirstName(), users.get(0).getFirstName());
        assertEquals(user.getLastName(), users.get(0).getLastName());
        assertEquals(user.getFullName(), users.get(0).getFullName());

        when(connection.createStatement()).thenThrow(new SQLException("Failed to create statement"));
        assertThrows(SQLException.class, () -> userDao.getAllUsers());
    }

    @Test
    public void testGetUserByMembership() throws Exception {
        int testUserId = 1;
        PremiumUserDto testUser = new PremiumUserDto();
        testUser.setUserId(testUserId);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("User_id")).thenReturn(testUser.getUserId());

        PremiumUserDto user = userDao.getUserBymembership(testUserId);
        assertEquals(testUser.getUserId(), user.getUserId());

        when(resultSet.next()).thenReturn(false);
        user = userDao.getUserBymembership(testUserId);
    }

    @Test
    public void getUserByIdTest() throws SQLException {
        UserDto user = new UserDto();
        user.setUserId(1);
        user.setEmail("test@test.com");
        user.setFirstName("fname");
        user.setLastName("lname");


        when(resultSet.next()).thenReturn(true, false); // Simulate a single row in the result
        when(resultSet.getInt("User_id")).thenReturn(user.getUserId());
        when(resultSet.getString("Email")).thenReturn(user.getEmail());
        when(resultSet.getString("First_name")).thenReturn(user.getFirstName());
        when(resultSet.getString("Last_name")).thenReturn(user.getLastName());
        UserDto result = userDao.getUserById(user.getUserId());

        assertNotNull(result);
        assertEquals(user.getUserId(), result.getUserId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
    }

    @Test
    public void addUserTest() throws SQLException {
        UserDto user = new UserDto();
        user.setUserId(1);
        user.setEmail("test@test.com");
        user.setFirstName("fname");
        user.setLastName("lname");
        user.setPassword("password");

        ResultSet keys = mock(ResultSet.class);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true, false);
        when(keys.getLong(1)).thenReturn(1L); // Simulate a generated user ID

        userDao.addUser(user);
        assertEquals(1, user.getUserId()); // Check if the user ID was set correctly

        assertThrows(SQLException.class, () -> {
            userDao.addUser(null);
        });
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        String email = "test@example.com";

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("Email")).thenReturn(email);

        UserDto result = userDao.getUserByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());

        when(resultSet.next()).thenReturn(false);

        result = userDao.getUserByEmail(email);

        assertNull(result);
    }

    @Test
    public void testResetUserPassword() throws Exception {
        UserDto user = new UserDto();
        user.setUserId(1);
        user.setPassword("newPassword");

        when(preparedStatement.executeUpdate()).thenReturn(user.getUserId());
        boolean result = userDao.resetUserPassword(user.getUserId(), user.getPassword());
        assertTrue(result);

        //Invalid ID Case
        user.setUserId(0);
        assertThrows(SQLException.class, () -> {
            userDao.resetUserPassword(user.getUserId(), user.getPassword());
        });
    }

    @Test
    public void testUpdateUserPayment() throws Exception {
        int userId = 1;
        int paymentID = 2;
        int premiumMembershipID = 3;

        when(preparedStatement.executeUpdate()).thenReturn(paymentID);
        boolean result = userDao.updateUserPayment(userId, paymentID, premiumMembershipID);
        assertTrue(result);

        //Invalid ID Case
        userId = 0;
        int finalUserId = userId;
        assertThrows(SQLException.class, () -> {
            userDao.updateUserPayment(finalUserId, paymentID, premiumMembershipID);
        });

    }

    @Test
    public void testStartExtendPremiumMembership() throws Exception {
        int userId = 1;
        String startDate = "2007-06-27";
        String endDate = "2008-06-27";
        int paymentID = 2;
        int premiumMembershipID = 3;

        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(premiumMembershipID);

        int result = userDao.startExtendPremiumMembership(userId, startDate, endDate, paymentID);
        assertEquals(result, premiumMembershipID);
    }

    @Test
    public void testUserToPremiumPayment() throws Exception {
        int userId = 1;
        PremiumUserPaymentDetailsDto details = new PremiumUserPaymentDetailsDto();
        details.setAmount(3000.0);
        int paymentID = 2;

        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(paymentID);

        int result = userDao.userToPremiumPayment(userId, details);
        assertEquals(result, paymentID);
    }

    @Test
    public void updateUserWeightTest() throws SQLException {
        double weight = 75.0;
        int userId = 1;

        userDao.updateUserWeight(weight, userId);

        verify(preparedStatement).setDouble(1, weight);
        verify(preparedStatement).setInt(2, userId);
        verify(preparedStatement).executeUpdate();

        assertThrows(UserNotFoundException.class, () -> {
            userDao.updateUserWeight(weight, 0);
        });
    }

    @Test
    public void getUserCurrentWeightTest() throws SQLException {
        int userId = 1;
        double currentWeight = 75.0;

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getDouble("Current_weight")).thenReturn(currentWeight);

        double result = userDao.getUserCurrentWeight(userId);

        assertEquals(currentWeight, result, 0);
        verify(preparedStatement).setInt(1, userId);

        // Sad path: invalid user ID
        assertThrows(UserNotFoundException.class, () -> {
            userDao.getUserCurrentWeight(0);
        });
    }
}