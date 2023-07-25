package com.flavourfit.Trackers.Weights;

import com.flavourfit.Exceptions.WeightHistoryException;
import com.flavourfit.Helpers.DateHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
    public class WeightHistoryServiceImpl implements IWeightHistoryService {
        private static Logger logger = LoggerFactory.getLogger(com.flavourfit.Trackers.Water.WaterHistoryServiceImpl.class);
        private final IWeightHistoryDao weightHistoryDao;

        @Autowired
        public WeightHistoryServiceImpl(IWeightHistoryDao waterHistoryDao) {

            this.weightHistoryDao =  waterHistoryDao;
        }

        /**
         * Method to record the water intake
         *
         * @param weight -- Double water intake to be updated
         * @param userId       -- int id of the user
         * @throws SQLException
         */
        @Override
        public void recordWeight(double weight, int userId) throws SQLException {
            logger.info("Started recordWaterIntake() method!");

            logger.info("Get current date string using DateHelpers static method");
            String currentDate = DateHelpers.getCurrentDateString();
            WeightHistoryDto waterHistoryDto = new WeightHistoryDto(weight, currentDate, userId);
            logger.info("Using waterHistoryDao to record water intake!!");
            this.weightHistoryDao.addWeight(waterHistoryDto);

            logger.info("Exiting recordWaterIntake() method!");
        }

        @Override
        public WeightHistoryDto fetchWeightByUserIdDate(String date, int userId) throws SQLException {
            logger.info("Started fetchWaterIntakeByUserIdDate() method!");

            WeightHistoryDto weightHistoryDto = null;
            logger.info("Using waterHistoryDao to get water intake for given date!!");
            weightHistoryDto = this.weightHistoryDao.getWeightByUserIdDate(date, userId);

            logger.info("Exiting fetchWaterIntakeByUserIdDate() method!");
            return weightHistoryDto;
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
    public List<WeightGraphDto> fetchWeightHistoryByPeriod(String startDate, String endDate, int userId)  {
        if (startDate == null || startDate.isEmpty()) {
            logger.error("Invalid start date.");
            //   throw new CalorieHistoryException("Invalid start date.");
        }

        if (endDate == null || endDate.isEmpty()) {
            logger.error("Invalid start date.");
            //  throw new CalorieHistoryException("Invalid end date.");
        }

        List<WeightGraphDto> weights = new ArrayList<>();

        try {
            List<WeightHistoryDto> weightHistoryList = this.weightHistoryDao.getWeightHistoryByPeriod(
                    startDate, endDate, userId);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDateObj = formatter.parse(startDate);
            Date endDateObj = formatter.parse(endDate);

            Date date = endDateObj;


            while (!date.before(startDateObj)) {
                String dateStr = formatter.format(date);

                WeightHistoryDto weight = this.getWeightFromList(dateStr, weightHistoryList);
                if (weight == null) {
                    weights.add(new WeightGraphDto(dateStr, 0d));
                } else {
                    weights.add(new WeightGraphDto(dateStr, weight.getWeight()));
                }

                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(date);
                dateCal.add(Calendar.DATE, -1);

                date = dateCal.getTime();
            }

            return weights;
        } catch (Exception e) {
            throw new WeightHistoryException(e);
        }

    }
    private WeightHistoryDto getWeightFromList (String date, List < WeightHistoryDto > weights){
        for (WeightHistoryDto weight : weights) {
            if (weight.getUpdateDate().equalsIgnoreCase(date)) {
                return weight;
            }
        }
        return null;
    }

    @Override
    public WeightHistoryDto fetchWeightByUserIdCurrent(  int userId) throws SQLException {
        logger.info("Started fetchWaterIntakeByUserIdDate() method!");

        WeightHistoryDto weightHistoryDto = null;
        logger.info("Using waterHistoryDao to get water intake for given date!!");
        weightHistoryDto = this.weightHistoryDao.getWeightByUserIdCurrent(userId);

        logger.info("Exiting fetchWaterIntakeByUserIdDate() method!");
        return weightHistoryDto;
    }
}



