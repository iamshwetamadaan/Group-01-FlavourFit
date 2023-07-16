package com.flavourfit.Trackers.Water;

import com.flavourfit.Helpers.DateHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
@Service
public class WaterHistoryServiceImpl implements IWaterHistoryService{
    private static Logger logger = LoggerFactory.getLogger(com.flavourfit.Trackers.Water.WaterHistoryServiceImpl.class);
    private final IWaterHistoryDao waterHistoryDao;

    @Autowired
    public WaterHistoryServiceImpl(IWaterHistoryDao waterHistoryDao) {

        this.waterHistoryDao =  waterHistoryDao;
    }

    /**
     * Method to record the water intake
     *
     * @param waterintake -- Double water intake to be updated
     * @param userId       -- int id of the user
     * @throws SQLException
     */
    @Override
    public void recordWaterIntake(double waterintake, int userId) throws SQLException {
        logger.info("Started recordWaterIntake() method!");

        logger.info("Get current date string using DateHelpers static method");
        String currentDate = DateHelpers.getCurrentDateString();
        WaterHistoryDto waterHistoryDto = new WaterHistoryDto(waterintake, currentDate, userId);
        logger.info("Using waterHistoryDao to record water intake!!");
        this.waterHistoryDao.addWaterIntake(waterHistoryDto);

        logger.info("Exiting recordWaterIntake() method!");
    }

    @Override
    public WaterHistoryDto fetchWaterIntakeByUserIdDate(String date, int userId) throws SQLException {
        logger.info("Started fetchWaterIntakeByUserIdDate() method!");

        WaterHistoryDto waterHistoryDto = null;
        logger.info("Using waterHistoryDao to get water intake for given date!!");
        waterHistoryDto = this.waterHistoryDao.getWaterIntakeByUserIdDate(date, userId);

        logger.info("Exiting fetchWaterIntakeByUserIdDate() method!");
        return waterHistoryDto;
    }
}
