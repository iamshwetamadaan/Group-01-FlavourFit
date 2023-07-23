package com.flavourfit.Trackers.WaterIntake;

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
public class WaterHistoryServiceImplTest {

    @Mock
    private IWaterHistoryDao waterHistoryDao;

    @InjectMocks
    private WaterHistoryServiceImpl waterHistoryService;

    @Test
    public void recordWaterIntakeTest() throws SQLException {
        double waterIntake = 1500;
        int userId = 1;
        String currentDate = "2023-07-01";

        // Call the method to record water intake
        waterHistoryService.recordWaterIntake(waterIntake, userId);


    }


    @Test
    public void fetchCalorieByUserIdDateTest() {

    }
    @Test
    public void fetchWaterHistoryByPeriodTest() throws SQLException {
        String startDate = "2023-07-01";
        String endDate = "2023-07-10";
        int userId = 1;

        List<WaterHistoryDto> mockWaterHistoryList = new ArrayList<>();
        mockWaterHistoryList.add(new WaterHistoryDto(1, 1500, "2023-07-01", 1));
        mockWaterHistoryList.add(new WaterHistoryDto(2, 2000, "2023-07-03", 1));
        mockWaterHistoryList.add(new WaterHistoryDto(3, 1800, "2023-07-05", 1));
        mockWaterHistoryList.add(new WaterHistoryDto(4, 2200, "2023-07-10", 1));

        when(waterHistoryDao.getWaterHistoryByPeriod(startDate, endDate, userId)).thenReturn(mockWaterHistoryList);

        List<WaterGraphDto> result = waterHistoryService.fetchWaterHistoryByPeriod(startDate, endDate, userId);

        verify(waterHistoryDao, times(1)).getWaterHistoryByPeriod(startDate, endDate, userId);
        assertEquals(10, result.size());

    }



}
