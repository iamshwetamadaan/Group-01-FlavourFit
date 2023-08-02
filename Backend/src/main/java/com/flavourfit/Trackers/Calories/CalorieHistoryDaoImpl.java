package com.flavourfit.Trackers.Calories;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Helpers.DateHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CalorieHistoryDaoImpl implements ICalorieHistoryDao {
    private static Logger logger = LoggerFactory.getLogger(CalorieHistoryDaoImpl.class);

    private final IDatabaseManager database;

    private Connection connection;

    @Autowired
    public CalorieHistoryDaoImpl(DatabaseManagerImpl database) {
        this.database = database;
        if (this.database != null && this.database.getConnection() != null) {
            this.connection = this.database.getConnection();
        }
    }

    /**
     * Method to add calorie count to the given
     *
     * @param calorieHistoryDto -- Calories to be updated
     * @throws SQLException
     */
    @Override
    public void addCalorieCount(CalorieHistoryDto calorieHistoryDto) throws SQLException {
        logger.info("Started addCalorieCount() method");

        if (calorieHistoryDto == null) {
            logger.error("CalorieHistoryDto object not valid!!");
            throw new SQLException("CalorieHistoryDto object not valid!!");
        } else {
            if (calorieHistoryDto.getCalorieCount() == 0.0d) {
                logger.warn("Calorie count may be invalid!!");
            }

            if (calorieHistoryDto.getUpdateDate().isEmpty()) {
                logger.warn("Calorie update date missing!!");
                throw new SQLException("Date not valid!!");
            }
        }

        this.testConnection();

        CalorieHistoryDto existingCalorieHistoryDto = this.getCalorieByUserIdDate(
                calorieHistoryDto.getUpdateDate(), calorieHistoryDto.getUserId());
        if (existingCalorieHistoryDto == null) {
            logger.info("Creating a prepared statement to insert record.");
            String query = "INSERT INTO Calorie_History (Calorie_Count,Update_Date,User_id) "
                    + " VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.info("Replacing values in prepared statement with actual values to be inserted");
            preparedStatement.setDouble(1, calorieHistoryDto.getCalorieCount());
            preparedStatement.setString(2, calorieHistoryDto.getUpdateDate());
            preparedStatement.setInt(3, calorieHistoryDto.getUserId());

            logger.info("Execute the insertion of record to the table");
            preparedStatement.executeUpdate();
        } else {
            logger.info("If calorie history row exists Execute the update of record to the table");
            double updatedCalories = existingCalorieHistoryDto.getCalorieCount() + calorieHistoryDto.getCalorieCount();
            existingCalorieHistoryDto.setCalorieCount(updatedCalories);
            this.updateCalorieHistory(existingCalorieHistoryDto);
        }
        logger.info("Added Calories to the Calorie history table!");

    }

    /**
     * Method to update given calorie hitory
     *
     * @param calorieHistoryDto -- CalorieHistoryDto to be updated in database
     * @throws SQLException
     */
    public void updateCalorieHistory(CalorieHistoryDto calorieHistoryDto) throws SQLException {
        logger.info("Started getCalorieByUserIdDate() method");

        if (calorieHistoryDto == null) {
            logger.error("Invalid data while updating calorie history!!");
            throw new SQLException("Invalid data while updating calorie history!!");
        }

        if (calorieHistoryDto.getUpdateDate() == null || calorieHistoryDto.getUpdateDate().isEmpty()) {
            logger.error("Invalid update date while updating calorie history!!");
            throw new SQLException("Invalid update date while updating calorie history!!");
        }

        this.testConnection();

        logger.info("Creating a prepared statement to insert record.");
        String query = "UPDATE Calorie_History SET Calorie_Count=? WHERE Calorie_history_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        logger.info("Replacing values in prepared statement with actual values to be updated");
        preparedStatement.setDouble(1, calorieHistoryDto.getCalorieCount());
        preparedStatement.setInt(2, calorieHistoryDto.getCalorieHistoryId());

        logger.info("Execute the update of record to the table");
        preparedStatement.executeUpdate();

    }

    /**
     * Method to get calories for user by date
     *
     * @param date   -- Date at which calorie count to be found
     * @param userId -- ID of the user
     * @return -- calorie data
     * @throws SQLException
     */
    @Override
    public CalorieHistoryDto getCalorieByUserIdDate(String date, int userId) throws SQLException {
        logger.info("Started getCalorieByUserIdDate() method");

        if (date == null || date.isEmpty()) {
            logger.error("Invalid date input while fetching calorie history!!");
            throw new SQLException("Invalid date input while fetching calorie history!!");
        }

        this.testConnection();

        CalorieHistoryDto calorieHistoryDto = null;
        String query = "SELECT * FROM Calorie_History WHERE Update_Date=? AND User_id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        logger.info("Replacing values in prepared statement with actual values for date and user id.");
        preparedStatement.setString(1, date);
        preparedStatement.setInt(2, userId);

        logger.info("Execute the query to get calorie count for date.");
        ResultSet resultSet = preparedStatement.executeQuery();

        logger.info("Iterate result set to get total calorie count.");
        calorieHistoryDto = this.extractResult(resultSet);

        return calorieHistoryDto;
    }

    /**
     * Method to get the calorieHistory for a period
     *
     * @param startDate -- Starting date of the period
     * @param endDate   -- Ending date of the period
     * @param userId    -- ID of the user
     * @return -- List of calorie history objects found
     * @throws SQLException
     */
    @Override
    public List<CalorieHistoryDto> getCalorieHistoryByPeriod(String startDate, String endDate, int userId) throws
                                                                                                          SQLException {
        logger.info("Started getCalorieByUserIdDate() method");

        if (startDate == null || startDate.isEmpty()) {
            logger.error("Invalid start date.");
            throw new SQLException("Invalid start date.");
        }

        if (endDate == null || endDate.isEmpty()) {
            logger.error("Invalid start date.");
            throw new SQLException("Invalid end date.");
        }

        this.testConnection();

        CalorieHistoryDto calorieHistoryDto = null;
        String query = "SELECT * FROM Calorie_History WHERE User_id=? AND Update_Date Between ? AND ? ORDER BY Calorie_history_id DESC";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        logger.info("Replacing values in prepared statement with actual values for date and user id.");
        preparedStatement.setInt(1, userId);
        preparedStatement.setString(2, startDate);
        preparedStatement.setString(3, endDate);

        logger.info("Execute the query to get calorie count for date.");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<CalorieHistoryDto> calorieHistoryList = this.extractResultList(resultSet);

        return calorieHistoryList;
    }

    @Override
    public CalorieHistoryDto getCaloriesByUserIdCurrent(int userId) throws SQLException {
        logger.info("Started getCaloriesByUserIdCurrent() method");

        this.testConnection();

        CalorieHistoryDto calorieHistoryDto = null;
        String query = "SELECT * FROM Calorie_History WHERE User_id=? AND Update_Date=? ORDER BY Update_Date DESC LIMIT 1";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        logger.info("Replacing values in prepared statement with actual values for date and user id.");
        preparedStatement.setInt(1, userId);
        preparedStatement.setString(2, DateHelpers.getCurrentDateString());

        logger.info("Execute the query to get calories curent.");
        ResultSet resultSet = preparedStatement.executeQuery();

        logger.info("Iterate result set to get total water intake.");
        calorieHistoryDto = this.extractResult(resultSet);

        return calorieHistoryDto;
    }

    /**
     * Method to extract 1 calorieHistory from result set
     *
     * @param resultSet -- Result set to be iterated
     * @return -- calorieHistory object found from result
     * @throws SQLException
     */
    private CalorieHistoryDto extractResult(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            throw new SQLException("Invalid result set!");
        }
        CalorieHistoryDto calorieHistoryDto = null;
        while (resultSet.next()) {
            int id = resultSet.getInt("Calorie_history_id");
            double calories = resultSet.getDouble("Calorie_Count");
            String date = resultSet.getString("Update_Date");
            int userId = resultSet.getInt("User_id");

            calorieHistoryDto = new CalorieHistoryDto(id, calories, date, userId);
        }

        return calorieHistoryDto;
    }

    /**
     * Method to extract list of calorieHistory from result set
     *
     * @param resultSet -- Result set to be iterated
     * @return -- List of calorieHistory objects found from result
     * @throws SQLException
     */
    private List<CalorieHistoryDto> extractResultList(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            throw new SQLException("Invalid result set!");
        }
        List<CalorieHistoryDto> calorieHistoryList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("Calorie_history_id");
            double calories = resultSet.getDouble("Calorie_Count");
            String date = resultSet.getString("Update_Date");
            int userId = resultSet.getInt("User_id");

            CalorieHistoryDto calorieHistoryDto = new CalorieHistoryDto(id, calories, date, userId);
            calorieHistoryList.add(calorieHistoryDto);
        }

        return calorieHistoryList;
    }

    /**
     * Method to check the database connection
     *
     * @throws SQLException
     */
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
}
