package com.flavourfit.Recipes;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Feeds.FeedDaoImpl;
import com.flavourfit.Recipes.Ingredients.IngredientDto;
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

    @Test
    public void getRecipeByIdTest() throws Exception {
        int recipeID = 1;

        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("recipe_id")).thenReturn(recipeID);
        when(resultSet.getString("recipe_name")).thenReturn("Potato Bake");
        when(resultSet.getString("recipe_description")).thenReturn("Description for Recipe 1");
        when(resultSet.getString("types")).thenReturn("Vegetarian");

        RecipeDto recipe = recipeDao.getRecipeById(recipeID);
        assertNotNull(recipe);
    }

    @Test
    public void getRecipeIngredientsTest() throws Exception {
        int recipeID = 1;
        int ingredientID = 3;

        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, true, false);
        when(resultSet.getInt("Ingredient_id")).thenReturn(ingredientID);
        when(resultSet.getString("Ingredient_name")).thenReturn("Potato");
        when(resultSet.getInt("Recipe_id")).thenReturn(recipeID);
        when(resultSet.getDouble("quantity")).thenReturn(5.0);
        when(resultSet.getString("quantity_unit")).thenReturn("kg");
        when(resultSet.getInt("Ingredient_id")).thenReturn(ingredientID+1);
        when(resultSet.getString("Ingredient_name")).thenReturn("Cream");
        when(resultSet.getInt("Recipe_id")).thenReturn(recipeID);
        when(resultSet.getDouble("quantity")).thenReturn(3.0);
        when(resultSet.getString("quantity_unit")).thenReturn("cups");
        when(resultSet.getInt("Ingredient_id")).thenReturn(ingredientID+2);
        when(resultSet.getString("Ingredient_name")).thenReturn("Garlic");
        when(resultSet.getInt("Recipe_id")).thenReturn(recipeID);
        when(resultSet.getDouble("quantity")).thenReturn(6.0);
        when(resultSet.getString("quantity_unit")).thenReturn("g");

        List<IngredientDto> ingredientList = recipeDao.getRecipeIngredients(recipeID);
        assertNotNull(ingredientList);
    }

    @Test
    public void testAddRecipe() throws SQLException {
        // Happy path
        RecipeDto happyPathRecipe = new RecipeDto();
        happyPathRecipe.setRecipeName("Pasta");
        happyPathRecipe.setRecipeDescription("Delicious pasta");
        happyPathRecipe.setTypes("Italian");
        happyPathRecipe.setEditable(true);

        ResultSet generatedKeys = mock(ResultSet.class);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeys);

        when(generatedKeys.next()).thenReturn(true,false);
        when(generatedKeys.getLong(1)).thenReturn(1L);

        RecipeDto addedRecipe = recipeDao.addRecipe(happyPathRecipe, 1);
        assertEquals(1, addedRecipe.getRecipeId());

        try {
            recipeDao.addRecipe(null, 1);
            fail("Expected SQLException");
        } catch (SQLException e) {
            assertEquals("Invalid recipe", e.getMessage());
        }
    }

}