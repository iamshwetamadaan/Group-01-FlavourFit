package com.flavourfit.Trackers.Water;


import java.sql.SQLException;
import java.util.List;

public interface IWaterHistoryDao {
    public void addWaterIntake(WaterHistoryDto waterHistoryDto) throws SQLException;

    WaterHistoryDto getWaterIntakeByUserIdDate(String date, int userId) throws SQLException;

    WaterHistoryDto getWaterIntakeByUserIdCurrent(int userId) throws SQLException;

    List<WaterHistoryDto> getWaterHistoryByPeriod(String startDate, String endDate, int userId) throws SQLException;
}
