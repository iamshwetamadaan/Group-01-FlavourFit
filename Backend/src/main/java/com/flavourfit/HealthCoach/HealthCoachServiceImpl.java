package com.flavourfit.HealthCoach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class HealthCoachServiceImpl implements IHealthCoachService{

    private static Logger logger = LoggerFactory.getLogger(HealthCoachServiceImpl.class);

    private IHealthCoachDao healthCoachDao;

    @Autowired
    public HealthCoachServiceImpl(IHealthCoachDao healthCoachDao){
        this.healthCoachDao = healthCoachDao;
    }
    @Override
    public ArrayList<HealthCoachDto> getAllHealthCoaches() throws SQLException {
        logger.info("Started getAllHealthCoaches() method");
        return healthCoachDao.getAllHealthCoaches();
    }
}
