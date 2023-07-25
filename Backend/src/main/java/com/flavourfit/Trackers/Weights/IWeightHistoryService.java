package com.flavourfit.Trackers.Weights;
import java.sql.SQLException;
import java.util.List;

public interface IWeightHistoryService {

    void recordWeight(double weight, int userId) throws SQLException;


    public WeightHistoryDto fetchWeightByUserIdDate(String date, int userId) throws SQLException;


    List<WeightGraphDto> fetchWeightHistoryByPeriod(String startDate, String endDate, int userId);

    WeightHistoryDto fetchWeightByUserIdCurrent(int userId) throws SQLException;
}



