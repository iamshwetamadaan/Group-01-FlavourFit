package com.flavourfit.Recipes;

import com.flavourfit.Exceptions.RecipeExceptions;
import com.flavourfit.Recipes.Ingredients.IIngredientsService;
import com.flavourfit.Recipes.Ingredients.IngredientDto;
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


    public RecipeServiceImpl(
            IRecipeDao recipeDao, IIngredientsService ingredientsService
    ) {
        this.recipeDao = recipeDao;
        this.ingredientsService = ingredientsService;
    }

    @Override
    public List<String> fetchAllRecipeTypes() throws SQLException {
        logger.info("Started method fetchAllRecipeTypes()");
        return recipeDao.getAllRecipesTypes();
    }

    @Override
    public ArrayList<Object> getRecipesByUser(int count, int userId) throws SQLException {
        logger.info("Started method getRecipesByUser()");
        return recipeDao.getRecipesByUser(userId,count);
    }

    @Override
    public ArrayList<Object> getFilteredRecipesByUser(int id, HashMap<String, Object> requestBody) throws SQLException {
        logger.info("Started method getFilteredRecipesByUser");
        return recipeDao.getFilteredRecipesByUser(id,requestBody);
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

//                this.savedRecipesService.saveRecipe(recipeId, userId);

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
}
