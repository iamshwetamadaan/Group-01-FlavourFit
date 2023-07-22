package com.flavourfit.Trackers.Weights;

import com.flavourfit.Helpers.DateHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

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

            WeightHistoryDto waterHistoryDto = null;
            logger.info("Using waterHistoryDao to get water intake for given date!!");
            waterHistoryDto = this.weightHistoryDao.getWeightByUserIdDate(date, userId);

            logger.info("Exiting fetchWaterIntakeByUserIdDate() method!");
            return waterHistoryDto;
        }

        @Override
        public WeightHistoryDto getWeightByDates(String startdate, String enddate, int userId) throws SQLException {
            logger.info("Started getWaterIntakeByDates() method!");

            WeightHistoryDto weightHistoryDto = null;
            logger.info("Using waterHistoryDao to get water intake for given dates!!");
            weightHistoryDto = this.weightHistoryDao.getWeightByDates(startdate, enddate, userId);

            logger.info("Exiting getWaterIntakeByDates() method!");
            return weightHistoryDto;
        }
    }


