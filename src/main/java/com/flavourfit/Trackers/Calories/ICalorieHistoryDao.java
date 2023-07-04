package com.flavourfit.Trackers.Calories;

import java.sql.SQLException;

public interface ICalorieHistoryDao {
    public void addCalorieCount(CalorieHistoryDto calorieHistoryDto) throws SQLException;

    public double getCalorieByDate(String date, int userId) throws SQLException;
}
