package com.flavourfit.Recipes.Ingredients;

import com.flavourfit.Exceptions.RecipeExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class IngredientsServiceImpl implements IIngredientsService {
    private final IIngredientsDao ingredientsDao;

    private static Logger logger = LoggerFactory.getLogger(IngredientsServiceImpl.class);

    @Autowired
    public IngredientsServiceImpl(IIngredientsDao ingredientsDao) {
        this.ingredientsDao = ingredientsDao;
    }

    @Override
    public void addIngredients(List<IngredientDto> ingredients, int recipeId) throws RecipeExceptions {
        logger.info("Entered addIngredients() method.");
        if (ingredients == null) {
            logger.error("Invalid ingredients list");
            throw new RecipeExceptions("Invalid ingredients list");
        }

        logger.info("Setting recipe id for all ingredients!");
        for (IngredientDto ingredientDto : ingredients) {
            ingredientDto.setRecipeId(recipeId);
        }

        try {
            this.ingredientsDao.addIngredients(ingredients);
            logger.info("Successfully added ingredients for recipeId {}", recipeId);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RecipeExceptions(e);
        }
    }

    @Override
    public List<IngredientDto> fetchRecipeIngredients(int recipeId) throws RecipeExceptions {
        logger.info("Entered fetchRecipeIngredients() method.");
        try {
            return this.ingredientsDao.getRecipeIngredients(recipeId);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RecipeExceptions(e);
        }
    }

}
