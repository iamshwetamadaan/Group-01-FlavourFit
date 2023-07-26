package com.flavourfit.Homepage;

import com.flavourfit.Homepage.DTO.RoutineDTO;
import com.flavourfit.Trackers.Calories.CalorieHistoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Service
public class
HomepageServiceImpl implements IHomepageService {

    private static Logger logger = LoggerFactory.getLogger(CalorieHistoryServiceImpl.class);
    private final IHomepageDao homepageDao;

    @Autowired
    public HomepageServiceImpl(IHomepageDao homepageDao) {
        this.homepageDao = homepageDao;
    }


    @Override
    public List<HomepageEventDto> getEventList() {
        

        try {
            List<HomepageEventDto> eventlist = this.homepageDao.getEventList();

        return eventlist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HashMap<String, Object> getExerciseByUser(int userID) throws SQLException {
        logger.info("Entered getExcerciseByUser method");
        List<RoutineDTO> routines = homepageDao.getRoutinesByUser(userID);
        String quoteOfTheDay = homepageDao.getQuoteOfTheDay();
        HashMap<String,Object> result = new HashMap<>();
        result.put("quoteOfTheDay",quoteOfTheDay);
        result.put("routines",routines);
        return result;
    }
}
