package com.flavourfit.Resources;

import com.flavourfit.Exceptions.RecipeExceptions;
import com.flavourfit.Recipes.CompleteRecipeDto;
import com.flavourfit.Recipes.Ingredients.IngredientDto;
import com.flavourfit.Recipes.RecipeDto;
import com.flavourfit.User.UserDto;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public final class Helpers {
    public static Properties getAppProperties() throws IOException {
        Properties appProperties = new Properties();
        InputStream stream = new FileInputStream(Paths.get(Constants.PROPERTIES_FILE_URL).toString());
        appProperties.load(stream);
        return appProperties;
    }

    public static boolean isValidUser(UserDto user) {
        if (user == null) {
            return false;
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return false;
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return false;
        }

        return true;
    }

    public static boolean isRecipeValid(CompleteRecipeDto completeRecipeDto) throws RecipeExceptions {
        RecipeDto recipe = completeRecipeDto.getRecipe();
        List<IngredientDto> ingredients = completeRecipeDto.getIngredients();

        if (recipe == null) {
            throw new RecipeExceptions("Invalid recipe");
        }

        if (recipe.getRecipeName() == null || recipe.getRecipeName().isEmpty()) {
            throw new RecipeExceptions("Invalid recipe. Does not have a valid name.");
        }

        if (ingredients != null && !ingredients.isEmpty()) {
            for (IngredientDto ingredient : ingredients) {
                if (ingredient.getIngredientName() == null || ingredient.getIngredientName().isEmpty()) {
                    throw new RecipeExceptions("Invalid recipe. Invalid ingredients in recipe.");
                }
            }
        }

        return true;
    }

}
