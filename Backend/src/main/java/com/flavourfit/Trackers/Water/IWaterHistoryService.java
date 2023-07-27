package com.flavourfit.Trackers.Water;


import java.sql.SQLException;
import java.util.List;

public interface IWaterHistoryService {
    public void recordWaterIntake(double waterIntake, int userId) throws SQLException;

    public WaterHistoryDto fetchWaterIntakeByUserIdDate(String date, int userId) throws SQLException;


    List<WaterGraphDto> fetchWaterHistoryByPeriod(String startDate, String endDate, int userId);

    WaterHistoryDto fetchWaterIntakeByUserIdCurrent(int userId) throws SQLException;
}
