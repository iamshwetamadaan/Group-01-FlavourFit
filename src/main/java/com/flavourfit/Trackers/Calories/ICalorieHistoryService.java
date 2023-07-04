package com.flavourfit.Trackers.Calories;

import java.sql.SQLException;

public interface ICalorieHistoryService {
    public void recordCalorieUpdate(double calorieCount, int userId) throws SQLException;

    public double fetchCalorieCountByDate(String date, int userId) throws SQLException;
}
