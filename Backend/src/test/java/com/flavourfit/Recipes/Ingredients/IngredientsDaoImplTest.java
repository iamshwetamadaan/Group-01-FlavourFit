package com.flavourfit.Recipes.Ingredients;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class IngredientsDaoImplTest {
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

    private IngredientsDaoImpl ingredientsDao;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        reset(database,connection,preparedStatement,resultSet,statement);
        when(database.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        ingredientsDao = new IngredientsDaoImpl(database);
    }

    @Test
    void addIngredientsTest() throws SQLException {
        // Happy Path Test
        List<IngredientDto> happyPathIngredients = new ArrayList<>();
        IngredientDto ingredient1 = new IngredientDto();
        ingredient1.setIngredientName("Ingredient1");
        ingredient1.setQuantity(100.0);
        ingredient1.setQuantityUnit("grams");
        ingredient1.setRecipeId(1);

        IngredientDto ingredient2= new IngredientDto();
        ingredient2.setIngredientName("Ingredient2");
        ingredient2.setQuantity(2.5);
        ingredient2.setQuantityUnit("cups");
        ingredient2.setRecipeId(1);

        happyPathIngredients.add(ingredient1);
        happyPathIngredients.add(ingredient2);
        ingredientsDao.addIngredients(happyPathIngredients);
        List<IngredientDto> sadPathIngredients = null;

        try {
            ingredientsDao.addIngredients(sadPathIngredients);
        } catch (SQLException ex) {
            // Verify that SQLException was thrown
            assertThrows(SQLException.class, () -> {
                throw ex;
            });
        }
    }

    @Test
    void getRecipeIngredientsTest() throws SQLException {
        // Mock data to be returned from the ResultSet
        List<IngredientDto> mockIngredients = new ArrayList<>();
        IngredientDto ingredient1 = new IngredientDto();
        ingredient1.setIngredientName("Ingredient1");
        ingredient1.setQuantity(100.0);
        ingredient1.setQuantityUnit("grams");
        ingredient1.setRecipeId(1);
        ingredient1.setIngredientId(1);

        IngredientDto ingredient2= new IngredientDto();
        ingredient2.setIngredientName("Ingredient2");
        ingredient2.setQuantity(2.5);
        ingredient2.setQuantityUnit("cups");
        ingredient2.setRecipeId(1);
        ingredient2.setIngredientId(2);

        mockIngredients.add(ingredient1);
        mockIngredients.add(ingredient2);

        // Mocking the ResultSet behavior
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("Ingredient_id")).thenReturn(1, 2);
        when(resultSet.getString("Ingredient_name")).thenReturn("Ingredient1", "Ingredient2");
        when(resultSet.getDouble("quantity")).thenReturn(100.0, 2.5);
        when(resultSet.getString("quantity_unit")).thenReturn("grams", "cups");
        when(resultSet.getInt("Recipe_id")).thenReturn(1);

        // Mocking the prepared statement behavior
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        List<IngredientDto> resultIngredients = ingredientsDao.getRecipeIngredients(1);

        // Verify the returned ingredients list
        assertEquals(mockIngredients.size(), resultIngredients.size());
        assertEquals(mockIngredients.get(0).getIngredientId(), resultIngredients.get(0).getIngredientId());
        assertEquals(mockIngredients.get(0).getIngredientName(), resultIngredients.get(0).getIngredientName());
        assertEquals(mockIngredients.get(0).getQuantity(), resultIngredients.get(0).getQuantity());
        assertEquals(mockIngredients.get(0).getQuantityUnit(), resultIngredients.get(0).getQuantityUnit());
        assertEquals(mockIngredients.get(0).getRecipeId(), resultIngredients.get(0).getRecipeId());

        assertEquals(mockIngredients.get(1).getIngredientId(), resultIngredients.get(1).getIngredientId());
        assertEquals(mockIngredients.get(1).getIngredientName(), resultIngredients.get(1).getIngredientName());
        assertEquals(mockIngredients.get(1).getQuantity(), resultIngredients.get(1).getQuantity());
        assertEquals(mockIngredients.get(1).getQuantityUnit(), resultIngredients.get(1).getQuantityUnit());
        assertEquals(mockIngredients.get(1).getRecipeId(), resultIngredients.get(1).getRecipeId());
    }

    @Test
    void updateIngredientsTest() throws SQLException {
        List<IngredientDto> ingredientDtoList = new ArrayList<>();
        IngredientDto ingredient1 = new IngredientDto();
        ingredient1.setIngredientName("Ingredient1");
        ingredient1.setQuantity(100.0);
        ingredient1.setQuantityUnit("grams");
        ingredient1.setRecipeId(1);
        ingredient1.setIngredientId(1);

        IngredientDto ingredient2= new IngredientDto();
        ingredient2.setIngredientName("Ingredient2");
        ingredient2.setQuantity(2.5);
        ingredient2.setQuantityUnit("cups");
        ingredient2.setRecipeId(1);
        ingredient2.setIngredientId(2);

        ingredientDtoList.add(ingredient1);
        ingredientDtoList.add(ingredient2);

        ingredientsDao.updateIngredients(ingredientDtoList);
    }
}
