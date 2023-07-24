package com.flavourfit.Homepage;

import com.flavourfit.Exceptions.CalorieHistoryException;
import com.flavourfit.Trackers.Calories.CalorieGraphDto;
import com.flavourfit.Trackers.Calories.CalorieHistoryDto;
import com.flavourfit.Trackers.Calories.CalorieHistoryServiceImpl;
import com.flavourfit.Trackers.Calories.ICalorieHistoryDao;
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
public class HomepageServiceImpl implements IHomepageService {

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
}
