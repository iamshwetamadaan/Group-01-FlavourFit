package com.flavourfit.Recipes;

import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeDaoImplTest {
    @Mock
    private IDatabaseManager databaseManager;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private PreparedStatement preparedStatement;
    @InjectMocks
    private RecipeDaoImpl recipeDao;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Mock the behavior of IDatabaseManager.getConnection()
        when(databaseManager.getConnection()).thenReturn(connection);

        // Mock the behavior of connection.createStatement()
        when(connection.createStatement()).thenReturn(statement);

        // Mock the behavior of connection.prepareStatement()
        String query = "INSERT INTO Recipes (Recipe_name, Recipe_description, Types, editable) values (?, ?, ?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);

    }

    @Test
    void getAllRecipesTypesTest() throws SQLException {
        // Mock the behavior of statement.executeQuery()
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, true, false); // Simulating two rows in the ResultSet
        when(resultSet.getString("types")).thenReturn("Breakfast", "Lunch");
        when(statement.executeQuery("SELECT DISTINCT(types) FROM Recipes;")).thenReturn(resultSet);

        List<String> expected = List.of("Breakfast", "Lunch");
        // Call the method under test
        List<String> result = recipeDao.getAllRecipesTypes();

        // Assert the result
        assertEquals(expected, result);

        // Reset for the next scenario
        reset(databaseManager, connection, preparedStatement, resultSet);
        setUp();

        // Mock the behavior of databaseManager.getConnection() to return null
        when(databaseManager.getConnection()).thenReturn([]);

        // Assert that an SQLException is thrown when trying to get all recipe types
        assertEquals(null, recipeDao.getAllRecipesTypes());
    }
}