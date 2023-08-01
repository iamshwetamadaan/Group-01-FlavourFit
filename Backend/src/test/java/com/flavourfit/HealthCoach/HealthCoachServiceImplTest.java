package com.flavourfit.HealthCoach;

import com.flavourfit.Trackers.Calories.CalorieHistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class HealthCoachServiceImplTest {

    @Mock
    private IHealthCoachDao iHealthCoachDao;

    @InjectMocks
    private HealthCoachServiceImpl healthCoachService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllHealthCoachesTest() throws SQLException {
        ArrayList<HealthCoachDto> expectedHealthCoaches = new ArrayList<>();
        // Add some test data to the expected list

        // Mock the behavior of healthCoachDao.getAllHealthCoaches()
        when(iHealthCoachDao.getAllHealthCoaches()).thenReturn(expectedHealthCoaches);

        // Act
        ArrayList<HealthCoachDto> resultHealthCoaches = healthCoachService.getAllHealthCoaches();

        // Assert
        // Verify that the method was called once on the mockDao
        verify(iHealthCoachDao, times(1)).getAllHealthCoaches();

        // Check if the result matches the expected data
        assertEquals(expectedHealthCoaches, resultHealthCoaches);
    }
}
