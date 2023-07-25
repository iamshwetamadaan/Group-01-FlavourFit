package com.flavourfit.Trackers.Calories;

import com.flavourfit.Exceptions.CalorieHistoryException;
import com.flavourfit.ResponsesDTO.GetResponse;

import java.sql.SQLException;
import java.util.List;

public interface ICalorieHistoryService {
    public void recordCalorieUpdate(double calorieCount, int userId);

    public CalorieHistoryDto fetchCalorieByUserIdDate(String date, int userId);

    List<CalorieGraphDto> fetchCalorieHistoryByPeriod(String startDate, String endDate, int userId);

    CalorieHistoryDto fetchCalorieByUserIdCurrent(int userId);
}
