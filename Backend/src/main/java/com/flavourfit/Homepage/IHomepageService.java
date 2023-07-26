package com.flavourfit.Homepage;

import com.flavourfit.Homepage.DTO.RoutineDTO;
import com.flavourfit.Trackers.Calories.CalorieGraphDto;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface IHomepageService {

    public List<HomepageEventDto> getEventList();

    public HashMap<String,Object> getExerciseByUser(int userID) throws SQLException;

}
