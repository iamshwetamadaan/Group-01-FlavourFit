package com.flavourfit.HealthCoach.Appointments;

import java.sql.SQLException;

public interface IAppointmentsService {

    public int bookAppointments(AppointmentsDto appointment, int userId) throws SQLException;
}
