package com.flavourfit.Recipes;

import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.ResponsesDTO.SavedRecipesResponse;
import com.flavourfit.Trackers.Calories.CalorieHistoryDaoImpl;
import com.flavourfit.Trackers.Calories.CalorieHistoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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

    @Mock
    private PreparedStatement preparedStatement;

    @BeforeEach
    public void initMocks() throws SQLException {
        MockitoAnnotations.initMocks(this);
        when(database.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
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

    @Test
    void getFilteredRecipesByUserTest() throws SQLException{

        HashMap<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("keyword","");
        requestBody.put("count",0);
//        ResultSet rs = Mockito.mock(ResultSet.class);
//        when(rs.next()).thenReturn(true).thenReturn(false);
//        when(rs.getString("recipe_name")).thenReturn("Recipe 1");
//        when(rs.getInt("recipe_id")).thenReturn(1);
//        when(rs.getString("recipe_description")).thenReturn("Lorem Epsum");
//        when(rs.getString("types")).thenReturn("Chicken");
//        when(preparedStatement.executeQuery()).thenReturn(rs);

        assertThrows(IllegalArgumentException.class, () -> recipeDao.getFilteredRecipesByUser(7, requestBody));

        requestBody.put("keyword","Lorem");
        requestBody.put("count",5);
        assertEquals(0, recipeDao.getFilteredRecipesByUser(99,requestBody).size());
    }
}