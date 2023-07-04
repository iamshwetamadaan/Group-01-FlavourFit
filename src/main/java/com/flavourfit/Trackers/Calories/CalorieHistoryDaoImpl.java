package com.flavourfit.Trackers.Calories;

import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class CalorieHistoryDaoImpl implements ICalorieHistoryDao {
    private static Logger logger = LoggerFactory.getLogger(CalorieHistoryDaoImpl.class);

    private final IDatabaseManager database;

    @Autowired
    public CalorieHistoryDaoImpl(IDatabaseManager database) {
        this.database = database;
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
            if (calorieHistoryDto.getCalorieCount() == 0.00d) {
                logger.warn("Calorie count may be invalid!!");
            }

            if (calorieHistoryDto.getUpdateDate().isEmpty()) {
                logger.warn("Calorie update date missing!!");
            }
        }

        if (database == null) {
            logger.error("No database object found!!");
            throw new SQLException("No database object found!!");
        } else {

            Connection connection = this.database.getConnection();

            if (connection == null) {
                logger.error("SQL connection not found!");
                throw new SQLException("SQL connection not found!");
            }

            CalorieHistoryDto existingCalorieHistoryDto = this.getCalorieByUserIdDate(calorieHistoryDto.getUpdateDate(), calorieHistoryDto.getUserId());
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
    }

    public void updateCalorieHistory(CalorieHistoryDto calorieHistoryDto) throws SQLException {
        logger.info("Started getCalorieByUserIdDate() method");

        if (calorieHistoryDto == null) {
            logger.error("Invalid data while updating calorie history!!");
            throw new SQLException("Invalid data while updating calorie history!!");
        }

        if (database == null) {
            logger.error("No database object found!!");
            throw new SQLException("No database object found!!");
        } else {

            Connection connection = this.database.getConnection();

            if (connection == null) {
                logger.error("SQL connection not found!");
                throw new SQLException("SQL connection not found!");
            }

            logger.info("Creating a prepared statement to insert record.");
            String query = "UPDATE Calorie_History SET Calorie_Count=? WHERE Calorie_history_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.info("Replacing values in prepared statement with actual values to be updated");
            preparedStatement.setDouble(1, calorieHistoryDto.getCalorieCount());
            preparedStatement.setInt(2, calorieHistoryDto.getCalorieHistoryId());

            logger.info("Execute the update of record to the table");
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public CalorieHistoryDto getCalorieByUserIdDate(String date, int userId) throws SQLException {
        logger.info("Started getCalorieByUserIdDate() method");

        if (date.isEmpty()) {
            logger.error("Invalid date input while fetching calorie history!!");
            throw new SQLException("Invalid date input while fetching calorie history!!");
        }

        if (database == null) {
            logger.error("No database object found!!");
            throw new SQLException("No database object found!!");
        } else {

            Connection connection = this.database.getConnection();

            if (connection == null) {
                logger.error("SQL connection not found!");
                throw new SQLException("SQL connection not found!");
            }

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

    }

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
}
