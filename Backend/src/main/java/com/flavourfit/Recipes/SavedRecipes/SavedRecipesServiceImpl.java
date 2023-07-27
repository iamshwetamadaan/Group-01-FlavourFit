package com.flavourfit.Recipes.SavedRecipes;

import com.flavourfit.Exceptions.RecipeExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class SavedRecipesServiceImpl implements ISavedRecipesService {
    private final ISavedRecipesDao savedRecipesDao;

    private static Logger logger = LoggerFactory.getLogger(SavedRecipesServiceImpl.class);

    @Autowired
    public SavedRecipesServiceImpl(ISavedRecipesDao savedRecipesDao) {
        this.savedRecipesDao = savedRecipesDao;
    }

    @Override
    public void saveRecipe(int recipeId, int userId) throws RecipeExceptions {
        logger.info("Entered saveRecipe() method");
        try {
            this.savedRecipesDao.addSavedRecipe(recipeId, userId);
            logger.info("Successfully saved recipe with id {} for userId {}", recipeId, userId);
        } catch (SQLException e) {
            logger.error("Failed to save recipe with id {} for userId {}", recipeId, userId);
            throw new RecipeExceptions(e);
        }
    }
}

