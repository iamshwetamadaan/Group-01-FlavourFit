package com.flavourfit.Homepage;

import com.flavourfit.Exceptions.TrackerException;
import com.flavourfit.Homepage.DTO.FitnessStreakDTO;
import com.flavourfit.Homepage.DTO.RoutineDTO;
import com.flavourfit.Trackers.Calories.CalorieGraphDto;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IHomepageService {


    public HashMap<String, Object> getExerciseByUser(int userID) throws SQLException;

    Map<String, Object> fetchTrackerSummary(int userId) throws TrackerException;

    List<HomepageEventDto> fetcheventlist();
}
