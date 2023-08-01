package com.flavourfit.User;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.Recipes.RecipeDaoImpl;
import com.flavourfit.Trackers.Weights.WeightHistoryDaoImpl;
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
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
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
}


//package com.flavourfit.User;
//
//import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
//import com.flavourfit.DatabaseManager.IDatabaseManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import java.sql.*;
//import java.util.List;
//
//public class UserDaoImplTest {
//    @Mock
//    private DatabaseManagerImpl database;
//
//    @Mock
//    private Connection connection;
//
//    @Mock
//    private Statement statement;
//
//    @Mock
//    private PreparedStatement preparedStatement;
//
//    @Mock
//    private ResultSet resultSet;
//
//    @InjectMocks
//    private UserDaoImpl userDao;
//
//    @BeforeEach
//    public void setUp() throws SQLException {
//        MockitoAnnotations.openMocks(this);
//        reset(database, connection, preparedStatement, statement);
//        when(database.getConnection()).thenReturn(connection);
//        when(connection.createStatement()).thenReturn(statement);
//        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
//        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
//        when(statement.executeQuery(any())).thenReturn(resultSet);
//        when(preparedStatement.executeQuery()).thenReturn(resultSet);
//    }
//
//    @Test
//    public void getAllUsersTest() throws SQLException {
//        // Normal flow
//        when(resultSet.next()).thenReturn(true, true, true, false);
//        when(resultSet.getInt("User_id")).thenReturn(1, 2, 3);
//        when(resultSet.getString("First_name")).thenReturn("User1", "User2", "User3");
//        when(resultSet.getString("Last_name")).thenReturn("Last1", "Last2", "Last3");
//        when(resultSet.getString("Phone")).thenReturn("Phone1", "Phone2", "Phone3");
//        when(resultSet.getString("Email")).thenReturn("Email1", "Email2", "Email3");
//        when(resultSet.getInt("Age")).thenReturn(25, 30, 35);
//        when(resultSet.getString("Street_address")).thenReturn("Address1", "Address2", "Address3");
//        when(resultSet.getString("City")).thenReturn("City1", "City2", "City3");
//        when(resultSet.getString("State")).thenReturn("State1", "State2", "State3");
//        when(resultSet.getString("Zip_code")).thenReturn("Zip1", "Zip2", "Zip3");
//        when(resultSet.getDouble("Current_weight")).thenReturn(70.0, 80.0, 90.0);
//        when(resultSet.getDouble("Target_Weight")).thenReturn(65.0, 75.0, 85.0);
//        when(resultSet.getString("Type")).thenReturn("Type1", "Type2", "Type3");
//        when(resultSet.getString("Password")).thenReturn("Pass1", "Pass2", "Pass3");
//
//        List<UserDto> users = userDao.getAllUsers();
//        System.out.println("======="+users.toArray().toString());
//    }
//
//    @Test
//    public void testGetUserByMembership() throws Exception {
//        int testUserId = 1;
//        PremiumUserDto testUser = new PremiumUserDto();
//        testUser.setUserId(testUserId);
//
//        when(database.getConnection()).thenReturn(connection);
//        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
//        when(preparedStatement.executeQuery()).thenReturn(resultSet);
//        when(resultSet.next()).thenReturn(true).thenReturn(false);
//        when(resultSet.getInt("User_id")).thenReturn(testUser.getUserId());
//
//        PremiumUserDto user = userDao.getUserBymembership(testUserId);
//        assertEquals(testUser.getUserId(), user.getUserId());
//
//        when(resultSet.next()).thenReturn(false);
//        user = userDao.getUserBymembership(testUserId);
//    }
//
//
//    @Test
//    public void getUserByIdTest() throws SQLException {
//        int testUserId = 1;
//        UserDto expectedUser = new UserDto();
//        expectedUser.setUserId(testUserId);
//        expectedUser.setFirstName("User1");
//
//        // Normal flow
//        when(resultSet.next()).thenReturn(true).thenReturn(
//                false); // This will ensure the while loop in getUserById() runs once and then exits
//        when(resultSet.getInt("User_id")).thenReturn(expectedUser.getUserId());
//        when(resultSet.getString("First_name")).thenReturn(expectedUser.getFirstName());
//
//        UserDto user = userDao.getUserById(testUserId);
//    }
//
//    @Test
//    public void addUserTest() throws Exception {
//        UserDto testUser = new UserDto();
//        testUser.setUserId(1);
//        testUser.setFirstName("Test");
//        testUser.setEmail("Test@test1111111.com");
//
//        // Normal flow
//        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
//        when(resultSet.next()).thenReturn(true).thenReturn(false);
//        when(resultSet.getLong(1)).thenReturn(1L);
//
//
//        // Reset for the next scenario
//        reset(database, connection, preparedStatement, resultSet);
//        setUp();
//
//        // Null user scenario
//        Exception exception = assertThrows(SQLException.class, () -> userDao.addUser(null));
//        assertEquals("User object not valid!!", exception.getMessage());
//
//    }
//
//    @Test
//    public void testGetUserByEmail() throws Exception {
//        String testEmail = "test@test.com";
//        UserDto testUser = new UserDto();
//        testUser.setUserId(1);
//        testUser.setFirstName("Test");
//
//        // Normal flow
//        when(preparedStatement.executeQuery()).thenReturn(resultSet);
//        when(resultSet.next()).thenReturn(true).thenReturn(false);
//        when(resultSet.getInt("User_id")).thenReturn(testUser.getUserId());
//        when(resultSet.getString("First_name")).thenReturn(testUser.getFirstName());
//    }
//
//    @Test
//    public void updateUserTest() throws SQLException{
//        UserDto userDto = new UserDto();
//
//
//        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
//                .thenReturn(preparedStatement);
//        when(preparedStatement.executeUpdate()).thenReturn(1); // Assuming the update is successful
//
//        // Act
//        int result = userDao.updateUser(userDto);
//
//        // Assert
//        assertEquals(0, result);
//    }
//
//}
