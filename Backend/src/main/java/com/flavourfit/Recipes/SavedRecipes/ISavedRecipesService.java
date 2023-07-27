
package com.flavourfit.Recipes.SavedRecipes;

import com.flavourfit.Exceptions.RecipeExceptions;

public interface ISavedRecipesService {
    void saveRecipe(int recipeId, int userId) throws RecipeExceptions;
}

