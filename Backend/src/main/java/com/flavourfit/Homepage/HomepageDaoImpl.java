package com.flavourfit.Homepage;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Homepage.DTO.FitnessStreakDTO;
import com.flavourfit.Homepage.DTO.RoutineDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HomepageDaoImpl implements IHomepageDao {

    private static Logger logger = LoggerFactory.getLogger(HomepageDaoImpl.class);

    private final IDatabaseManager database;

    private Connection connection;

    @Autowired
    public HomepageDaoImpl(DatabaseManagerImpl database) {
        this.database = database;
        if (this.database != null && this.database.getConnection() != null) {
            this.connection = this.database.getConnection();
        }
    }

    @Override
    public List<HomepageEventDto> getEventList() throws
            SQLException {

        logger.info("Started getEventList() method");
        this.testConnection();
        String query = "SELECT * FROM Events";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        logger.info("Execute the query to get event list.");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<HomepageEventDto> eventList = this.extractResultListfromEvents(resultSet);
        return eventList;
    }

    @Override
    public List<RoutineDTO> getRoutinesByUser(int userId) throws SQLException {
        logger.info("Started getroutinesByUser method()");
        this.testConnection();

        String query = "SELECT * FROM Fitness_Routines WHERE User_id=? ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        logger.info("Execute the query to get event list.");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<RoutineDTO> routines = extractResultListRoutines(resultSet);

        return routines;

    }


    @Override
    public String getQuoteOfTheDay() throws SQLException {
        logger.info("Started getQuoteOfTheDay() method");
        this.testConnection();
        String quoteOfTheDay = "";
        String query = "SELECT quote_of_the_day, tip_id FROM Fitness_Tips order by tip_id desc limit 1 ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        logger.info("Execute the query to get event list.");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            quoteOfTheDay = resultSet.getString("quote_of_the_day");
        }

        return quoteOfTheDay;
    }

    @Override
    public FitnessStreakDTO getFitnessStreak(int userId) throws SQLException {
        logger.info("Started getFitnessStreak() method");
        this.testConnection();

        FitnessStreakDTO fitnessStreak = new FitnessStreakDTO();

        logger.info("Fetching calorie streak for user {}", userId);
        FitnessStreakDTO calorieStreak = this.getCalorieStreak(userId);

        logger.info("Fetching water streak for user {}", userId);
        FitnessStreakDTO waterStreak = this.getWaterStreak(userId);

        fitnessStreak.setAvgCalorie(calorieStreak.getAvgCalorie());
        fitnessStreak.setAvgWaterIntake(waterStreak.getAvgWaterIntake());

        if (calorieStreak.getStreak() > waterStreak.getStreak()) {
            fitnessStreak.setStreak(calorieStreak.getStreak());
        } else {
            fitnessStreak.setStreak(waterStreak.getStreak());
        }

        logger.info("Successfully fetched fitness streak.");
        return fitnessStreak;
    }

    private FitnessStreakDTO getCalorieStreak(int userId) throws SQLException {
        logger.info("Entered method getCalorieStreak()");

        logger.info("Executing custom stored procedure to get calorie streak");
        CallableStatement stmt = this.connection.prepareCall("{CALL iterateCalorieDates(?, @calories, @rowCount)}");
        stmt.setInt(1, userId);
        stmt.execute();


        logger.info("Fetching values from the result of the stored procedure");
        double avgCalories = 0.0d;
        int streak = 0;
        PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT @calories, @rowCount");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            avgCalories = resultSet.getDouble(1);
            streak = resultSet.getInt(2);
        }

        if (streak > 0) {
            avgCalories = avgCalories / streak;
        }

        logger.info("Successfully fetched calorie streak");
        FitnessStreakDTO fitnessStreak = new FitnessStreakDTO();
        fitnessStreak.setStreak(streak);
        fitnessStreak.setAvgCalorie(avgCalories);

        return fitnessStreak;
    }

    private FitnessStreakDTO getWaterStreak(int userId) throws SQLException {
        logger.info("Entered method getWaterStreak()");

        logger.info("Executing custom stored procedure to get calorie streak");
        CallableStatement stmt = this.connection.prepareCall("{CALL iterateWaterDates(?, @water, @rowCount)}");
        stmt.setInt(1, userId);
        stmt.execute();


        logger.info("Fetching values from the result of the stored procedure");
        double avgWater = 0.0d;
        int streak = 0;
        PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT @water, @rowCount");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            avgWater = resultSet.getDouble(1);
            streak = resultSet.getInt(2);
        }

        if (streak > 0) {
            avgWater = avgWater / streak;
        }

        logger.info("Successfully fetched calorie streak");
        FitnessStreakDTO fitnessStreak = new FitnessStreakDTO();
        fitnessStreak.setStreak(streak);
        fitnessStreak.setAvgWaterIntake(avgWater);

        return fitnessStreak;
    }

    private List<RoutineDTO> extractResultListRoutines(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            throw new SQLException("Invalid result set!");
        }
        List<RoutineDTO> routines = new ArrayList<RoutineDTO>();
        while (resultSet.next()) {
            RoutineDTO routine = new RoutineDTO();
            routine.setRoutineName(resultSet.getString("routine_name"));
            routine.setRoutineDescription(resultSet.getString("routine_description"));
            routine.setTips(resultSet.getString("tips"));
            routines.add(routine);

        }
        return routines;

    }

    private List<HomepageEventDto> extractResultListfromEvents(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            throw new SQLException("Invalid result set!");
        }
        List<HomepageEventDto> eventList = new ArrayList<>();
        while (resultSet.next()) {
            int event_id = resultSet.getInt("Event_id");
            String event_name = resultSet.getString("Event_name");
            String start_date = resultSet.getString("Start_date");
            String end_date = resultSet.getString("End_date");
            String capacity = resultSet.getString("Capacity");
            String host_Name = resultSet.getString("Host_Name");
            String event_description = resultSet.getString("Event_description");

            HomepageEventDto homepageEventDto = new HomepageEventDto(event_id, event_name, start_date, end_date, capacity, host_Name, event_description);
            eventList.add(homepageEventDto);
        }

        return eventList;
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
