package com.flavourfit.User;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements IUserDao {
    private static Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private final IDatabaseManager database;

    private Connection connection;

    @Autowired
    public UserDaoImpl(DatabaseManagerImpl database) {
        this.database = database;
        if (this.database != null && this.database.getConnection() != null) {
            this.connection = this.database.getConnection();
        }
    }


    /**
     * Method to fetch all users from the database
     *
     * @return -- List of Users received from the database
     * @throws SQLException
     */
    @Override
    public List<UserDto> getAllUsers() throws SQLException {
        logger.info("Started getAllUsers() method");
        List<UserDto> users = new ArrayList<>();

        this.testConnection();

        Statement statement = connection.createStatement();

        logger.info("Running select query to get all users");
        ResultSet resultSet = statement.executeQuery("SELECT * from Users;");
        while (resultSet.next()) {
            UserDto userDto = this.extractUserFromResult(resultSet);
            users.add(userDto);
        }
        logger.info("Received data from db and added users to users list.");

        return users;
    }

    /**
     * Method to fetch a user from database by their User_id
     *
     * @param userId -- int UserId of the user
     * @return -- User received from the
     * @throws SQLException
     */
    @Override
    public UserDto getUserById(int userId) throws SQLException {
        logger.info("Started getUserById() method");
        UserDto user = null;


        this.testConnection();

        logger.info("Running select query to get user by userId");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Users WHERE User_id=?");
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            user = this.extractUserFromResult(resultSet);
        }
        logger.info("Returning received user as response");
        return user;
    }

    @Override
    public PremiumUserDto getUserBymembership(int userId) throws SQLException {
        logger.info("Started getUserById() method");
        if (database != null) {
            PremiumUserDto user = null;
            Connection connection = this.database.getConnection();

            if (connection == null) {
                logger.error("SQL connection not found!");
                throw new SQLException("SQL connection not found!");
            }

            logger.info("Running select query to get premium user by userId");
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM Premium_Memberships WHERE User_id=? ");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user = this.extractPremiumUserFromResult(resultSet);
            }
            logger.info("Returning received user as response");
            return user;
        }
        return null;
    }

