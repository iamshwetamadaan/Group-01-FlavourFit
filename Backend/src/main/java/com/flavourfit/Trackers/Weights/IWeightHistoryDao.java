package com.flavourfit.Trackers.Weights;


import java.sql.SQLException;

public interface IWeightHistoryDao {

    void addWeight(WeightHistoryDto weightHistoryDto) throws SQLException;

    public WeightHistoryDto getWeightByUserIdDate(String date, int userId) throws SQLException;


      public  WeightHistoryDto getWeightByDates(String startdate, String enddate, int userId) throws SQLException;
    }


