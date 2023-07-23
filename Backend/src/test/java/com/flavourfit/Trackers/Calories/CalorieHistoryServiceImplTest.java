package com.flavourfit.Trackers.Calories;

import com.flavourfit.Exceptions.CalorieHistoryException;
import com.flavourfit.Helpers.DateHelpers;
import com.flavourfit.ResponsesDTO.GetResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalorieHistoryServiceImplTest {

    @Mock
    private ICalorieHistoryDao calorieHistoryDao;

    @InjectMocks
    private CalorieHistoryServiceImpl calorieHistoryService;

    @Test
    public void recordCalorieUpdateTest() {
        double calorieCount = 1500.00;
        int userId = 1;

        try {
            doNothing().when(calorieHistoryDao).addCalorieCount(any(CalorieHistoryDto.class));
            calorieHistoryService.recordCalorieUpdate(calorieCount, userId);
            verify(calorieHistoryDao, times(1)).addCalorieCount(any(CalorieHistoryDto.class));
        } catch (SQLException e) {
            fail("No exception should be thrown");
        }

        try {
            doThrow(SQLException.class).when(calorieHistoryDao).addCalorieCount(any(CalorieHistoryDto.class));
            assertThrows(CalorieHistoryException.class, () -> calorieHistoryService.recordCalorieUpdate(calorieCount, userId));
            verify(calorieHistoryDao, times(2)).addCalorieCount(any(CalorieHistoryDto.class));
        } catch (SQLException e) {
            fail("No exception should be thrown");
        }
    }

    @Test
    public void fetchCalorieByUserIdDateTest() {
        String date = "2023-07-20";
        int userId = 1;
        double calorieCount = 2000.00;
        CalorieHistoryDto calorieHistoryDto = new CalorieHistoryDto(calorieCount, date, userId);

        try {
            when(calorieHistoryDao.getCalorieByUserIdDate(date, userId)).thenReturn(calorieHistoryDto);
            CalorieHistoryDto result = calorieHistoryService.fetchCalorieByUserIdDate(date, userId);
            verify(calorieHistoryDao, times(1)).getCalorieByUserIdDate(date, userId);
            assertEquals(calorieCount, result.getCalorieCount());
            assertEquals(date, result.getUpdateDate());
            assertEquals(userId, result.getUserId());
        } catch (SQLException e) {
            fail("No exception should be thrown");
        }

        try {
            when(calorieHistoryDao.getCalorieByUserIdDate(date, userId)).thenThrow(SQLException.class);
            assertThrows(CalorieHistoryException.class, () -> calorieHistoryService.fetchCalorieByUserIdDate(date, userId));
            verify(calorieHistoryDao, times(2)).getCalorieByUserIdDate(date, userId);
        } catch (SQLException e) {
            fail("No exception should be thrown");
        }
    }

    @Test
    public void fetchCalorieHistoryByPeriodTest() {
        String startDate = "2023-07-01";
        String endDate = "2023-07-20";
        int userId = 1;
        double calorieCount = 2000.00;
        CalorieHistoryDto calorieHistoryDto = new CalorieHistoryDto(calorieCount, startDate, userId);
        List<CalorieHistoryDto> calorieHistoryList = Arrays.asList(calorieHistoryDto);

        try {
            when(calorieHistoryDao.getCalorieHistoryByPeriod(startDate, endDate, userId)).thenReturn(calorieHistoryList);
            List<CalorieGraphDto> result = calorieHistoryService.fetchCalorieHistoryByPeriod(startDate, endDate, userId);
            verify(calorieHistoryDao, times(1)).getCalorieHistoryByPeriod(startDate, endDate, userId);
            assertEquals(calorieCount, result.get(19).getCalorie());
            assertEquals(startDate, result.get(19).getDate());
        } catch (Exception e) {
            fail("No exception should be thrown");
        }

        try {
            when(calorieHistoryDao.getCalorieHistoryByPeriod(startDate, endDate, userId)).thenThrow(SQLException.class);
            assertThrows(CalorieHistoryException.class, () -> calorieHistoryService.fetchCalorieHistoryByPeriod(startDate, endDate, userId));
            verify(calorieHistoryDao, times(2)).getCalorieHistoryByPeriod(startDate, endDate, userId);
        } catch (Exception e) {
            fail("No exception should be thrown");
        }

        assertThrows(CalorieHistoryException.class, () -> calorieHistoryService.fetchCalorieHistoryByPeriod(null, endDate, userId));
        assertThrows(CalorieHistoryException.class, () -> calorieHistoryService.fetchCalorieHistoryByPeriod("", endDate, userId));

        assertThrows(CalorieHistoryException.class, () -> calorieHistoryService.fetchCalorieHistoryByPeriod(startDate, null, userId));
        assertThrows(CalorieHistoryException.class, () -> calorieHistoryService.fetchCalorieHistoryByPeriod(startDate, "", userId));
    }




}
