package com.flavourfit.Recipes;

import com.flavourfit.Exceptions.RecipeExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {
    @Mock
    private IRecipeDao recipeDao;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchAllRecipeTypesTest() throws SQLException {
        // Success Case
        // Mock the behavior of recipeDao.getAllRecipesTypes()
        List<String> mockRecipeTypes = List.of("Breakfast", "Lunch");
        when(recipeDao.getAllRecipesTypes()).thenReturn(mockRecipeTypes);

        // Call the method
        List<String> result = recipeService.fetchAllRecipeTypes();
        // Successfully fetched recipes from database
        assertEquals(mockRecipeTypes, result);

        // Failure Case
        // Database is not connected
        when(recipeDao.getAllRecipesTypes()).thenThrow(new SQLException("Database error"));
        assertThrows(SQLException.class, () -> recipeService.fetchAllRecipeTypes());
    }
}