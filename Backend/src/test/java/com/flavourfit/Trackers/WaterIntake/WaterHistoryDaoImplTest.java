package com.flavourfit.Trackers.WaterIntake;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.Trackers.Water.WaterHistoryDaoImpl;
import com.flavourfit.Trackers.Water.WaterHistoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private WaterHistoryDaoImpl waterHistoryDao;

    @Mock
    private DatabaseManagerImpl database;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    public void initMocks() throws SQLException {
        MockitoAnnotations.openMocks(this);
        reset(database,connection,resultSet,preparedStatement);
        when(database.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        waterHistoryDao = new WaterHistoryDaoImpl(database);
    }

    @Test
    public void addWaterIntakeTest() throws SQLException {
        WaterHistoryDto waterHistoryDto = new WaterHistoryDto(1, 500.0, "2023-08-01", 1234);
        waterHistoryDao.addWaterIntake(waterHistoryDto);

        try {
            waterHistoryDao.addWaterIntake(null);
            fail("Expected SQLException not thrown");
        } catch (SQLException e) {
            assertEquals("WaterHistoryDto object not valid!!", e.getMessage());
        }

        WaterHistoryDto invalidWaterIntake = new WaterHistoryDto(1, 0.0, "2023-08-01", 1234);
        waterHistoryDao.addWaterIntake(invalidWaterIntake);

        WaterHistoryDto invalidDate = new WaterHistoryDto(1, 500.0, "", 1234);
        try {
            waterHistoryDao.addWaterIntake(invalidDate);
            fail("Expected SQLException not thrown");
        } catch (SQLException e) {
            assertEquals("Invalid date input while fetching water history!!", e.getMessage());
        }
    }

    @Test
    public void updateWaterHistoryTest() throws SQLException {
        WaterHistoryDto waterHistoryDto = new WaterHistoryDto(1, 500.0, "2023-08-01", 1234);
        waterHistoryDao.updateWaterHistory(waterHistoryDto);

        try {
            waterHistoryDao.updateWaterHistory(null);
            fail("Expected SQLException not thrown");
        } catch (SQLException e) {
            assertEquals("Invalid data while updating water history!!", e.getMessage());
        }

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
        assertEquals(1, result.getWaterHistoryId());
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
