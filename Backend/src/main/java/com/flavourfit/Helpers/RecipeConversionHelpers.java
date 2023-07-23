package com.flavourfit.Helpers;

import com.flavourfit.Recipes.Ingredients.IngredientDto;

import java.util.ArrayList;
import java.util.List;

public class RecipeConversionHelpers {
    /**
     * Method to get the scale quantity in ingredients list
     *
     * @return -- List<IngredientDto> of scaled ingredients for a recipe
     */
    public List<IngredientDto> scaleIngredients(double scalingFactor, List<IngredientDto> ingredientList) {
        List<IngredientDto> scaledIngredients = new ArrayList<>();

        for(IngredientDto ingredient : ingredientList) {
            double ingredientQuantity = ingredient.getQuantity();
            double scaledQuantity = scalingFactor * ingredientQuantity;

            ingredient.setQuantity(scaledQuantity);

            scaledIngredients.add(ingredient);
        }
        return scaledIngredients;
    }
}
