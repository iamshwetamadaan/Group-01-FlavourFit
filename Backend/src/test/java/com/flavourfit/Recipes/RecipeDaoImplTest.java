package com.flavourfit.Recipes;

import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class RecipeDaoImplTest {

    @Mock
    private IDatabaseManager database;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private RecipeDaoImpl recipeDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllRecipesTypes_Success() throws SQLException {
        // Mocking the behavior of database.getConnection()
        when(database.getConnection()).thenReturn(connection);

        // Mocking the behavior of connection.createStatement()
        when(connection.createStatement()).thenReturn(statement);

        // Mocking the behavior of statement.executeQuery()
        String mockType1 = "Breakfast";
        String mockType2 = "Lunch";
        when(resultSet.next()).thenReturn(true, true, false); // Simulating two rows in the ResultSet
        when(resultSet.getString("types")).thenReturn(mockType1, mockType2);
        when(statement.executeQuery("SELECT DISTINCT(types) FROM Recipes;")).thenReturn(resultSet);

        // Calling the method under test
        List<String> result = recipeDao.getAllRecipesTypes();

        // Asserting the result
        List<String> expected = List.of(mockType1, mockType2);
        assertEquals(expected, result);
    }

    @Test
    void testGetAllRecipesTypes_ConnectionError() throws SQLException {
        // Mocking the behavior of database.getConnection() to return null
        when(database.getConnection()).thenReturn(null);

        // Asserting that an SQLException is thrown when trying to get all recipe types
        assertThrows(SQLException.class, () -> recipeDao.getAllRecipesTypes());
    }

    @Test
    void testGetAllRecipesTypes_QueryError() throws SQLException {
        // Mocking the behavior of database.getConnection()
        when(database.getConnection()).thenReturn(connection);

        // Mocking the behavior of connection.createStatement()
        when(connection.createStatement()).thenReturn(statement);

        // Mocking the behavior of statement.executeQuery() to throw an SQLException
        when(statement.executeQuery("SELECT DISTINCT(types) FROM Recipes;")).thenThrow(new SQLException("Query failed"));

        // Asserting that an SQLException is thrown when trying to get all recipe types
        assertThrows(SQLException.class, () -> recipeDao.getAllRecipesTypes());
    }
}
