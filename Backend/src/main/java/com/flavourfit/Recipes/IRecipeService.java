package com.flavourfit.Recipes;

import java.sql.SQLException;
import java.util.List;

public interface IRecipeService {
    public List<String> fetchAllRecipeTypes() throws SQLException;
}
