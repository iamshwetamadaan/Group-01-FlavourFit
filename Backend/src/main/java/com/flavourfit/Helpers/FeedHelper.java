
package com.flavourfit.Helpers;

import com.flavourfit.Exceptions.RecipeExceptions;
import com.flavourfit.Feeds.FeedDto;
import com.flavourfit.Recipes.Ingredients.IngredientDto;
import com.flavourfit.Recipes.RecipeDto;

import java.util.List;

public class FeedHelper {
    public static FeedDto convertRecipeToFeed(RecipeDto recipe, List<IngredientDto> ingredients) {
        if (recipe.getRecipeId() == 0) {
            throw new RecipeExceptions("Invalid recipe");
        }

        FeedDto feed = new FeedDto();

        StringBuilder recipeStr = new StringBuilder();

        recipeStr.append(recipe.getRecipeName()).append("\n");
        recipeStr.append(recipe.getTypes()).append("\n");
        if (ingredients.size() > 0) {
            recipeStr.append("\n Ingredients: \n");
            for (IngredientDto ingredient : ingredients) {
                recipeStr.append(ingredient.getIngredientName()).append("\t");
                recipeStr.append(ingredient.getQuantity()).append(" ");
                if(ingredient.getQuantityUnit()!=null) {
                    recipeStr.append(ingredient.getQuantityUnit());
                }
                recipeStr.append("\n");
            }
        }

        recipeStr.append("\n").append(recipe.getRecipeDescription());

        feed.setFeedContent(recipeStr.toString());
        feed.setLikeCount(0);

        return feed;
    }
}

