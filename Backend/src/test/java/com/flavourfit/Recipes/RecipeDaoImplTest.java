package com.flavourfit.Recipes;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Feeds.FeedDaoImpl;
import com.flavourfit.ResponsesDTO.SavedRecipesResponse;
import com.flavourfit.Trackers.Calories.CalorieHistoryDaoImpl;
import com.flavourfit.Trackers.Calories.CalorieHistoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeDaoImplTest {
    @Mock
    private DatabaseManagerImpl database;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private RecipeDaoImpl recipeDao;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        reset(database,connection,preparedStatement,resultSet,statement);
        when(database.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        recipeDao = new RecipeDaoImpl(database);
    }

    @Test
    void getAllRecipesTypesTest() throws SQLException {
        // Mock the behavior of statement.executeQuery()
        when(resultSet.next()).thenReturn(true, true,false); // Simulating two rows in the ResultSet
        when(resultSet.getString("types")).thenReturn("Breakfast", "Lunch");
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        List<String> expected = List.of("Breakfast", "Lunch");
        List<String> result = recipeDao.getAllRecipesTypes();

        assertEquals(expected, result);
    }

    @Test
    void getRecipesByUserTest() throws SQLException {
        int id = 7;
        int count = 5;

        assertThrows(SQLException.class, () -> recipeDao.getRecipesByUser(id, 0));
        when(resultSet.getString("recipe_name")).thenReturn("Recipe 1");
        when(resultSet.getInt("recipe_id")).thenReturn(1);
        when(resultSet.getString("recipe_description")).thenReturn("Description 1");
        when(resultSet.getString("types")).thenReturn("Type 1");

        ArrayList<Object> recipes = recipeDao.getRecipesByUser(count, id);

        assertEquals(0, recipes.size());
    }

    @Test
    void getFilteredRecipesByUserTest() throws SQLException {
        int userId = 7;
        int count = 5; // Set a non-zero count value

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        ArrayList<Object> expectedRecipes = new ArrayList<>();
        // Add some test data to the expectedRecipes list
        SavedRecipesResponse recipe1 = new SavedRecipesResponse();
        recipe1.setRecipeId(1);
        recipe1.setRecipeName("Recipe 1");
        recipe1.setDescription("Description for Recipe 1");
        recipe1.setTypes("Type 1, Type 2");
        expectedRecipes.add(recipe1);

        when(resultSet.next()).thenReturn(true, false); // Simulate a single result in the result set
        when(resultSet.getInt("recipe_id")).thenReturn(1);
        when(resultSet.getString("recipe_name")).thenReturn("Recipe 1");
        when(resultSet.getString("recipe_description")).thenReturn("Description for Recipe 1");
        when(resultSet.getString("types")).thenReturn("Type 1, Type 2");

        // Act
        ArrayList<Object> resultRecipes = recipeDao.getRecipesByUser(userId, count);

        // Assert
        assertEquals(1, resultRecipes.size());
    }

    @Test
    void updateRecipeTest() {

    }

}