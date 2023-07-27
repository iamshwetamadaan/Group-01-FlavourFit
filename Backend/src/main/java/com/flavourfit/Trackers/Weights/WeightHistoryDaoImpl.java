package com.flavourfit.Trackers.Weights;

import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
    public class WeightHistoryDaoImpl implements IWeightHistoryDao {

        private static Logger logger = LoggerFactory.getLogger(com.flavourfit.Trackers.Weights.IWeightHistoryDao.class);

        private final IDatabaseManager database;

        private Connection connection;

        @Autowired
        public  WeightHistoryDaoImpl(IDatabaseManager database) {
            this.database = database;
            if (this.database != null && this.database.getConnection() != null) {
                this.connection = this.database.getConnection();
            }
        }

        /**
         * Method to add water intake to the given user
         *
         * @param weightHistoryDto -- Water intake to be updated
         * @throws SQLException
         */
        @Override
        public void addWeight(WeightHistoryDto weightHistoryDto) throws SQLException {
            logger.info("Started addWeight() method");

            if (weightHistoryDto == null) {
                logger.error("WeightHistoryDto object not valid!!");
                throw new SQLException("WeightHistoryDto object not valid!!");
            } else {
                if (weightHistoryDto.getWeight() == 0.00d) {
                    logger.warn("Weight may be invalid!!");
                }

                if (weightHistoryDto.getUpdateDate().isEmpty()) {
                    logger.warn("Weight update date missing!!");
                }
            }

            this.testConnection();

            WeightHistoryDto existingWeightHistoryDto = this.getWeightByUserIdDate(
                    weightHistoryDto.getUpdateDate(), weightHistoryDto.getUserId());
            if (existingWeightHistoryDto == null) {
                logger.info("Creating a prepared statement to insert record.");
                String query = "INSERT INTO Weight_History (weight,Update_Date,User_id) "
                        + " VALUES(?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                logger.info("Replacing values in prepared statement with actual values to be inserted");
                preparedStatement.setDouble(1, weightHistoryDto.getWeight());
                preparedStatement.setString(2, weightHistoryDto.getUpdateDate());
                preparedStatement.setInt(3, weightHistoryDto.getUserId());

                logger.info("Execute the insertion of record to the table");
                preparedStatement.executeUpdate();
            } else {
                logger.info("If water history row exists Execute the update of record to the table");
                double updatedWeight = existingWeightHistoryDto.getWeight() + weightHistoryDto.getWeight();
                existingWeightHistoryDto.setWeight(updatedWeight);
                this.updateWeightHistory(existingWeightHistoryDto);
            }
            logger.info("Added water intake to the Water history table!");
        }

        public void updateWeightHistory(WeightHistoryDto weightHistoryDto) throws SQLException {
            logger.info("Started getWaterIntakeByUserIdDate() method");

            if (weightHistoryDto == null) {
                logger.error("Invalid data while updating water history!!");
                throw new SQLException("Invalid data while updating water history!!");
            }

            this.testConnection();

            logger.info("Creating a prepared statement to insert record.");
            String query = "UPDATE Weight_History SET weight=? WHERE weight_history_id=?";
           // UserService.updateUserWeight(weightHistoryDto.getWeight());
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.info("Replacing values in prepared statement with actual values to be updated");
            preparedStatement.setDouble(1, weightHistoryDto.getWeight());
            preparedStatement.setInt(2, weightHistoryDto.getWeightHistoryId());

            logger.info("Execute the update of record to the table");
            preparedStatement.executeUpdate();
        }

        @Override
        public WeightHistoryDto getWeightByUserIdDate(String date, int userId) throws SQLException {
            logger.info("Started getWaterIntakeByUserIdDate() method");

            if (date.isEmpty()) {
                logger.error("Invalid date input while fetching water history!!");
                throw new SQLException("Invalid date input while fetching water history!!");
            }

            this.testConnection();

            WeightHistoryDto weightHistoryDto = null;
            String query = "SELECT * FROM Weight_History WHERE Update_Date=? AND User_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.info("Replacing values in prepared statement with actual values for date and user id.");
            preparedStatement.setString(1, date);
            preparedStatement.setInt(2, userId);

            logger.info("Execute the query to get weight for date.");
            ResultSet resultSet = preparedStatement.executeQuery();

            logger.info("Iterate result set to get weight.");
            weightHistoryDto = this.extractResult(resultSet);

            return weightHistoryDto;
        }


    @Override
    public List<WeightHistoryDto> getWeightHistoryByPeriod(String startDate, String endDate, int userId) throws
                                                                                                       SQLException {
        logger.info("Started getWeightHistoryByPeriod() method");

        if (startDate == null || startDate.isEmpty()) {
            logger.error("Invalid start date.");
            throw new SQLException("Invalid start date.");
        }

        if (endDate == null || endDate.isEmpty()) {
            logger.error("Invalid start date.");
            throw new SQLException("Invalid end date.");
        }

        this.testConnection();

        WeightHistoryDto weightHistoryDto = null;
        String query = "SELECT * FROM Weight_History WHERE User_id=? AND Update_Date Between ? AND ? ORDER BY weight_history_id DESC";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        logger.info("Replacing values in prepared statement with actual values for date and user id.");
        preparedStatement.setInt(1, userId);
        preparedStatement.setString(2, startDate);
        preparedStatement.setString(3, endDate);

        logger.info("Execute the query to get weight for date.");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<WeightHistoryDto> weightHistoryList = this.extractResultList(resultSet);

        return weightHistoryList;
    }

    @Override
    public WeightHistoryDto getWeightByUserIdCurrent(int userId) throws SQLException {
        logger.info("Started getWaterIntakeByUserIdDate() method");


        this.testConnection();

        WeightHistoryDto weightHistoryDto = null;
        String query = "SELECT * FROM Weight_History WHERE   User_id=? ORDER BY Update_Date DESC LIMIT 1";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        logger.info("Replacing values in prepared statement with actual values for date and user id.");
        preparedStatement.setInt(1, userId);

        logger.info("Execute the query to get WEIGHT current.");
        ResultSet resultSet = preparedStatement.executeQuery();

        logger.info("Iterate result set to get WEIGHT current.");
        weightHistoryDto = this.extractResult(resultSet);

        return weightHistoryDto;
    }



    private WeightHistoryDto extractResult(ResultSet resultSet) throws SQLException {
            if (resultSet == null) {
                throw new SQLException("Invalid result set!");
            }
            WeightHistoryDto waterHistoryDto = null;
            while (resultSet.next()) {
                int id = resultSet.getInt("Water_history_id");
                double water_intake = resultSet.getDouble("Water_intake");
                String date = resultSet.getString("Update_Date");
                int userId = resultSet.getInt("User_id");

                waterHistoryDto = new WeightHistoryDto(id, water_intake, date, userId);
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

    /**
     * Method to extract list of weightHistory from result set
     *
     * @param resultSet -- Result set to be iterated
     * @return -- List of calorieHistory objects found from result
     * @throws SQLException
     */
    private List<WeightHistoryDto> extractResultList(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            throw new SQLException("Invalid result set!");
        }
        List<WeightHistoryDto> weightHistoryList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("weight_history_id");
            double waterintake = resultSet.getDouble("weight");
            String date = resultSet.getString("Update_Date");
            int userId = resultSet.getInt("User_id");

            WeightHistoryDto weightHistoryDto = new WeightHistoryDto(id, waterintake, date, userId);
            weightHistoryList.add(weightHistoryDto);
        }

        return weightHistoryList;
    }

}


