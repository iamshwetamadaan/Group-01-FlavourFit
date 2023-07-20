package com.flavourfit.Recipes;

import com.flavourfit.Recipes.Ingredients.IngredientDto;

import java.util.List;

public class CompleteRecipeDto {
    private RecipeDto recipe;
    private List<IngredientDto> ingredients;

    public RecipeDto getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeDto recipe) {
        this.recipe = recipe;
    }

    public List<IngredientDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientDto> ingredients) {
        this.ingredients = ingredients;
    }
}
