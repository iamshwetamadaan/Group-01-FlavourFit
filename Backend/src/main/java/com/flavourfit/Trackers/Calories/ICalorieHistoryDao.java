package com.flavourfit.Trackers.Calories;

import java.sql.SQLException;
import java.util.List;

public interface ICalorieHistoryDao {
    public void addCalorieCount(CalorieHistoryDto calorieHistoryDto) throws SQLException;

    CalorieHistoryDto getCalorieByUserIdDate(String date, int userId) throws SQLException;

    List<CalorieHistoryDto> getCalorieHistoryByPeriod(String startDate, String endDate, int userId) throws SQLException;

    CalorieHistoryDto getCaloriesByUserIdCurrent(int userId) throws SQLException;
}
