package com.flavourfit.Trackers.WaterIntake;

import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Trackers.Water.WaterHistoryDaoImpl;
import com.flavourfit.Trackers.Water.WaterHistoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class WaterHistoryDaoImplTest {

    @InjectMocks
    private WaterHistoryDaoImpl waterHistoryDao;

    @Mock
    private IDatabaseManager database;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    public void initMocks() throws SQLException {
        MockitoAnnotations.initMocks(this);
        when(database.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    public void addWaterIntakeTest() throws SQLException {
        WaterHistoryDto waterHistoryDto = new WaterHistoryDto(1, 0, "2023-07-01", 1);

        assertThrows(SQLException.class, () -> waterHistoryDao.addWaterIntake(null));

        assertDoesNotThrow(() -> waterHistoryDao.addWaterIntake(new WaterHistoryDto(1, 0, "2023-07-01", 1)));

        assertThrows(SQLException.class, () -> waterHistoryDao.addWaterIntake(new WaterHistoryDto(1, 1500, "", 1)));

        WaterHistoryDto validDto = new WaterHistoryDto(1, 1500, "2023-07-01", 1);
        waterHistoryDao.addWaterIntake(validDto);
    }

    @Test
    public void updateWaterHistoryTest() throws SQLException {
        WaterHistoryDto waterHistoryDto = new WaterHistoryDto(1, 1500, "2023-07-23", 1);
        assertThrows(SQLException.class, () -> waterHistoryDao.updateWaterHistory(null));

        when(preparedStatement.executeUpdate()).thenReturn(1);

        waterHistoryDao.updateWaterHistory(waterHistoryDto);

    }


    @Test
    public void getWaterIntakeByUserIdDateTest() throws SQLException {
        String testDate = "2023-07-01";
        int testUserId = 1;

        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("Water_history_id")).thenReturn(1);
        when(rs.getDouble("Water_intake")).thenReturn(1500.00d);
        when(rs.getString("Update_Date")).thenReturn(testDate);
        when(rs.getInt("User_id")).thenReturn(testUserId);
        when(preparedStatement.executeQuery()).thenReturn(rs);

        WaterHistoryDto result = waterHistoryDao.getWaterIntakeByUserIdDate(testDate, testUserId);

        assertNotNull(result);
        assertEquals(4, result.getWaterHistoryId());
        assertEquals(testDate, result.getUpdateDate());
        assertEquals(testUserId, result.getUserId());
    }

    @Test
    public void getWaterHistoryByPeriodTest() throws SQLException {

        List<WaterHistoryDto> mockWaterHistoryList = new ArrayList<>();
        mockWaterHistoryList.add(new WaterHistoryDto(1, 1500, "2023-07-01", 1));
        mockWaterHistoryList.add(new WaterHistoryDto(2, 2000, "2023-07-02", 1));

        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rs.getInt("Water_history_id")).thenReturn(1).thenReturn(2);
        when(rs.getDouble("Water_intake")).thenReturn(1500.00d).thenReturn(2000.00d);
        when(rs.getString("Update_Date")).thenReturn("2023-07-01").thenReturn("2023-07-02");
        when(rs.getInt("User_id")).thenReturn(1).thenReturn(1);
        when(preparedStatement.executeQuery()).thenReturn(rs);


        assertThrows(SQLException.class, () -> waterHistoryDao.getWaterHistoryByPeriod(null, "2023-07-02", 1));
        assertThrows(SQLException.class, () -> waterHistoryDao.getWaterHistoryByPeriod("", "2023-07-02", 1));


        assertThrows(SQLException.class, () -> waterHistoryDao.getWaterHistoryByPeriod("2023-07-01", null, 1));
        assertThrows(SQLException.class, () -> waterHistoryDao.getWaterHistoryByPeriod("2023-07-01", "", 1));


        List<WaterHistoryDto> validDtoList = waterHistoryDao.getWaterHistoryByPeriod("2023-07-01", "2023-07-02", 1);
        for (int i = 0; i < validDtoList.size(); i++) {
            assertEquals(mockWaterHistoryList.get(i).getUpdateDate(), validDtoList.get(i).getUpdateDate());
            assertEquals(mockWaterHistoryList.get(i).getUserId(), validDtoList.get(i).getUserId());
        }
    }

}
