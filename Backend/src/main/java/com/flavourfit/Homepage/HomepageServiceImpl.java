package com.flavourfit.Homepage;

import com.flavourfit.Exceptions.TrackerException;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.Homepage.DTO.FitnessStreakDTO;
import com.flavourfit.Homepage.DTO.RoutineDTO;
import com.flavourfit.Trackers.Calories.CalorieHistoryDto;
import com.flavourfit.Trackers.Calories.CalorieHistoryServiceImpl;
import com.flavourfit.Trackers.Calories.ICalorieHistoryService;
import com.flavourfit.Trackers.Water.IWaterHistoryService;
import com.flavourfit.Trackers.Water.WaterHistoryDto;
import com.flavourfit.User.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service
public class
HomepageServiceImpl implements IHomepageService {

    private static Logger logger = LoggerFactory.getLogger(CalorieHistoryServiceImpl.class);
    private final IHomepageDao homepageDao;

    private final IWaterHistoryService waterHistoryService;

    private final ICalorieHistoryService calorieHistoryService;

    private final IUserService userService;

    @Autowired
    public HomepageServiceImpl(
            IHomepageDao homepageDao, IWaterHistoryService waterHistoryService,
            ICalorieHistoryService calorieHistoryService,
            IUserService userService
    ) {
        this.homepageDao = homepageDao;
        this.waterHistoryService = waterHistoryService;
        this.calorieHistoryService = calorieHistoryService;
        this.userService = userService;
    }




    @Override
    public HashMap<String, Object> getExerciseByUser(int userID) throws SQLException {
        logger.info("Entered getExcerciseByUser method");
        List<RoutineDTO> routines = homepageDao.getRoutinesByUser(userID);
        String quoteOfTheDay = homepageDao.getQuoteOfTheDay();
        HashMap<String, Object> result = new HashMap<>();
        result.put("quoteOfTheDay", quoteOfTheDay);
        result.put("routines", routines);
        return result;
    }

    @Override
    public Map<String, Object> fetchTrackerSummary(int userId) throws TrackerException {
        logger.info("Entered service method fetchTrackerSummary()");
        if (userId == 0) {
            logger.error("Invalid User");
            throw new UserNotFoundException("Invalid user");
        }

        Map<String, Object> trackerSummary = new HashMap<>();
        try {
            logger.info("Fetching current water intake and calories from respective services.");
            WaterHistoryDto waterHistoryDto = this.waterHistoryService.fetchWaterIntakeByUserIdCurrent(userId);
            CalorieHistoryDto calorieHistoryDto = this.calorieHistoryService.fetchCalorieByUserIdCurrent(userId);
            double waterIntake = 0.0d;
            double calorieCount = 0.0d;

            if(waterHistoryDto!=null){
                waterIntake=waterHistoryDto.getWaterIntake();
            }

            if(calorieHistoryDto!=null){
                calorieCount=calorieHistoryDto.getCalorieCount();
            }

            logger.info("Fetching currentWeight from userService");
            double currentWeight = this.userService.fetchUserCurrentWeight(userId);

            logger.info("Fetching tracker summary");
            FitnessStreakDTO fitnessStreak = this.homepageDao.getFitnessStreak(userId);

            trackerSummary.put("calorieCount", calorieCount);
            trackerSummary.put("waterIntake", waterIntake);
            trackerSummary.put("currentWeight", currentWeight);
            trackerSummary.put("fitnessStreak", fitnessStreak.getStreak());
            trackerSummary.put("averageStreakCalories", fitnessStreak.getAvgCalorie());
            trackerSummary.put("averageStreakWater", fitnessStreak.getAvgWaterIntake());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new TrackerException(e);
        }

        return trackerSummary;
    }

    @Override
    public List<HomepageEventDto> fetcheventlist() {
        logger.info("Entered service method fetcheventlist()");
        List<HomepageEventDto> eventlist = new ArrayList<>();
        try {

            List<HomepageEventDto> eventslist = this.homepageDao.getEventList();
            logger.info("Fetching event list.");
            HomepageEventDto event = this.geteventList(eventslist);
            eventlist.add(new HomepageEventDto(event.getevent_ID(), event.getEvent_name(),event.getStart_date(),event.getEnd_date(),event.getCapacity(),event.getHostname(),event.getEvent_description()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return eventlist;
    }

    private HomepageEventDto geteventList( List<HomepageEventDto> eventlist) {
        for (HomepageEventDto event : eventlist)
                return event;
        return null;
    }
}
