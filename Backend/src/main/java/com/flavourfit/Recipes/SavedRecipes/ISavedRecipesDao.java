
package com.flavourfit.Recipes.SavedRecipes;

import java.sql.SQLException;

public interface ISavedRecipesDao {
    SavedRecipesDto addSavedRecipe(int recipeId, int userId) throws SQLException;
}

