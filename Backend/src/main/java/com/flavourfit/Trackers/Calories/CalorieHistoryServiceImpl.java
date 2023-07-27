package com.flavourfit.Trackers.Calories;

import com.flavourfit.Exceptions.CalorieHistoryException;
import com.flavourfit.Helpers.DateHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CalorieHistoryServiceImpl implements ICalorieHistoryService {
    private static Logger logger = LoggerFactory.getLogger(CalorieHistoryServiceImpl.class);
    private final ICalorieHistoryDao calorieHistoryDao;

    @Autowired
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
    public void recordCalorieUpdate(double calorieCount, int userId) {
        logger.info("Started recordCalorieUpdate() method!");

        logger.info("Get current date string using DateHelpers static method");
        String currentDate = DateHelpers.getCurrentDateString();
        CalorieHistoryDto calorieHistoryDto = new CalorieHistoryDto(calorieCount, currentDate, userId);
        logger.info("Using calorieHistoryDao to record calorie count!!");
        try {
            this.calorieHistoryDao.addCalorieCount(calorieHistoryDto);
        } catch (SQLException e) {
            throw new CalorieHistoryException(e);
        }

        logger.info("Exiting recordCalorieUpdate() method!");
    }

    @Override
    public CalorieHistoryDto fetchCalorieByUserIdDate(String date, int userId) {
        logger.info("Started fetchCalorieCountByDate() method!");

        CalorieHistoryDto calorieHistoryDto = null;
        logger.info("Using calorieHistoryDao to get calorie count for given date!!");
        try {
            calorieHistoryDto = this.calorieHistoryDao.getCalorieByUserIdDate(date, userId);
        } catch (SQLException e) {
            throw new CalorieHistoryException(e);
        }

        logger.info("Exiting fetchCalorieCountByDate() method!");
        return calorieHistoryDto;
    }

    /**
     * Method to fetch the calories between given dates
     *
     * @param startDate -- Start date of the period
     * @param endDate   -- End date of the period
     * @param userId    -- ID of the user
     * @return -- GetResponse object with list of calories
     */
    @Override
    public List<CalorieGraphDto> fetchCalorieHistoryByPeriod(String startDate, String endDate, int userId) {
        if (startDate == null || startDate.isEmpty()) {
            logger.error("Invalid start date.");
            throw new CalorieHistoryException("Invalid start date.");
        }

        if (endDate == null || endDate.isEmpty()) {
            logger.error("Invalid start date.");
            throw new CalorieHistoryException("Invalid end date.");
        }

        List<CalorieGraphDto> calories = new ArrayList<>();

        try {
            List<CalorieHistoryDto> calorieHistoryList = this.calorieHistoryDao.getCalorieHistoryByPeriod(startDate, endDate, userId);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDateObj = formatter.parse(startDate);
            Date endDateObj = formatter.parse(endDate);

            Date date = endDateObj;


            while (!date.before(startDateObj)) {
                String dateStr = formatter.format(date);

                CalorieHistoryDto calorie = this.getCalorieFromList(dateStr, calorieHistoryList);
                if (calorie == null) {
                    calories.add(new CalorieGraphDto(dateStr, 0d));
                } else {
                    calories.add(new CalorieGraphDto(dateStr, calorie.getCalorieCount()));
                }

                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(date);
                dateCal.add(Calendar.DATE, -1);

                date = dateCal.getTime();
            }

            return calories;
        } catch (Exception e) {
            throw new CalorieHistoryException(e);
        }
    }

    private CalorieHistoryDto getCalorieFromList(String date, List<CalorieHistoryDto> calories) {
        for (CalorieHistoryDto calorie : calories) {
            if (calorie.getUpdateDate().equalsIgnoreCase(date)) {
                return calorie;
            }
        }
        return null;
    }

    @Override
    public CalorieHistoryDto fetchCalorieByUserIdCurrent(int userId) {
        logger.info("Started fetchCalorieCountByDate() method!");

        CalorieHistoryDto calorieHistoryDto = null;
        logger.info("Using calorieHistoryDao to get calorie count current!!");
        try {
            calorieHistoryDto = this.calorieHistoryDao.getCaloriesByUserIdCurrent(userId);
        } catch (SQLException e) {
            throw new CalorieHistoryException(e);
        }

        logger.info("Exiting fetchCalorieCountByDate() method!");
        return calorieHistoryDto;
    }
}
