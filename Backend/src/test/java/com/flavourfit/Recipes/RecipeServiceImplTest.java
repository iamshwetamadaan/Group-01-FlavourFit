package com.flavourfit.Recipes;

import com.flavourfit.ResponsesDTO.SavedRecipesResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceImplTest {

    @InjectMocks
    RecipeServiceImpl recipeService;

    @Mock
    IRecipeDao recipeDao;

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
}
