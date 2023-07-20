package com.flavourfit.User;

import com.flavourfit.DatabaseManager.IDatabaseManager;
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
    public UserDaoImpl(IDatabaseManager database) {
        this.database = database;
        this.connection = database.getConnection();
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
                    "SELECT * FROM Users U inner join Premium_Memberships PM on U.User_id =  PM.User_id WHERE U.User_id=? and PM.Is_active = 1  ");
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
        String query = "Update Users set First_name=?, Last_name=?, Phone=?, Email=?, Age=?" +
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

        if (database != null) {
            Connection connection = this.database.getConnection();

            if (connection == null) {
                logger.error("SQL connection not found!");
                throw new SQLException("SQL connection not found!");
            }

            logger.info("Creating a prepared statement to update record.");
            String query = "UPDATE Users SET Password = ? where User_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            logger.info("Replacing values in prepared statement with actual values to be inserted");
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userId);
            logger.info("Execute the update of record to the table");
            preparedStatement.executeUpdate();

            ResultSet keys = preparedStatement.getGeneratedKeys();
            long updateUserIdPassword;
            while (keys.next()) {
                updateUserIdPassword = keys.getLong(1);
                logger.info("Updated Password with userId: {}, to the Users table!", updateUserIdPassword);
            }
            passwordReset = true;
        }

        return passwordReset;
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
            user.setMembership_ID(resultSet.getInt("Premium_membership_id"));
            user.setStart_date(resultSet.getString("Start_date"));
            user.setExpiry_date(resultSet.getString("Expiry_date"));
            user.setIs_active(resultSet.getInt("Is_active"));
        }
        return user;
    }
}
