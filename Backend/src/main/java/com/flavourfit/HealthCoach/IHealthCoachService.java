package com.flavourfit.HealthCoach;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IHealthCoachService {

    public ArrayList<HealthCoachDto> getAllHealthCoaches() throws SQLException;
}
