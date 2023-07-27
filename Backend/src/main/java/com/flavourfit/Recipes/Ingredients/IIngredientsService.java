package com.flavourfit.Recipes.Ingredients;

import com.flavourfit.Exceptions.RecipeExceptions;

import java.util.List;

public interface IIngredientsService {
    void addIngredients(List<IngredientDto> ingredients, int recipeId) throws RecipeExceptions;

    List<IngredientDto> fetchRecipeIngredients(int recipeId) throws RecipeExceptions;

    void updateIngredients(List<IngredientDto> ingredients,int recipeId) throws RecipeExceptions;
}
