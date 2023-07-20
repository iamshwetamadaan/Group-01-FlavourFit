package com.flavourfit.Recipes;

public class RecipeDto {
    private int recipeId;
    private String recipeName;
    private long recipeDescription;
    private String types;

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public long getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(long recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "RecipeDto{" +
                "recipeId=" + recipeId +
                ", recipeName='" + recipeName + '\'' +
                ", recipeDescription=" + recipeDescription +
                ", types='" + types + '\'' +
                '}';
    }
}
