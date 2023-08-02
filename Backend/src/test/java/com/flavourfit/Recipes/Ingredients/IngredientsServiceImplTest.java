package com.flavourfit.Recipes.Ingredients;

import com.flavourfit.Exceptions.RecipeExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class IngredientsServiceImplTest {

    @Mock
    private IIngredientsDao ingredientsDao;

    @InjectMocks
    private IngredientsServiceImpl ingredientsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void dddIngredientsTest() throws RecipeExceptions, SQLException {
        List<IngredientDto> ingredients = new ArrayList<>();
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
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);

        int recipeId = 1;

        doNothing().when(ingredientsDao).addIngredients(any(List.class));

        ingredientsService.addIngredients(ingredients, recipeId);

        verify(ingredientsDao, times(1)).addIngredients(ingredients);

        ingredients = null;

        try {
            ingredientsService.addIngredients(ingredients, recipeId);
        } catch (RecipeExceptions e) {
            assert e.getMessage().equals("Invalid ingredients list");
        }
    }

    @Test
    public void fetchRecipeIngredientsTest() throws RecipeExceptions, SQLException {
        int recipeId = 1;
        List<IngredientDto> mockIngredients = new ArrayList<>();
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
        mockIngredients.add(ingredient1);
        mockIngredients.add(ingredient1);

        when(ingredientsDao.getRecipeIngredients(recipeId)).thenReturn(mockIngredients);

        List<IngredientDto> result = ingredientsService.fetchRecipeIngredients(recipeId);

        assertEquals(mockIngredients, result);
        verify(ingredientsDao, times(1)).getRecipeIngredients(recipeId);

        when(ingredientsDao.getRecipeIngredients(recipeId)).thenThrow(new SQLException("Database connection error"));

        try {
            ingredientsService.fetchRecipeIngredients(2);
        } catch (RecipeExceptions e) {
            assertEquals("Database connection error", e.getMessage());
        }

        verify(ingredientsDao, times(1)).getRecipeIngredients(recipeId);
    }

    @Test
    public void updateIngredientsHappyPathTest() throws RecipeExceptions, SQLException {
        // Happy path test
        int recipeId = 1;
        List<IngredientDto> ingredients = new ArrayList<>();
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
        ingredients.add(ingredient1);
        ingredients.add(ingredient1);

        IngredientDto ingredient3 = new IngredientDto();
        ingredient3.setIngredientName("Old Ingredient 1");
        ingredient3.setQuantity(200.0);
        ingredient3.setQuantityUnit("grams");
        ingredient3.setRecipeId(1);
        ingredient3.setIngredientId(1);
        List<IngredientDto> oldIngredients = new ArrayList<>();
        oldIngredients.add(ingredient3);

        when(ingredientsDao.getRecipeIngredients(recipeId)).thenReturn(oldIngredients);
        doNothing().when(ingredientsDao).updateIngredients(any(List.class));
        doNothing().when(ingredientsDao).addIngredients(any(List.class));

        ingredientsService.updateIngredients(ingredients, recipeId);
    }

}
