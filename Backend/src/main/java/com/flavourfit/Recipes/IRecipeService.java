package com.flavourfit.Recipes;

import com.flavourfit.Exceptions.RecipeExceptions;
import com.flavourfit.Recipes.Ingredients.IngredientDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IRecipeService {
    public List<String> fetchAllRecipeTypes() throws SQLException;

    public ArrayList<Object> getRecipesByUser(int count, int userId) throws SQLException;

    public ArrayList<Object> getFilteredRecipesByUser(int userId, HashMap<String, Object> requestBody) throws SQLException;

    CompleteRecipeDto recordRecipe(CompleteRecipeDto completeRecipe, int userId) throws
            RecipeExceptions;

    CompleteRecipeDto convertRecipe(int recipeId, double scale, String system) throws RecipeExceptions;

    CompleteRecipeDto fetchRecipeByRecipeId(int recipeId) throws RecipeExceptions;

    CompleteRecipeDto updateRecipe(CompleteRecipeDto completeRecipeDto) throws RecipeExceptions;
}
