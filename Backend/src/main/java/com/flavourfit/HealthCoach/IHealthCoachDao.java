package com.flavourfit.HealthCoach;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IHealthCoachDao {

    public ArrayList<HealthCoachDto> getAllHealthCoaches() throws SQLException;
}
