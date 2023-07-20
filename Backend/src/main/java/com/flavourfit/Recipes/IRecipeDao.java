package com.flavourfit.Recipes;

import java.sql.SQLException;
import java.util.List;

public interface IRecipeDao {
    public List<String> getAllRecipesTypes() throws SQLException;
}
