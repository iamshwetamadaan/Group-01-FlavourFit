package com.flavourfit.Recipes.Ingredients;

import java.sql.SQLException;
import java.util.List;

public interface IIngredientsDao {
    void addIngredients(List<IngredientDto> ingredients) throws SQLException;

    List<IngredientDto> getRecipeIngredients(int recipeId) throws SQLException;

    void updateIngredients(List<IngredientDto> ingredients) throws SQLException;
}
