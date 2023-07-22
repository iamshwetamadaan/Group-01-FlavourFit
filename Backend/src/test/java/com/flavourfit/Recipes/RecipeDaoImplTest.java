package com.flavourfit.Recipes;

import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Trackers.Calories.CalorieHistoryDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class
RecipeDaoImplTest {

    @InjectMocks
    private RecipeDaoImpl recipeDao;

    @Mock
    private IDatabaseManager database;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    public void initMocks() throws SQLException {
        MockitoAnnotations.initMocks(this);
        when(database.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
    }

    @Test
    void getAllRecipesTypes() {
    }

    @Test
    void getRecipesByUserTest() throws SQLException{
        int id=7;
        int count=5;

        assertThrows(SQLException.class, () -> recipeDao.getRecipesByUser(id,0));
        when(resultSet.getString("recipe_name")).thenReturn("Recipe 1");
        when(resultSet.getInt("recipe_id")).thenReturn(1);
        when(resultSet.getString("recipe_description")).thenReturn("Description 1");
        when(resultSet.getString("types")).thenReturn("Type 1");

        // Act
        ArrayList<Object> recipes = recipeDao.getRecipesByUser(count, id);

        // Assert
        assertEquals(1, recipes.size());

    }
}