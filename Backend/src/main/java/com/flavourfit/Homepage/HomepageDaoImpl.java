package com.flavourfit.Homepage;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Homepage.DTO.RoutineDTO;
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
public class HomepageDaoImpl implements IHomepageDao {

    private static Logger logger = LoggerFactory.getLogger(HomepageDaoImpl.class);

    private final IDatabaseManager database;

    private Connection connection;

    @Autowired
    public HomepageDaoImpl() {
        this.database = DatabaseManagerImpl.getInstance();
        if (this.database != null && this.database.getConnection() != null) {
            this.connection = this.database.getConnection();
        }
    }

    @Override
    public List<HomepageEventDto> getEventList() throws
            SQLException {
        logger.info("Started getEventList() method");


        this.testConnection();

        HomepageEventDto homepageEventDto = null;
        String query = "SELECT * FROM Weight_History WHERE User_id=? AND Update_Date Between ? AND ? ORDER BY weight_history_id DESC";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        // logger.info("Replacing values in prepared statement with actual values for date and user id.");
        // preparedStatement.setInt(1, userId);
        // preparedStatement.setString(2, startDate);
        // preparedStatement.setString(3, endDate);

        logger.info("Execute the query to get event list.");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<HomepageEventDto> eventList = this.extractResultList(resultSet);

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

        List<RoutineDTO> routines = this.extractResultListRoutines(resultSet);

        return routines;

    }


    @Override
    public String getQuoteOfTheDay() throws SQLException {
        logger.info("Started getQuoteOfTheDay() method");
        this.testConnection();
        String quoteOfTheDay="";
        String query = "SELECT quote_of_the_day, tip_id FROM Fitness_Tips order by tip_id desc limit 1 ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        logger.info("Execute the query to get event list.");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            quoteOfTheDay = resultSet.getString("quote_of_the_day");
        }

        return quoteOfTheDay;
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

    private List<HomepageEventDto> extractResultList(ResultSet resultSet) throws SQLException {
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

            HomepageEventDto homepageEventDto = new HomepageEventDto(event_id, event_name, start_date, end_date,capacity,host_Name,event_description);
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
