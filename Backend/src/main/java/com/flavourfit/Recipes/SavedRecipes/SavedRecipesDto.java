package com.flavourfit.Recipes.SavedRecipes;

public class SavedRecipesDto {
    private int recipeId;
    private int userId;

    public SavedRecipesDto(int recipeId, int userId) {
        this.recipeId = recipeId;
        this.userId = userId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

