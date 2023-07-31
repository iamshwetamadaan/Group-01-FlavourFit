package com.flavourfit.Recipes;

import com.flavourfit.Exceptions.RecipeExceptions;
import com.flavourfit.Recipes.Ingredients.IIngredientsService;
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
    @Mock
    private IIngredientsService ingredientsService;

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
    public void updateRecipeTest(){
        CompleteRecipeDto completeRecipeDto = new CompleteRecipeDto();
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setRecipeId(1);
        recipeDto.setRecipeName("name");
        recipeDto.setRecipeDescription("description");
        recipeDto.setTypes("types");
        recipeDto.setEditable(true);
        completeRecipeDto.setRecipe(recipeDto);

        List<IngredientDto> ingredients = new ArrayList<>();
        IngredientDto ingredient1 = new IngredientDto();
        ingredient1.setIngredientName("Ingredient1");
        ingredient1.setQuantity(100);
        ingredient1.setQuantityUnit("g");

        IngredientDto ingredient2 = new IngredientDto();
        ingredient1.setIngredientName("Ingredient2");
        ingredient1.setQuantity(2);
        ingredient1.setQuantityUnit("cups");

        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        completeRecipeDto.setIngredients(ingredients);

        try {
            recipeDto = completeRecipeDto.getRecipe();
            ingredients = completeRecipeDto.getIngredients();
            //fail("Expected RecipeExceptions but no exception was thrown.");
        } catch (RecipeExceptions e) {
            throw new RecipeExceptions("Recipe not updated");
        }

        try {
            when(recipeDao.getRecipeById(recipeDto.getRecipeId())).thenReturn(recipeDto);
            when(ingredientsService.fetchRecipeIngredients(recipeDto.getRecipeId())).thenReturn(ingredients);

            recipeService.updateRecipe(completeRecipeDto);

            verify(recipeDao).updateRecipe(recipeDto);
            verify(ingredientsService).updateIngredients(ingredients, recipeDto.getRecipeId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}