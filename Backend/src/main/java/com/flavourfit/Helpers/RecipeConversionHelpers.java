package com.flavourfit.Helpers;

import com.flavourfit.Recipes.Ingredients.IngredientDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeConversionHelpers {
    /**
     * Method to get the scale quantity in ingredients list
     *
     * @return -- List<IngredientDto> of scaled ingredients for a recipe
     */
    public static List<IngredientDto> scaleIngredients(double scalingFactor, List<IngredientDto> ingredientList) {
        List<IngredientDto> scaledIngredients = new ArrayList<>();

        for(IngredientDto ingredient : ingredientList) {
            double ingredientQuantity = ingredient.getQuantity();
            double scaledQuantity = scalingFactor * ingredientQuantity;

            ingredient.setQuantity(scaledQuantity);

            scaledIngredients.add(ingredient);
        }
        return scaledIngredients;
    }

    public static List<IngredientDto> metricToImperial(List<IngredientDto> ingredientList) {
        List<IngredientDto> convertedIngredients = new ArrayList<>();

        String filePath = "src/main/java/com/flavourfit/Helpers/Conversion.json";

        try {
            // Parse the JSON file
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(filePath));

            JSONObject jsonObject = (JSONObject) obj;
            //JSONObject conversions = (JSONObject) jsonObject.get("Conversion_metrics");
            JSONArray conversionArray = (JSONArray) jsonObject.get("Conversion_metrics");

            for(IngredientDto ingredient : ingredientList) {

                for(Object eachConversion : conversionArray) {

                    JSONObject conversion = (JSONObject) eachConversion;

                    String type1 = (String) conversion.get("Type1");
                    String type2 = (String) conversion.get("Type2");

                    double quantity1 = (Double) conversion.get("Quantity1");
                    double quantity2 = (Double) conversion.get("Quantity2");

                    double actualQuantity = ingredient.getQuantity();

                    if (type1.equals(ingredient.getQuantityUnit())) {

                        ingredient.setQuantity((actualQuantity * quantity2) / (quantity1));
                        ingredient.setQuantityUnit(type2);

                    }
                }
                convertedIngredients.add(ingredient);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return convertedIngredients;
    }

    public static List<IngredientDto> imperialToMetric(List<IngredientDto> ingredientList) {
        List<IngredientDto> convertedIngredients = new ArrayList<>();

        String filePath = "src/main/java/com/flavourfit/Helpers/Conversion.json";

        try {
            // Parse the JSON file
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(filePath));

            JSONObject jsonObject = (JSONObject) obj;
            //JSONObject conversions = (JSONObject) jsonObject.get("Conversion_metrics");
            JSONArray conversionArray = (JSONArray) jsonObject.get("Conversion_metrics");

            for(IngredientDto ingredient : ingredientList) {

                for(Object eachConversion : conversionArray) {

                    JSONObject conversion = (JSONObject) eachConversion;

                    String type1 = (String) conversion.get("Type1");
                    String type2 = (String) conversion.get("Type2");

                    double quantity1 = (Double) conversion.get("Quantity1");
                    double quantity2 = (Double) conversion.get("Quantity2");

                    double actualQuantity = ingredient.getQuantity();

                    if (type2.equals(ingredient.getQuantityUnit())) {

                        ingredient.setQuantity((actualQuantity * quantity1) / (quantity2));
                        ingredient.setQuantityUnit(type1);
                    }
                }
                convertedIngredients.add(ingredient);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return convertedIngredients;
    }
}
