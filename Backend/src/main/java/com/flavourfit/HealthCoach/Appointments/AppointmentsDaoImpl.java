package com.flavourfit.HealthCoach.Appointments;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.HealthCoach.HealthCoachDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class AppointmentsDaoImpl implements IAppointmentsDao{

    private static Logger logger = LoggerFactory.getLogger(HealthCoachDaoImpl.class);
    private final IDatabaseManager database;
    private Connection connection;

    @Autowired
    public AppointmentsDaoImpl(DatabaseManagerImpl database) {
        this.database = database;
        if (this.database != null && this.database.getConnection() != null) {
            this.connection = this.database.getConnection();
        }
    }
    @Override
    public int bookAppointments(AppointmentsDto appointment, int userId) throws SQLException{
        logger.info("Started bookAppointments method");
        int count = 0;
        this.testConnection();

        logger.info("Creating a prepared statement to book an appointment");
        String query = "Insert into Appointments values (?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        logger.info("Replacing values in prepared statement with actual values to be inserted");
        preparedStatement.setString(1,null);
        preparedStatement.setString(2,appointment.getStartDate());
        preparedStatement.setString(3,appointment.getEndDate());
        preparedStatement.setInt(4,userId);
        preparedStatement.setInt(5,appointment.getCoach_id());
        logger.info("Executing the update user request");
        count = preparedStatement.executeUpdate();
        return count;
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
