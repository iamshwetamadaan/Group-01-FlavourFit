package com.flavourfit.Recipes;

import com.flavourfit.Exceptions.RecipeExceptions;
import com.flavourfit.Recipes.Ingredients.IngredientDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IRecipeService {
    public List<String> fetchAllRecipeTypes() throws SQLException;

    public ArrayList<Object> getRecipesByUser(int count, int userId) throws SQLException;

    CompleteRecipeDto recordRecipe(CompleteRecipeDto completeRecipe, int userId) throws
            RecipeExceptions;
}
