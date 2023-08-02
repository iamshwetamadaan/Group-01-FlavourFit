package com.flavourfit.Recipes;

import com.flavourfit.Exceptions.RecipeExceptions;
import com.flavourfit.Helpers.RecipeConversionHelpers;
import com.flavourfit.Recipes.Ingredients.IIngredientsService;
import com.flavourfit.Recipes.Ingredients.IngredientDto;
import com.flavourfit.Recipes.SavedRecipes.ISavedRecipesService;
import com.flavourfit.Resources.Constants;
import com.flavourfit.Resources.Helpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class RecipeServiceImpl implements IRecipeService {

    private static Logger logger = LoggerFactory.getLogger(RecipeServiceImpl.class);


    private final IRecipeDao recipeDao;
    private final IIngredientsService ingredientsService;
    private final ISavedRecipesService savedRecipesService;


    public RecipeServiceImpl(
            IRecipeDao recipeDao, IIngredientsService ingredientsService,
            ISavedRecipesService savedRecipesService
    ) {
        this.recipeDao = recipeDao;
        this.ingredientsService = ingredientsService;
        this.savedRecipesService = savedRecipesService;
    }

    @Override
    public List<String> fetchAllRecipeTypes() throws SQLException {
        logger.info("Started method fetchAllRecipeTypes()");
        return recipeDao.getAllRecipesTypes();
    }

    @Override
    public ArrayList<Object> getRecipesByUser(int count, int userId) throws SQLException {
        logger.info("Started method getRecipesByUser()");
        return recipeDao.getRecipesByUser(userId, count);
    }

    @Override
    public ArrayList<Object> getFilteredRecipesByUser(int id, HashMap<String, Object> requestBody) throws SQLException {
        logger.info("Started method getFilteredRecipesByUser");
        return recipeDao.getFilteredRecipesByUser(id, requestBody);
    }

    @Override
    public CompleteRecipeDto recordRecipe(CompleteRecipeDto completeRecipe, int userId) throws RecipeExceptions {
        if (Helpers.isRecipeValid(completeRecipe)) {
            try {
                completeRecipe.getRecipe().setEditable(true);
                RecipeDto addedRecipe = this.recipeDao.addRecipe(completeRecipe.getRecipe(), userId);
                int recipeId = addedRecipe.getRecipeId();

                List<IngredientDto> ingredients = completeRecipe.getIngredients();
                if (ingredients == null) {
                    ingredients = new ArrayList<>();
                }
                this.ingredientsService.addIngredients(ingredients, recipeId);

                this.savedRecipesService.saveRecipe(recipeId, userId);

                List<IngredientDto> addedIngredients = this.ingredientsService.fetchRecipeIngredients(recipeId);
                CompleteRecipeDto newRecipe = new CompleteRecipeDto();
                newRecipe.setRecipe(addedRecipe);
                newRecipe.setIngredients(addedIngredients);

                return newRecipe;
            } catch (SQLException e) {
                throw new RecipeExceptions(e);
            }
        } else {
            throw new RecipeExceptions("Invalid recipe");
        }
    }

    @Override
    public CompleteRecipeDto convertRecipe(int recipeId, double scale, String system) throws RecipeExceptions {
        logger.info("Started method convertRecipe()");
        CompleteRecipeDto completeRecipe = new CompleteRecipeDto();
        List<IngredientDto> convertedIngredientList = new ArrayList<>();

        try {
            RecipeDto recipe = this.recipeDao.getRecipeById(recipeId);
            List<IngredientDto> ingredientList = this.recipeDao.getRecipeIngredients(recipeId);
            logger.info("Start scaleIngredients() method");

            List<IngredientDto> scaledIngredientList = RecipeConversionHelpers.scaleIngredients(scale, ingredientList);

            logger.info("Got the scaled ingredients");

            if (system.equalsIgnoreCase(Constants.imperialSystem)) {
                convertedIngredientList = RecipeConversionHelpers.metricToImperial(scaledIngredientList);
                logger.info("Got the scaled ingredients converted to imperial");
            } else if (system.equalsIgnoreCase(Constants.metricSystem)) {
                convertedIngredientList = RecipeConversionHelpers.imperialToMetric(scaledIngredientList);
                logger.info("Got the scaled ingredients converted to metric");
            }

            completeRecipe.setRecipe(recipe);
            completeRecipe.setIngredients(convertedIngredientList);
            logger.info("Got the complete recipe");
            return completeRecipe;
        } catch (SQLException e) {
            throw new RecipeExceptions(e);
        }
    }

    @Override
    public CompleteRecipeDto fetchRecipeByRecipeId(int recipeId) throws RecipeExceptions {
        logger.info("Enter service method fetchRecipeByRecipeId()");
        if (recipeId == 0) {
            logger.error("Invalid recipe");
            throw new RecipeExceptions("Invalid recipe");
        }

        try {
            logger.info("Fetching recipe with id {}", recipeId);
            CompleteRecipeDto completeRecipeDto = new CompleteRecipeDto();
            RecipeDto recipe = this.recipeDao.getRecipeById(recipeId);
            List<IngredientDto> ingredients = this.ingredientsService.fetchRecipeIngredients(recipeId);
            completeRecipeDto.setRecipe(recipe);
            completeRecipeDto.setIngredients(ingredients);
            return completeRecipeDto;
        } catch (SQLException e) {
            throw new RecipeExceptions("Recipe not found", e);
        }
    }

    @Override
    public CompleteRecipeDto updateRecipe(CompleteRecipeDto completeRecipeDto) throws RecipeExceptions {
        logger.info("Enter service method updateRecipe()");
        if (completeRecipeDto == null || completeRecipeDto.getRecipe() == null) {
            logger.error("Invalid recipe");
            throw new RecipeExceptions("Invalid recipe");
        }

        if (!completeRecipeDto.getRecipe().isEditable()) {
            logger.error("This recipe is not editable");
            throw new RecipeExceptions("This recipe is not editable");
        }

        try {
            logger.info("Updating recipe and ingredients");
            this.recipeDao.updateRecipe(completeRecipeDto.getRecipe());
            this.ingredientsService.updateIngredients(completeRecipeDto.getIngredients(), completeRecipeDto.getRecipe().getRecipeId());
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RecipeExceptions(e);
        }

        return completeRecipeDto;
    }
}