//    Updating user info to the table

    @Override
    public int updateUser(UserDto user) throws SQLException {
        int count = 0;
        logger.info("Started updateUser() method");

        if (user == null) {
            logger.error("User object not valid!!");
            throw new SQLException("User object not valid!!");
        }

        this.testConnection();

        logger.info("Creating a prepared statement update the record");
        String query = "Update Users set" +
                " First_name=?, Last_name=?, Phone=?, Email=?, Age=?" +
                ",Street_address=?, City=?,State=?, Zip_code=?, Current_weight=?, Target_weight=?" +
                "where User_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        logger.info("Replacing values in prepared statement with actual values to be inserted");
        this.replaceStatementPlaceholders(user, preparedStatement, 12);
        preparedStatement.setDouble(11, user.getTargetWeight());
        preparedStatement.setInt(12, user.getUserId());
        logger.info("Executing the update user request");
        count = preparedStatement.executeUpdate();
        return count;
    }

    @Override
    public void addUser(UserDto user) throws SQLException {
        logger.info("Started addUser() method");

        if (user == null) {
            logger.error("User object not valid!!");
            throw new SQLException("User object not valid!!");
        }

        this.testConnection();

        logger.info("Creating a prepared statement to insert record.");
        String query = "INSERT INTO Users (First_name, Last_name, Phone, Email, Age, Street_address, "
                + " City, State, Zip_code, Current_weight, Target_weight, Type, Password) "
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        logger.info("Replacing values in prepared statement with actual values to be inserted");
        this.replaceStatementPlaceholders(user, preparedStatement, 13);
        logger.info("Execute the insertion of record to the table");
        preparedStatement.executeUpdate();

        ResultSet keys = preparedStatement.getGeneratedKeys();
        long insertedUserId;
        while (keys.next()) {
            insertedUserId = keys.getLong(1);
            user.setUserId((int) insertedUserId);
            logger.info("Added User with userId: {}, to the Users table!", insertedUserId);
        }
    }

    /**
     * Method to fetch a user from database by their User_id
     *
     * @param email -- String email of the user
     * @return -- User received from the
     * @throws SQLException
     */
    @Override
    public UserDto getUserByEmail(String email) throws SQLException {
        logger.info("Started getUserById() method");

        this.testConnection();

        UserDto user = null;

        logger.info("Running select query to get user by userId");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Users WHERE Email=?");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            user = this.extractUserFromResult(resultSet);
        }
        logger.info("Returning received user as response");
        return user;

    }

    @Override
    public boolean resetUserPassword(int userId, String newPassword) throws SQLException {

        boolean passwordReset = false;

        logger.info("Started resetUserPassword() method");

        if (userId == 0) {
            logger.error("User object not valid!!");
            throw new SQLException("User object not valid!!");
        }

        this.testConnection();

        logger.info("Creating a prepared statement to update record.");
        String query = "UPDATE Users SET Password = ? where User_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        logger.info("Replacing values in prepared statement with actual values to be inserted");
        preparedStatement.setString(1, newPassword);
        preparedStatement.setInt(2, userId);
        logger.info("Execute the update of record to the table");
        int rows = preparedStatement.executeUpdate();

        if (rows > 0){
            logger.info("Updated Password with userId: {}, to the Users table!", userId);
            return true;
        } else {
            logger.warn("Incorrect user id; not found");
            return false;
        }
/**
        ResultSet keys = preparedStatement.getGeneratedKeys();
        long updateUserIdPassword;
        while (keys.next()) {
            updateUserIdPassword = keys.getLong(1);
            logger.info("Updated Password with userId: {}, to the Users table!", updateUserIdPassword);
        }

        return true;
 **/
    }

    @Override
    public int userToPremiumPayment(int userId, PremiumUserPaymentDetailsDto details) throws SQLException {

        int userPremiumID = 0;

        logger.info("Started userUpgradedToPremium() method");

        if (userId == 0) {
            logger.error("User object not valid!!");
            throw new SQLException("User object not valid!!");
        }

        this.testConnection();

        logger.info("Creating a prepared statement to insert record.");
        String query = "INSERT INTO Payments (amount, reason, User_id, Premium_membership_id) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        logger.info("Entering values in prepared statement with actual values to be inserted");
        preparedStatement.setDouble(1, details.getAmount());
        preparedStatement.setString(2, "Premium Membership Payment");
        preparedStatement.setInt(3, userId);
        preparedStatement.setInt(4, 1);
        logger.info("Execute the update of record to the table");
        preparedStatement.executeUpdate();

        ResultSet keys = preparedStatement.getGeneratedKeys();

        while (keys.next()) {
            userPremiumID = keys.getInt(1);
            logger.info("Entered Customer Membership for userId: {}, to the Registered_Customer table!", userPremiumID);
        }

        return userPremiumID;
    }

    @Override
    public int startExtendPremiumMembership(int userId, String startDate, String expiryDate, int paymentID) throws
            SQLException {

        int premiumMembershipID = 0;

        logger.info("Started startExtendPremiumMembership() method");

        if (userId == 0) {
            logger.error("User object not valid!!");
            throw new SQLException("User object not valid!!");
        }

        this.testConnection();

        logger.info("Creating a prepared statement to update record.");
        String query = "INSERT INTO Premium_Memberships (Start_date, Expiry_date, Is_active, User_id) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        logger.info("Entering values in prepared statement with actual values to be inserted");
        preparedStatement.setString(1, startDate);
        preparedStatement.setString(2, expiryDate);
        preparedStatement.setInt(3, 1);
        preparedStatement.setInt(4, userId);
        logger.info("Execute the update of record to the table");
        preparedStatement.executeUpdate();

        ResultSet keys = preparedStatement.getGeneratedKeys();

        while (keys.next()) {
            premiumMembershipID = keys.getInt(1);
            logger.info("Entered the Initialization/Extension of membership for userId: {}, to the Premium_Memberships table!", premiumMembershipID);
        }

        return premiumMembershipID;
    }

    @Override
    public boolean updateUserPayment(int userId, int paymentID, int premiumMembershipID) throws SQLException {

        boolean paymentUpdated = false;

        logger.info("Started startExtendPremiumMembership() method");

        if (userId == 0) {
            logger.error("User object not valid!!");
            throw new SQLException("User object not valid!!");
        }

        this.testConnection();

        logger.info("Creating a prepared statement to update record.");
        String query = "UPDATE Payments SET Premium_membership_id = ? where Payment_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        logger.info("Entering values in prepared statement with actual values to be inserted");
        preparedStatement.setInt(1, premiumMembershipID);
        preparedStatement.setInt(2, paymentID);
        logger.info("Execute the update of record to the table");
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0){
            paymentUpdated = true;
            logger.info("Updated the ID for premium membership for paymentID: {}, to the Payments table!", paymentID);
        } else {
            paymentUpdated = false;
        }
