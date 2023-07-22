package com.flavourfit.Trackers.Weights;


import com.flavourfit.Trackers.Water.WaterHistoryDto;

import java.sql.SQLException;
import java.util.List;

public interface IWeightHistoryDao {

    void addWeight(WeightHistoryDto weightHistoryDto) throws SQLException;

    public WeightHistoryDto getWeightByUserIdDate(String date, int userId) throws SQLException;


    List<WeightHistoryDto> getWeightHistoryByPeriod(String startDate, String endDate, int userId) throws SQLException;
}



