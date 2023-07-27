package com.flavourfit.Recipes;

import com.flavourfit.Recipes.Ingredients.IngredientDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IRecipeDao {
    public List<String> getAllRecipesTypes() throws SQLException;

    public ArrayList<Object> getRecipesByUser(int id,int count) throws SQLException;

    public ArrayList<Object> getFilteredRecipesByUser(int userID , HashMap<String, Object> requestbody) throws SQLException;

    RecipeDto addRecipe(RecipeDto recipeDto, int userId) throws SQLException;

    public RecipeDto getRecipeById(int recipeId) throws SQLException;

    public List<IngredientDto> getRecipeIngredients(int recipeId) throws SQLException;

    void updateRecipe(RecipeDto recipeDto) throws SQLException;
}
