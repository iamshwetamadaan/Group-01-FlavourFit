package com.flavourfit.Trackers.Water;
import com.flavourfit.Exceptions.WaterHistoryException;

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
public class WaterHistoryServiceImpl implements IWaterHistoryService {
    private static Logger logger = LoggerFactory.getLogger(com.flavourfit.Trackers.Water.WaterHistoryServiceImpl.class);
    private final IWaterHistoryDao waterHistoryDao;

    @Autowired
    public WaterHistoryServiceImpl(IWaterHistoryDao waterHistoryDao) {

        this.waterHistoryDao = waterHistoryDao;
    }

    /**
     * Method to record the water intake
     *
     * @param waterintake -- Double water intake to be updated
     * @param userId      -- int id of the user
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

    /**
     * Method to fetch the calories between given dates
     *
     * @param startDate -- Start date of the period
     * @param endDate   -- End date of the period
     * @param userId    -- ID of the user
     * @return -- GetResponse object with list of calories
     */
    @Override
    public List<WaterGraphDto> fetchWaterHistoryByPeriod(String startDate, String endDate, int userId)  {
        if (startDate == null || startDate.isEmpty()) {
            logger.error("Invalid start date.");
            //   throw new CalorieHistoryException("Invalid start date.");
        }

        if (endDate == null || endDate.isEmpty()) {
            logger.error("Invalid start date.");
            //  throw new CalorieHistoryException("Invalid end date.");
        }

        List<WaterGraphDto> waterintakes = new ArrayList<>();

       try {
            List<WaterHistoryDto> waterHistoryList = this.waterHistoryDao.getWaterHistoryByPeriod(
                    startDate, endDate, userId);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDateObj = formatter.parse(startDate);
            Date endDateObj = formatter.parse(endDate);

            Date date = endDateObj;


            while (!date.before(startDateObj)) {
                String dateStr = formatter.format(date);

                WaterHistoryDto waterintake = this.getWaterIntakeFromList(dateStr, waterHistoryList);
                if (waterintake == null) {
                    waterintakes.add(new WaterGraphDto(dateStr, 0d));
                } else {
                    waterintakes.add(new WaterGraphDto(dateStr, waterintake.getWaterIntake()));
                }

                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(date);
                dateCal.add(Calendar.DATE, -1);

                date = dateCal.getTime();
            }

            return waterintakes;
        } catch (Exception e) {
            throw new WaterHistoryException(e);
        }

    }
        private WaterHistoryDto getWaterIntakeFromList (String date, List < WaterHistoryDto > waterintakes){
            for (WaterHistoryDto waterintake : waterintakes) {
                if (waterintake.getUpdateDate().equalsIgnoreCase(date)) {
                    return waterintake;
                }
            }
            return null;
        }

    @Override
    public WaterHistoryDto fetchWaterIntakeByUserIdCurrent(int userId) throws SQLException {
        logger.info("Started fetchWaterIntakeByUserIdCurrent() method!");

        WaterHistoryDto waterHistoryDto = null;
        logger.info("Using waterHistoryDao to get water intake for given date!!");
        waterHistoryDto = this.waterHistoryDao.getWaterIntakeByUserIdCurrent(userId);

        logger.info("Exiting fetchWaterIntakeByUserIdDate() method!");
        return waterHistoryDto;
    }

    }

