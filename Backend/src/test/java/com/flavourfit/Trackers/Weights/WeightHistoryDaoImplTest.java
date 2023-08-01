package com.flavourfit.Trackers.Weights;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Homepage.HomepageDaoImpl;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class WeightHistoryDaoImplTest {
        private WeightHistoryDaoImpl weightHistoryDao;

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
            weightHistoryDao = new WeightHistoryDaoImpl(database);
        }

    @Test
    public void addWeightTest() throws SQLException {
        WeightHistoryDto weightHistoryDto = new WeightHistoryDto(1, 0, "2023-07-01", 1);

        assertThrows(SQLException.class, () -> weightHistoryDao.addWeight(null));

        assertDoesNotThrow(() -> weightHistoryDao.addWeight(new WeightHistoryDto(1, 0, "2023-07-01", 1)));

        assertThrows(SQLException.class, () -> weightHistoryDao.addWeight(new WeightHistoryDto(1, 1500, "", 1)));

        WeightHistoryDto validDto = new WeightHistoryDto(1, 1500, "2023-07-01", 1);
        weightHistoryDao.addWeight(validDto);
        verify(preparedStatement, times(2)).executeUpdate();
    }

    @Test
    public void updateWeightHistoryTest() throws SQLException {
        WeightHistoryDto weightHistoryDto = new WeightHistoryDto(1, 1500, "2023-07-23", 1);
        assertThrows(SQLException.class, () -> weightHistoryDao.updateWeightHistory(null));

        when(preparedStatement.executeUpdate()).thenReturn(1);

        weightHistoryDao.updateWeightHistory(weightHistoryDto);

        verify(preparedStatement, times(1)).executeUpdate();
    }
    @Test
    public void getWeightByUserIdDateTest() throws SQLException {
        String testDate = "2023-07-01";
        int testUserId = 1;

        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("weight_history_id")).thenReturn(1);
        when(rs.getDouble("weight")).thenReturn(150.00d);
        when(rs.getString("Update_Date")).thenReturn(testDate);
        when(rs.getInt("User_id")).thenReturn(testUserId);
        when(preparedStatement.executeQuery()).thenReturn(rs);

        WeightHistoryDto result = weightHistoryDao.getWeightByUserIdDate(testDate, testUserId);

        assertNotNull(result);
       // assertEquals(10, result.getWeightHistoryId());
       // assertEquals(150.00d, result.getWeight(), 0.001);
        assertEquals(testDate, result.getUpdateDate());
        assertEquals(testUserId, result.getUserId());
    }





    @Test
    public void getWeightHistoryByPeriodTest() throws SQLException {
        String startDate = "2023-07-01";
        String endDate = "2023-07-02";
        int userId = 1;

        // Mock the ResultSet
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rs.getInt("weight_history_id")).thenReturn(1).thenReturn(2);
        when(rs.getDouble("weight")).thenReturn(75.0).thenReturn(76.0);
        when(rs.getString("Update_Date")).thenReturn("2023-07-01").thenReturn("2023-07-02");
        when(rs.getInt("User_id")).thenReturn(userId).thenReturn(userId);
        when(preparedStatement.executeQuery()).thenReturn(rs);


        List<WeightHistoryDto> weightHistoryList = weightHistoryDao.getWeightHistoryByPeriod(startDate, endDate, userId);

        assertNotNull(weightHistoryList);
        assertEquals(2, weightHistoryList.size());


        WeightHistoryDto firstDto = weightHistoryList.get(0);
        assertEquals(1, firstDto.getWeightHistoryId());
        assertEquals(75.0, firstDto.getWeight(), 0.001);
        assertEquals("2023-07-01", firstDto.getUpdateDate());
        assertEquals(userId, firstDto.getUserId());


        WeightHistoryDto secondDto = weightHistoryList.get(1);
        assertEquals(2, secondDto.getWeightHistoryId());
        assertEquals(76.0, secondDto.getWeight(), 0.001);
        assertEquals("2023-07-02", secondDto.getUpdateDate());
        assertEquals(userId, secondDto.getUserId());

        verify(preparedStatement, times(1)).executeQuery();

        verify(preparedStatement).setInt(1, userId);
        verify(preparedStatement).setString(2, startDate);
        verify(preparedStatement).setString(3, endDate);


    }


    @Test
        public void getWeightHistoryByPeriodTest_InvalidDates() {
            String startDate = "";
            String endDate = "2023-07-22";
            int userId = 1;

            assertThrows(SQLException.class, () -> weightHistoryDao.getWeightHistoryByPeriod(startDate, endDate, userId));
        }
}
