package com.flavourfit.Homepage;

import com.flavourfit.Homepage.DTO.FitnessStreakDTO;
import com.flavourfit.Homepage.DTO.RoutineDTO;

import java.sql.SQLException;
import java.util.List;

public interface IHomepageDao {


    public List<RoutineDTO> getRoutinesByUser(int userId) throws SQLException;

    public String getQuoteOfTheDay() throws SQLException;

    FitnessStreakDTO getFitnessStreak(int userId) throws SQLException;

    public List<HomepageEventDto> getEventList() throws SQLException;
}
