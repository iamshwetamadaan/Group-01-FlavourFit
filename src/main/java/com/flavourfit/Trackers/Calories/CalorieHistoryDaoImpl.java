package com.flavourfit.Trackers.Calories;

import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class CalorieHistoryDaoImpl implements ICalorieHistoryDao {
    private static Logger logger = LoggerFactory.getLogger(CalorieHistoryDaoImpl.class);

    private final IDatabaseManager database;

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

            logger.info("Added Calories to the Calorie history table!");
        }
    }

    /**
     * Method to get calorie count by date
     *
     * @param date   -- String date at which calorie intake to be checked
     * @param userId -- Int userId of the user
     * @return -- Double calorie count for the given date
     * @throws SQLException
     */
    @Override
    public double getCalorieByDate(String date, int userId) throws SQLException {
        logger.info("Started getCalorieByDate() method");

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


            double calorieIntake = 0.0d;
            logger.info("Creating a prepared statement to insert record.");
            String query = "SELECT Calorie_Count FROM Calorie_History WHERE Update_Date=? AND User_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.info("Replacing values in prepared statement with actual values for date and user id.");
            preparedStatement.setString(1, date);
            preparedStatement.setInt(2, userId);

            logger.info("Execute the query to get calorie count for date.");
            ResultSet resultSet = preparedStatement.executeQuery();

            logger.info("Iterate result set to get total calorie count.");
            while (resultSet.next()) {
                calorieIntake += resultSet.getDouble("Calorie_Count");
            }
            return calorieIntake;
        }
    }
}
