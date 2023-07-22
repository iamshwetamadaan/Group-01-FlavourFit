package com.flavourfit.Trackers.Weights;

import java.sql.SQLException;

public interface IWeightHistoryService {

    void recordWeight(double weight, int userId) throws SQLException;



   // WeightHistoryDto getWeight(String startdate, String enddate, int userId) throws SQLException;



        public WeightHistoryDto fetchWeightByUserIdDate(String date, int userId) throws SQLException;



        public WeightHistoryDto getWeightByDates(String startdate, String enddate, int userId) throws SQLException;
    }



