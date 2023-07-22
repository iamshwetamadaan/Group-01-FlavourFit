package com.flavourfit.Trackers.Water;


import java.sql.SQLException;

public interface IWaterHistoryService {
    public void recordWaterIntake(double waterIntake, int userId) throws SQLException;

    public WaterHistoryDto fetchWaterIntakeByUserIdDate(String date, int userId) throws SQLException;

   // public WaterHistoryDto getWaterIntakeByUserIdDate(String date, int userId) throws SQLException;

    public WaterHistoryDto getWaterIntakeByDates(String startdate, String enddate, int userId) throws SQLException;
}
