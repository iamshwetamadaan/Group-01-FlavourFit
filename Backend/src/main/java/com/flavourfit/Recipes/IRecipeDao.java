package com.flavourfit.Recipes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IRecipeDao {
    public List<String> getAllRecipesTypes() throws SQLException;

    public ArrayList<Object> getRecipesByUser(int id,int count) throws SQLException;

    public ArrayList<Object> getFilteredRecipesByUser(int userID , HashMap<String, Object> requestbody) throws SQLException;


    RecipeDto addRecipe(RecipeDto recipeDto, int userId) throws SQLException;
}
