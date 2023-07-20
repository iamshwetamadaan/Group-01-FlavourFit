package com.flavourfit.Recipes;

import com.flavourfit.Exceptions.RecipeExceptions;
import com.flavourfit.Recipes.Ingredients.IngredientDto;

import java.sql.SQLException;
import java.util.List;

public interface IRecipeService {
    public List<String> fetchAllRecipeTypes() throws SQLException;

    CompleteRecipeDto recordRecipe(CompleteRecipeDto completeRecipe, int userId) throws
            RecipeExceptions;
}
