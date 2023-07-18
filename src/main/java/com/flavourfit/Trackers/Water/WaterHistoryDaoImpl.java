package com.flavourfit.Trackers.Water;

import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class WaterHistoryDaoImpl implements IWaterHistoryDao {

    private static Logger logger = LoggerFactory.getLogger(com.flavourfit.Trackers.Water.WaterHistoryDaoImpl.class);

    private final IDatabaseManager database;

    private Connection connection;

    @Autowired
    public WaterHistoryDaoImpl(IDatabaseManager database) {
        this.database = database;
        if (this.database != null && this.database.getConnection() != null) {
            this.connection = this.database.getConnection();
        }
    }

    /**
     * Method to add water intake to the given user
     *
     * @param waterHistoryDto -- Water intake to be updated
     * @throws SQLException
     */
    @Override
    public void addWaterIntake(WaterHistoryDto waterHistoryDto) throws SQLException {
        logger.info("Started addWaterIntake() method");

        if (waterHistoryDto == null) {
            logger.error("WaterHistoryDto object not valid!!");
            throw new SQLException("WaterHistoryDto object not valid!!");
        } else {
            if (waterHistoryDto.getWaterIntake() == 0.00d) {
                logger.warn("Water intake count may be invalid!!");
            }

            if (waterHistoryDto.getUpdateDate().isEmpty()) {
                logger.warn("Water intake update date missing!!");
            }
        }

        this.testConnection();

        WaterHistoryDto existingWaterHistoryDto = this.getWaterIntakeByUserIdDate(
                waterHistoryDto.getUpdateDate(), waterHistoryDto.getUserId());
        if (existingWaterHistoryDto == null) {
            logger.info("Creating a prepared statement to insert record.");
            String query = "INSERT INTO Water_History (Water_intake,Update_Date,User_id) "
                    + " VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.info("Replacing values in prepared statement with actual values to be inserted");
            preparedStatement.setDouble(1, waterHistoryDto.getWaterIntake());
            preparedStatement.setString(2, waterHistoryDto.getUpdateDate());
            preparedStatement.setInt(3, waterHistoryDto.getUserId());

            logger.info("Execute the insertion of record to the table");
            preparedStatement.executeUpdate();
        } else {
            logger.info("If water history row exists Execute the update of record to the table");
            double updatedWaterIntake = existingWaterHistoryDto.getWaterIntake() + waterHistoryDto.getWaterIntake();
            existingWaterHistoryDto.setWaterIntake(updatedWaterIntake);
            this.updateWaterHistory(existingWaterHistoryDto);
        }
        logger.info("Added water intake to the Water history table!");
    }

    public void updateWaterHistory(WaterHistoryDto waterHistoryDto) throws SQLException {
        logger.info("Started getWaterIntakeByUserIdDate() method");

        if (waterHistoryDto == null) {
            logger.error("Invalid data while updating water history!!");
            throw new SQLException("Invalid data while updating water history!!");
        }

        this.testConnection();

        logger.info("Creating a prepared statement to insert record.");
        String query = "UPDATE Water_History SET Water_intake=? WHERE Water_history_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        logger.info("Replacing values in prepared statement with actual values to be updated");
        preparedStatement.setDouble(1, waterHistoryDto.getWaterIntake());
        preparedStatement.setInt(2, waterHistoryDto.getWaterHistoryId());

        logger.info("Execute the update of record to the table");
        preparedStatement.executeUpdate();
    }

    @Override
    public WaterHistoryDto getWaterIntakeByUserIdDate(String date, int userId) throws SQLException {
        logger.info("Started getWaterIntakeByUserIdDate() method");

        if (date.isEmpty()) {
            logger.error("Invalid date input while fetching water history!!");
            throw new SQLException("Invalid date input while fetching water history!!");
        }

        this.testConnection();

        WaterHistoryDto waterHistoryDto = null;
        String query = "SELECT * FROM Water_History WHERE Update_Date=? AND User_id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        logger.info("Replacing values in prepared statement with actual values for date and user id.");
        preparedStatement.setString(1, date);
        preparedStatement.setInt(2, userId);

        logger.info("Execute the query to get water intake for date.");
        ResultSet resultSet = preparedStatement.executeQuery();

        logger.info("Iterate result set to get total water intake.");
        waterHistoryDto = this.extractResult(resultSet);

        return waterHistoryDto;
    }

    private WaterHistoryDto extractResult(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            throw new SQLException("Invalid result set!");
        }
        WaterHistoryDto waterHistoryDto = null;
        while (resultSet.next()) {
            int id = resultSet.getInt("Water_history_id");
            double water_intake = resultSet.getDouble("Water_intake");
            String date = resultSet.getString("Update_Date");
            int userId = resultSet.getInt("User_id");

            waterHistoryDto = new WaterHistoryDto(id, water_intake, date, userId);
        }

        return waterHistoryDto;
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
}
