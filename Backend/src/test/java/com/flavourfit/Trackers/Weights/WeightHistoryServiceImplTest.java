package com.flavourfit.Trackers.Weights;


import com.flavourfit.Exceptions.WeightHistoryException;
import com.flavourfit.Helpers.DateHelpers;
import com.flavourfit.ResponsesDTO.GetResponse;
import com.flavourfit.Trackers.Water.IWaterHistoryDao;
import com.flavourfit.Trackers.Water.WaterGraphDto;
import com.flavourfit.Trackers.Water.WaterHistoryDto;
import com.flavourfit.Trackers.Water.WaterHistoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeightHistoryServiceImplTest {

    @Mock
    private IWeightHistoryDao weightHistoryDao;

    @InjectMocks
    private WeightHistoryServiceImpl weightHistoryService;

    @Test
    public void recordWeightTest() throws SQLException {
        double weight = 75.5;
        int userId = 1;
        WeightHistoryDto weightHistoryDto = new WeightHistoryDto(weight, "2023-07-23", userId);

        assertDoesNotThrow(() -> weightHistoryService.recordWeight(weight, userId));
    }


    @Test
    public void fetchWeightByUserIdDateTest() throws SQLException {
        String date = "2023-07-20";
        int userId = 1;
        double weight = 75.5;
        WeightHistoryDto weightHistoryDto = new WeightHistoryDto(weight, date, userId);

        when(weightHistoryDao.getWeightByUserIdDate(date, userId)).thenReturn(weightHistoryDto);

        WeightHistoryDto result = weightHistoryService.fetchWeightByUserIdDate(date, userId);

        verify(weightHistoryDao, times(1)).getWeightByUserIdDate(date, userId);
        assertEquals(weight, result.getWeight());
        assertEquals(date, result.getUpdateDate());
        assertEquals(userId, result.getUserId());
    }

    @Test
    public void fetchWeightByUserIdDateTest_ThrowsSQLException() throws SQLException {
        String date = "2023-07-20";
        int userId = 1;

        when(weightHistoryDao.getWeightByUserIdDate(date, userId)).thenThrow(SQLException.class);

        assertThrows(SQLException.class, () -> weightHistoryService.fetchWeightByUserIdDate(date, userId));

        verify(weightHistoryDao, times(1)).getWeightByUserIdDate(date, userId);
    }

    @Test
    public void fetchWeightHistoryByPeriodTest() throws SQLException {
        String startDate = "2023-07-20";
        String endDate = "2023-07-22";
        int userId = 1;

        List<WeightHistoryDto> weightHistoryList = new ArrayList<>();
        weightHistoryList.add(new WeightHistoryDto(75.0, "2023-07-20", userId));
        weightHistoryList.add(new WeightHistoryDto(74.5, "2023-07-21", userId));
        weightHistoryList.add(new WeightHistoryDto(74.0, "2023-07-22", userId));

        when(weightHistoryDao.getWeightHistoryByPeriod(startDate, endDate, userId)).thenReturn(weightHistoryList);

        List<WeightGraphDto> result = weightHistoryService.fetchWeightHistoryByPeriod(startDate, endDate, userId);

        verify(weightHistoryDao, times(1)).getWeightHistoryByPeriod(startDate, endDate, userId);
        assertEquals(3, result.size());
        assertEquals(74.0, result.get(0).getWeight());
        assertEquals("2023-07-22", result.get(0).getDate());
        assertEquals(74.5, result.get(1).getWeight());
        assertEquals("2023-07-21", result.get(1).getDate());
    }

    @Test
    public void fetchWeightHistoryByPeriodTest_InvalidDates() {
        String startDate = "";
        String endDate = "2023-07-22";
        int userId = 1;

        assertThrows(WeightHistoryException.class, () -> weightHistoryService.fetchWeightHistoryByPeriod(startDate, endDate, userId));
    }

}
