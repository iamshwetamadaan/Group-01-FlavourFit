package com.flavourfit.Homepage;

import com.flavourfit.Exceptions.TrackerException;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.Homepage.DTO.FitnessStreakDTO;
import com.flavourfit.Homepage.DTO.RoutineDTO;
import com.flavourfit.Trackers.Calories.CalorieHistoryDto;
import com.flavourfit.Trackers.Calories.ICalorieHistoryService;
import com.flavourfit.Trackers.Water.IWaterHistoryService;
import com.flavourfit.Trackers.Water.WaterHistoryDto;
import com.flavourfit.User.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class HomepageServiceImplTest {
    @Mock
    private HomepageDaoImpl homepageDao;

    @InjectMocks
    private HomepageServiceImpl homepageService;

    @Mock
    private IWaterHistoryService waterHistoryService;

    @Mock
    private ICalorieHistoryService calorieHistoryService;

    @Mock
    private IUserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        //    trackersController = new TrackersController(calorieHistoryService, null, authService);
    }

    @Test
    public void getExerciseByUserTest() throws SQLException {
        int userId = 7; // Replace with a valid user ID for testing
        List<RoutineDTO> mockRoutines = new ArrayList<>();
        RoutineDTO mockRoutine = new RoutineDTO();
        mockRoutine.setRoutineDescription("This is description");
        mockRoutine.setRoutineName("Routine name");
        mockRoutine.setTips("This is tips");
        mockRoutines.add(mockRoutine);
        String mockQuote = "This is a mock quote of the day.";

        when(homepageDao.getRoutinesByUser(userId)).thenReturn(mockRoutines);
        when(homepageDao.getQuoteOfTheDay()).thenReturn(mockQuote);

        HashMap<String, Object> result = homepageService.getExerciseByUser(userId);

        // Assert the result contains the expected routines and quoteOfTheDay
        assertNotNull(result);
        assertEquals(2, result.size()); // Assuming routines is a List<RoutineDTO>
        assertEquals(mockQuote, result.get("quoteOfTheDay"));
    }

    @Test
    public void testFetchEventList() throws SQLException {

        List<HomepageEventDto> mockEventList = new ArrayList<>();
        mockEventList.add(new HomepageEventDto(1, "Inhale and exhale", "2023-09-01", "2023-09-01", "100", "Sasha Berkley", "Yoga and Pilates event"));
        mockEventList.add(new HomepageEventDto(2, "Fitness freak", "2023-09-07", "2023-09-07", "100", "John Mendow", "HIIT Workout session"));

        when(homepageDao.getEventList()).thenReturn(mockEventList);
        List<HomepageEventDto> eventList = homepageService.fetcheventlist();

        assertEquals(1, eventList.size()); // Check if two events are returned as expected
        assertEquals("Inhale and exhale", eventList.get(0).getEvent_name());
    }

    @Test
    void fetchTrackerSummaryTest() throws Exception {
        int userId = 1;

        WaterHistoryDto waterHistory = new WaterHistoryDto(100.0,"2023-08-01",userId);
        CalorieHistoryDto calorieHistory = new CalorieHistoryDto(200.0,"2023-08-01",userId);
        FitnessStreakDTO fitnessStreak = new FitnessStreakDTO();
        fitnessStreak.setStreak(5);
        fitnessStreak.setAvgCalorie(150.0);
        fitnessStreak.setAvgWaterIntake(75.0);

        when(waterHistoryService.fetchWaterIntakeByUserIdCurrent(userId)).thenReturn(waterHistory);
        when(calorieHistoryService.fetchCalorieByUserIdCurrent(userId)).thenReturn(calorieHistory);
        when(userService.fetchUserCurrentWeight(userId)).thenReturn(60.0);
        when(homepageDao.getFitnessStreak(userId)).thenReturn(fitnessStreak);

        Map<String, Object> trackerSummary = homepageService.fetchTrackerSummary(userId);

        assertEquals(200.0, trackerSummary.get("calorieCount"));
        assertEquals(100.0, trackerSummary.get("waterIntake"));
        assertEquals(60.0, trackerSummary.get("currentWeight"));
        assertEquals(5, trackerSummary.get("fitnessStreak"));
        assertEquals(150.0, trackerSummary.get("averageStreakCalories"));
        assertEquals(75.0, trackerSummary.get("averageStreakWater"));
    }

}
