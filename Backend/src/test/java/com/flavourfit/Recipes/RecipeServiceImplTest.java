package com.flavourfit.Recipes;

import com.flavourfit.Exceptions.RecipeExceptions;
import com.flavourfit.Recipes.Ingredients.IngredientDto;
import com.flavourfit.ResponsesDTO.SavedRecipesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Test
    public void getRecipesByUserTest() throws SQLException {
        SavedRecipesResponse savedRecipe = new SavedRecipesResponse();
        savedRecipe.setTypes("Veg");
        savedRecipe.setRecipeName("ABC");
        savedRecipe.setRecipeId(1);
        savedRecipe.setDescription("Description 1");

        ArrayList<Object> recipes = new ArrayList<>();
        recipes.add(savedRecipe);

        when(recipeService.getRecipesByUser(1,5)).thenReturn(recipes);
        assertEquals(1,recipeService.getRecipesByUser(1,5).size());
    }

    @Test
    public void getFilteredRecipesByUserTest() throws SQLException {
        SavedRecipesResponse savedRecipe = new SavedRecipesResponse();
        savedRecipe.setTypes("Veg");
        savedRecipe.setRecipeName("ABC");
        savedRecipe.setRecipeId(1);
        savedRecipe.setDescription("Description 1");

        HashMap<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("keyword","ABC");
        requestBody.put("count",3);

        ArrayList<Object> recipes = new ArrayList<>();
        recipes.add(savedRecipe);

        when(recipeService.getFilteredRecipesByUser(1,requestBody)).thenReturn(recipes);
        assertEquals(1,recipeService.getFilteredRecipesByUser(1,requestBody).size());
    }

    @Test
    public void convertRecipeTest() throws Exception {
        int recipeId = 2;
        double scale = 0.5;
        String system = "imperial";

        RecipeDto recipe = new RecipeDto();
        recipe.setRecipeId(recipeId);

        List<IngredientDto> expectedConvertedIngredients = new ArrayList<>();

        when(recipeDao.getRecipeById(recipeId)).thenReturn(recipe);
        when(recipeDao.getRecipeIngredients(recipeId)).thenReturn(expectedConvertedIngredients);

        // Act
        CompleteRecipeDto result = recipeService.convertRecipe(recipeId, scale, system);

        assertNotNull(result);
    }
}