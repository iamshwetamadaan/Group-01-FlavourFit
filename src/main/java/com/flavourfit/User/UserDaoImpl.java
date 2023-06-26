package com.flavourfit.User;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements IUserDao {
    private static Logger logger = LoggerFactory.getLogger(DatabaseManagerImpl.class);
    IDatabaseManager database;

    public UserDaoImpl(IDatabaseManager database) {
        this.database = database;
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

        if (database != null) {
            Connection connection = this.database.getConnection();

            if (connection == null) {
                logger.error("SQL connection not found!");
                throw new SQLException("SQL connection not found!");
            }

            Statement statement = connection.createStatement();

            logger.info("Running select query to get all users");
            ResultSet resultSet = statement.executeQuery("SELECT * from Users;");
            while (resultSet.next()) {
                UserDto userDto = this.extractUserFromResult(resultSet);
                users.add(userDto);
            }
            logger.info("Received data from db and added users to users list.");
        }

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
        if (database != null) {
            UserDto user = null;
            Connection connection = this.database.getConnection();

            if (connection == null) {
                logger.error("SQL connection not found!");
                throw new SQLException("SQL connection not found!");
            }

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
        return null;
    }

    @Override
    public void addUser(UserDto user) throws SQLException {
        logger.info("Started getUserById() method");

        if (user == null) {
            logger.error("User object not valid!!");
            throw new SQLException("User object not valid!!");
        }

        if (database != null) {
            Connection connection = this.database.getConnection();

            if (connection == null) {
                logger.error("SQL connection not found!");
                throw new SQLException("SQL connection not found!");
            }

            logger.info("Creating a prepared statement to insert record.");
            String query = "INSERT INTO Users (First_name, Last_name, Phone, Email, Age, Street_address, "
                    + " City, State, Zip_code, Current_weight, Target_weight, Type) "
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.info("Replacing values in prepared statement with actual values to be inserted");
            this.replaceStatementPlaceholders(user, preparedStatement);
            logger.info("Execute the insertion of record to the table");
            preparedStatement.executeUpdate();

            ResultSet keys = preparedStatement.getGeneratedKeys();
            long insertedUserId;
            while (keys.next()) {
                insertedUserId = keys.getLong(1);
                logger.info("Added User {} to the Users table!", insertedUserId);
            }
        }
    }

    private void replaceStatementPlaceholders(UserDto user, PreparedStatement preparedStatement) throws SQLException {
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
        }
        return user;
    }
}
