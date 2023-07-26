package com.flavourfit.HealthCoach.Appointments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class AppointmentsServiceImpl implements IAppointmentsService{

    private static Logger logger = LoggerFactory.getLogger(AppointmentsServiceImpl.class);
    IAppointmentsDao appointmentDao;

    @Autowired
    public AppointmentsServiceImpl(IAppointmentsDao appointmentDao){
        this.appointmentDao = appointmentDao;
    }

    @Override
    public int bookAppointments(AppointmentsDto appointment, int userID) throws SQLException {
        logger.info("Started bookAppointments() from service class");

        logger.info("Check if coach id is present");
        if(appointment.getCoach_id()==0){
            throw new SQLException("Coach id not present");
        }

        logger.info("Check for start date and end date");
        if(appointment.getStartDate().length()==0 || appointment.getEndDate().length()==0){
            throw new SQLException("Invalid start date or end date");
        }

        logger.info("Calling bookAppointments() from bookAppoitmentsDao class");
        return appointmentDao.bookAppointments(appointment,userID);
    }
}
