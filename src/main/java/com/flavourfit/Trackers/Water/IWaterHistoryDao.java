package com.flavourfit.Trackers.Water;


import java.sql.SQLException;

public interface IWaterHistoryDao {
    public void addWaterIntake(WaterHistoryDto waterHistoryDto) throws SQLException;

    WaterHistoryDto getWaterIntakeByUserIdDate(String date, int userId) throws SQLException;

}
