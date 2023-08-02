package com.flavourfit.User;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.Recipes.RecipeDaoImpl;
import com.flavourfit.Trackers.Weights.WeightHistoryDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
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
    private Statement statement;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private UserDaoImpl userDao;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        reset(database,connection,preparedStatement,resultSet,statement);
        when(database.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(connection.prepareStatement(anyString(),anyInt())).thenReturn(preparedStatement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        userDao = new UserDaoImpl(database);
    }

    @BeforeEach
    public void initBeforeEach(){
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
        int testUserId = 1;
        UserDto expectedUser = new UserDto();
        expectedUser.setUserId(testUserId);
        expectedUser.setFirstName("User1");

        // Normal flow
        when(resultSet.next()).thenReturn(true).thenReturn(
                false); // This will ensure the while loop in getUserById() runs once and then exits
        when(resultSet.getInt("User_id")).thenReturn(expectedUser.getUserId());
        when(resultSet.getString("First_name")).thenReturn(expectedUser.getFirstName());

        UserDto user = userDao.getUserById(testUserId);
    }

    @Test
    public void addUserTest() throws Exception {
        UserDto testUser = new UserDto();
        testUser.setUserId(1);
        testUser.setFirstName("Test");
        testUser.setEmail("Test@test1111111.com");

        // Normal flow
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(1)).thenReturn(1L);

        // Reset for the next scenario
        reset(database, connection, preparedStatement, resultSet);
        setUp();

        // Null user scenario
        Exception exception = assertThrows(SQLException.class, () -> userDao.addUser(null));
        assertEquals("User object not valid!!", exception.getMessage());
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        String testEmail = "test@test.com";
        UserDto testUser = new UserDto();
        testUser.setUserId(1);
        testUser.setFirstName("Test");

        // Normal flow
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("User_id")).thenReturn(testUser.getUserId());
        when(resultSet.getString("First_name")).thenReturn(testUser.getFirstName());
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
        assertThrows(SQLException.class,()->{userDao.resetUserPassword(user.getUserId(), user.getPassword());});
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
        assertThrows(SQLException.class,()->{userDao.updateUserPayment(finalUserId, paymentID,premiumMembershipID);});

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

        int result = userDao.startExtendPremiumMembership(userId,startDate,endDate,paymentID);
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
        assertEquals(result,paymentID);
    }
}