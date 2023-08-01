package com.flavourfit.HealthCoach;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Feeds.FeedDaoImpl;
import com.flavourfit.Feeds.FeedDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class HealthCoachDaoImpl implements IHealthCoachDao{
    private static Logger logger = LoggerFactory.getLogger(HealthCoachDaoImpl.class);
    private final IDatabaseManager database;
    private Connection connection;

    @Autowired
    public HealthCoachDaoImpl(DatabaseManagerImpl database) {
        this.database = database;
        if (this.database != null && this.database.getConnection() != null) {
            this.connection = this.database.getConnection();
        }
    }
    @Override
    public ArrayList<HealthCoachDto> getAllHealthCoaches() throws SQLException {
        logger.info("Started getFeedsById() method");
        ArrayList<HealthCoachDto> coaches = new ArrayList<HealthCoachDto>();

        this.testConnection();

        logger.info("Running select query to all the health coaches");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Health_Coaches");
        ResultSet resultSet = preparedStatement.executeQuery();
        logger.info("Obtained the list of coaches");
        while(resultSet.next()){
            coaches.add(this.extractHealthCoachDTO(resultSet));
        }

        logger.info("Returning received coaches as response");
        return coaches;
    }

    public HealthCoachDto extractHealthCoachDTO(ResultSet resultSet) throws SQLException{
        HealthCoachDto coach = new HealthCoachDto();
        coach.setFirst_name(resultSet.getString("first_name"));
        coach.setLast_name(resultSet.getString("last_name"));
        coach.setDescription(resultSet.getString("description"));
        coach.setCoach_id(resultSet.getInt("coach_id"));
        coach.setSpeciality(resultSet.getString("speciality"));
        coach.setYears_of_experience(resultSet.getString("years_of_experience"));
        return coach;
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
