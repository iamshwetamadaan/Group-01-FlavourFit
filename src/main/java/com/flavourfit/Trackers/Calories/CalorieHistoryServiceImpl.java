package com.flavourfit.Trackers.Calories;

import com.flavourfit.Helpers.DateHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class CalorieHistoryServiceImpl implements ICalorieHistoryService {
    private static Logger logger = LoggerFactory.getLogger(CalorieHistoryServiceImpl.class);
    private final ICalorieHistoryDao calorieHistoryDao;

    public CalorieHistoryServiceImpl(ICalorieHistoryDao calorieHistoryDao) {
        this.calorieHistoryDao = calorieHistoryDao;
    }

    /**
     * Method to record the calorie update
     *
     * @param calorieCount -- Double calorie count to be updated
     * @param userId       -- int id of the user
     * @throws SQLException
     */
    @Override
    public void recordCalorieUpdate(double calorieCount, int userId) throws SQLException {
        logger.info("Started recordCalorieUpdate() method!");

        logger.info("Get current date string using DateHelpers static method");
        String currentDate = DateHelpers.getCurrentDateString();
        CalorieHistoryDto calorieHistoryDto = new CalorieHistoryDto(calorieCount, currentDate, userId);
        logger.info("Using calorieHistoryDao to record calorie count!!");
        this.calorieHistoryDao.addCalorieCount(calorieHistoryDto);

        logger.info("Exiting recordCalorieUpdate() method!");
    }

    @Override
    public double fetchCalorieCountByDate(String date, int userId) throws SQLException {
        logger.info("Started fetchCalorieCountByDate() method!");

        double calorieCount = 0.0d;
        logger.info("Using calorieHistoryDao to get calorie count for given date!!");
        calorieCount = this.calorieHistoryDao.getCalorieByDate(date, 1);

        logger.info("Exiting fetchCalorieCountByDate() method!");
        return calorieCount;
    }
}
