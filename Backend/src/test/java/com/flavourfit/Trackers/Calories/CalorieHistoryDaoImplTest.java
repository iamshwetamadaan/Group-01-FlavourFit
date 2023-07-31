package com.flavourfit.Trackers.Calories;

import com.flavourfit.DatabaseManager.IDatabaseManager;
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

public class CalorieHistoryDaoImplTest {

    @InjectMocks
    private CalorieHistoryDaoImpl calorieHistoryDao;

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
    public void addCalorieCountTest() throws SQLException {
        CalorieHistoryDto calorieHistoryDto = new CalorieHistoryDto(1, 0, "2023-07-01", 1);

        assertThrows(SQLException.class, () -> calorieHistoryDao.addCalorieCount(null));

        assertDoesNotThrow(() -> calorieHistoryDao.addCalorieCount(new CalorieHistoryDto(1, 0, "2023-07-01", 1)));

        assertThrows(SQLException.class, () -> calorieHistoryDao.addCalorieCount(new CalorieHistoryDto(1, 1500, "", 1)));

        CalorieHistoryDto validDto = new CalorieHistoryDto(1, 1500, "2023-07-01", 1);
        calorieHistoryDao.addCalorieCount(validDto);
    }

    @Test
    public void updateCalorieHistoryTest() throws SQLException {
        CalorieHistoryDto calorieHistoryDto = new CalorieHistoryDto(1, 1500, "2023-07-01", 1);

        assertThrows(SQLException.class, () -> calorieHistoryDao.updateCalorieHistory(null));

        assertDoesNotThrow(() -> calorieHistoryDao.updateCalorieHistory(new CalorieHistoryDto(1, 0, "2023-07-01", 1)));

        assertThrows(SQLException.class, () -> calorieHistoryDao.updateCalorieHistory(new CalorieHistoryDto(1, 1500, "", 1)));

        CalorieHistoryDto validDto = new CalorieHistoryDto(1, 1500, "2023-07-01", 1);
        calorieHistoryDao.updateCalorieHistory(validDto);
    }

    @Test
    public void getCalorieByUserIdDateTest() throws SQLException {
        CalorieHistoryDto mockCalorieHistoryDto = new CalorieHistoryDto(1, 1500, "2023-07-01", 1);
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("Calorie_history_id")).thenReturn(1);
        when(rs.getDouble("Calorie_Count")).thenReturn(19500.00d);
        when(rs.getString("Update_Date")).thenReturn("2023-07-01");
        when(rs.getInt("User_id")).thenReturn(1);
        when(preparedStatement.executeQuery()).thenReturn(rs);

        assertThrows(SQLException.class, () -> calorieHistoryDao.getCalorieByUserIdDate(null, 1));

        assertThrows(SQLException.class, () -> calorieHistoryDao.getCalorieByUserIdDate("", 1));

        CalorieHistoryDto validDto = calorieHistoryDao.getCalorieByUserIdDate("2023-07-01", 1);
        assertEquals(mockCalorieHistoryDto.getUpdateDate(), validDto.getUpdateDate());
        assertEquals(mockCalorieHistoryDto.getUserId(), validDto.getUserId());
    }

    @Test
    public void getCalorieHistoryByPeriodTest() throws SQLException {
        // Mock CalorieHistoryDto list
        List<CalorieHistoryDto> mockCalorieHistoryList = new ArrayList<>();
        mockCalorieHistoryList.add(new CalorieHistoryDto(1, 1500, "2023-07-01", 1));
        mockCalorieHistoryList.add(new CalorieHistoryDto(2, 2000, "2023-07-02", 1));

        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rs.getInt("Calorie_history_id")).thenReturn(1).thenReturn(2);
        when(rs.getDouble("Calorie_Count")).thenReturn(19500.00d).thenReturn(2000.00d);
        when(rs.getString("Update_Date")).thenReturn("2023-07-01").thenReturn("2023-07-02");
        when(rs.getInt("User_id")).thenReturn(1).thenReturn(1);
        when(preparedStatement.executeQuery()).thenReturn(rs);

        assertThrows(SQLException.class, () -> calorieHistoryDao.getCalorieHistoryByPeriod(null, "2023-07-02", 1));

        assertThrows(SQLException.class, () -> calorieHistoryDao.getCalorieHistoryByPeriod("", "2023-07-02", 1));

        assertThrows(SQLException.class, () -> calorieHistoryDao.getCalorieHistoryByPeriod("2023-07-01", null, 1));

        assertThrows(SQLException.class, () -> calorieHistoryDao.getCalorieHistoryByPeriod("2023-07-01", "", 1));

        List<CalorieHistoryDto> validDtoList = calorieHistoryDao.getCalorieHistoryByPeriod("2023-07-01", "2023-07-02", 1);
        assertEquals(1, validDtoList.size());
        for (int i = 0; i < validDtoList.size(); i++) {
            assertEquals(mockCalorieHistoryList.get(i).getUpdateDate(), validDtoList.get(i).getUpdateDate());
            assertEquals(mockCalorieHistoryList.get(i).getUserId(), validDtoList.get(i).getUserId());
        }
    }

}