/**
        ResultSet keys = preparedStatement.getGeneratedKeys();
        long recheckPaymentID =paymentID;
        while (keys.next()) {
            recheckPaymentID = keys.getInt(1);
            logger.info("Updated the ID for premium membership for paymentID: {}, to the Payments table!", recheckPaymentID);
        }
 **/
        return paymentUpdated;
    }

    @Override
    public void clearGuestPassword(String email) throws UserNotFoundException {
        logger.info("Entered clearGuestPassword() method");

        if (email == null || email.isEmpty()) {
            logger.error("Invalid email {} while clearing password", email);
            throw new UserNotFoundException("Invalid email " + email);
        }

        try {
            this.testConnection();

            logger.info("Get user for email {}", email);
            UserDto userDto = this.getUserByEmail(email);
            String query = "UPDATE Users SET Password = ? where User_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "");
            preparedStatement.setInt(2, userDto.getUserId());

            logger.info("Executing query to clear password");
            preparedStatement.executeUpdate();
            logger.info("Successfully cleared password for {}", email);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new UserNotFoundException(e);
        }
    }

    @Override
    public void updateUserWeight(double weight, int userId) throws SQLException {
        logger.info("Entered updateUserWeight() method");

        if (userId == 0) {
            logger.error("Invalid user ");
            throw new UserNotFoundException("Invalid user");
        }

        this.testConnection();

        String query = "UPDATE Users SET Current_weight=? WHERE User_id=?";
        logger.info("Creating query to update weight");
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setDouble(1, weight);
        preparedStatement.setInt(2, userId);
        preparedStatement.executeUpdate();
        logger.info("Updated weight of user with id {}", userId);
    }

    @Override
    public double getUserCurrentWeight(int userId) throws SQLException {
        logger.info("Entered getUserCurrentWeight() method");

        if (userId == 0) {
            logger.error("Invalid user ");
            throw new UserNotFoundException("Invalid user");
        }

        this.testConnection();

        String query = "SELECT Current_weight FROM Users WHERE User_id=?";
        logger.info("Creating query to fetch current weight");
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        double currentWeight = 0.0d;
        while (resultSet.next()) {
            currentWeight = resultSet.getDouble("Current_weight");
        }
        logger.info("Fetched weight of user with id {}", userId);
        return currentWeight;
    }

    private void replaceStatementPlaceholders(UserDto user, PreparedStatement preparedStatement, int count) throws
            SQLException {
        if (user == null || preparedStatement == null) {
            return;
        }

        preparedStatement.setString(1, user.getFirstName());
        preparedStatement.setString(2, user.getLastName());
        preparedStatement.setString(3, user.getPhone());
        preparedStatement.setString(4, user.getEmail());
        preparedStatement.setInt(5, user.getAge());
        preparedStatement.setString(6, user.getStreetAddress());
        preparedStatement.setString(7, user.getCity());
        preparedStatement.setString(8, user.getState());
        preparedStatement.setString(9, user.getZipCode());
        preparedStatement.setDouble(10, user.getCurrentWeight());
        preparedStatement.setDouble(11, user.getTargetWeight());
        preparedStatement.setString(12, user.getType());
        if (count > 12) {
            preparedStatement.setString(13, user.getPassword());
        }

    }

    private UserDto extractUserFromResult(ResultSet resultSet) throws SQLException {
        UserDto user = new UserDto();
        if (resultSet != null) {
            user.setUserId(resultSet.getInt("User_id"));
            user.setFirstName(resultSet.getString("First_name"));
            user.setLastName(resultSet.getString("Last_name"));
            user.setPhone(resultSet.getString("Phone"));
            user.setEmail(resultSet.getString("Email"));
            user.setAge(resultSet.getInt("Age"));
            user.setStreetAddress(resultSet.getString("Street_address"));
            user.setCity(resultSet.getString("City"));
            user.setState(resultSet.getString("State"));
            user.setZipCode(resultSet.getString("Zip_code"));
            user.setCurrentWeight(resultSet.getDouble("Current_weight"));
            user.setTargetWeight(resultSet.getDouble("Target_Weight"));
            user.setType(resultSet.getString("Type"));
            user.setPassword(resultSet.getString("Password"));
        }
        return user;
    }

    private void testConnection() throws SQLException {
        if (database == null && connection == null) {
            logger.error("SQL connection not found!");
            throw new SQLException("SQL connection not found!");
        }


        if (connection == null && this.database.getConnection() == null) {
            logger.error("SQL connection not found!");
            throw new SQLException("SQL connection not found!");
        } else {
            this.connection = this.database.getConnection();
        }
    }

    private PremiumUserDto extractPremiumUserFromResult(ResultSet resultSet) throws SQLException {
        PremiumUserDto user = new PremiumUserDto();
        if (resultSet != null) {
            user.setUserId(resultSet.getInt("User_id"));
            user.setMembership_ID(resultSet.getInt("Premium_membership_id"));
            user.setStart_date(resultSet.getString("Start_date"));
            user.setExpiry_date(resultSet.getString("Expiry_date"));
            user.setIs_active(resultSet.getInt("Is_active"));
        }
        return user;
    }
}
