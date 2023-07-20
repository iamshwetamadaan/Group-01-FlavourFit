package com.flavourfit.Recipes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class RecipeServiceImpl implements IRecipeService{

    private static Logger logger = LoggerFactory.getLogger(RecipeServiceImpl.class);

    private IRecipeDao recipeDao;

    @Autowired
    public RecipeServiceImpl(IRecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    @Override
    public List<String> fetchAllRecipeTypes() throws SQLException {
        logger.info("Started method fetchAllRecipeTypes()");
        return recipeDao.getAllRecipesTypes();
    }
}
