package com.flavourfit.Recipes;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Exceptions.RecipeExceptions;
import com.flavourfit.Recipes.Ingredients.IngredientDto;
import com.flavourfit.ResponsesDTO.SavedRecipesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class RecipeDaoImpl implements IRecipeDao {
    private static Logger logger = LoggerFactory.getLogger(RecipeDaoImpl.class);
    private final IDatabaseManager database;
    private Connection connection;

    @Autowired
    public RecipeDaoImpl(DatabaseManagerImpl database) {
        this.database = database;
        if (this.database != null && this.database.getConnection() != null) {
            this.connection = this.database.getConnection();
        }
    }

    /**
     * Method fetches all recipes types of the existing recipes present in the database
     *
     * @return list of unique types fetched from the database
     * @throws SQLException
     */
    @Override
    public List<String> getAllRecipesTypes() throws SQLException {
        logger.info("Started getAllRecipesTypes() method");
        List<String> recipeTypes = new ArrayList<>();
        this.testConnection();

        Statement statement = connection.createStatement();
        logger.info("Running query to fetch different types from the recipes");
        ResultSet resultset = statement.executeQuery("SELECT DISTINCT(types) FROM Recipes;");
        while (resultset.next()) {
            recipeTypes.add(resultset.getString("types"));
        }
        logger.info("Received data from db and added types to recipeTypes list.");
        return recipeTypes;
    }

    @Override
    public RecipeDto addRecipe(RecipeDto recipeDto, int userId) throws SQLException {
        logger.info("Started addRecipe() method");
        if (recipeDto == null) {
            logger.error("Invalid recipe");
            throw new SQLException("Invalid recipe");
        }

        this.testConnection();

        logger.info("Creating a prepared statement to insert record.");
        String query = "INSERT INTO Recipes (Recipe_name,Recipe_description,Types,editable) values (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        logger.info("Replacing values in prepared statement with actual values to be inserted");
        this.replaceStatementPlaceholders(recipeDto, preparedStatement);
        logger.info("Execute the insertion of record to the table");
        preparedStatement.executeUpdate();

        ResultSet keys = preparedStatement.getGeneratedKeys();
        long insertedRecipeId;
        while (keys.next()) {
            insertedRecipeId = keys.getLong(1);
            recipeDto.setRecipeId((int) insertedRecipeId);
            logger.info("Added User with userId: {}, to the Users table!", insertedRecipeId);
        }

        return recipeDto;
    }

    @Override
    public ArrayList<Object> getRecipesByUser(int id, int count) throws SQLException {
        logger.info("Started getRecipesByUser() method");
        ArrayList<Object> recipes = new ArrayList<Object>();

        if (count == 0)
            throw new SQLException("Count cannot be 0");

        this.testConnection();

        logger.info("Creating a statement to get the records the record");
        Statement statement = connection.createStatement();
        logger.info("Running query to fetch recipes for given user");
        ResultSet resultset = statement.executeQuery("SELECT Recipes.recipe_id, Recipes.recipe_name, Recipes.recipe_description,Recipes.types \n" +
                "FROM Recipes\n" +
                "INNER JOIN Saved_Recipes ON Recipes.recipe_id=Saved_Recipes.recipe_id\n" +
                "where Saved_Recipes.user_id=" + id);
        while (resultset.next()) {
            SavedRecipesResponse recipe = new SavedRecipesResponse();

            recipe.setRecipeName(resultset.getString("recipe_name"));
            recipe.setRecipeId(resultset.getInt("recipe_id"));
            recipe.setDescription(resultset.getString("recipe_description"));
            recipe.setTypes(resultset.getString("types"));
            recipes.add(recipe);
        }
        logger.info("Received data from db and added types to recipeTypes list.");
        return recipes;
    }

    @Override
    public ArrayList<Object> getFilteredRecipesByUser(int id, HashMap<String, Object> requestBody) throws SQLException {
        logger.info("Started getFilteredRecipesByUser() method");
        ArrayList<Object> recipes = new ArrayList<Object>();

        String keyword = (String) requestBody.get("keyword");
        String typesStr = ((String) requestBody.get("types"));
        String[] types=null;
        if(typesStr!=null && !typesStr.isEmpty()){
            types = typesStr.split(",");
        }

        int count = (int) requestBody.get("count");

        if (count == 0)
            throw new IllegalArgumentException("Count cannot be 0");

        this.testConnection();

        logger.info("Creating a prepared statement to get the records the record");
        StringBuilder sql = new StringBuilder("SELECT * FROM Recipes");

        boolean hasTypes = types!=null && types.length > 0;
        boolean hasKeyword = (keyword != null && !keyword.isEmpty());

        if (hasTypes || hasKeyword) {
            sql.append(" WHERE ");
        }

        if (hasTypes) {
            sql.append("Types IN (");
            for (int i = 0; i < types.length; i++) {
                sql.append("?");
                if (i != types.length - 1) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }

        if (hasTypes && hasKeyword) {
            sql.append(" AND ");
        }

        if (hasKeyword) {
            sql.append("(Recipe_name LIKE ? OR Recipe_description LIKE ?)");
        }

        PreparedStatement preparedStatement = this.connection.prepareStatement(sql.toString());
        int index = 1;
        if (hasTypes) {
            for (String type : types) {
                preparedStatement.setString(index++, type);
            }
        }

        if (hasKeyword) {
            preparedStatement.setString(index++, "%" + keyword + "%");
            preparedStatement.setString(index, "%" + keyword + "%");
        }

        logger.info("Running query to fetch recipes for given user");
        ResultSet resultset = preparedStatement.executeQuery();
        while (resultset.next()) {
            SavedRecipesResponse recipe = new SavedRecipesResponse();

            recipe.setRecipeName(resultset.getString("recipe_name"));
            recipe.setRecipeId(resultset.getInt("recipe_id"));
            recipe.setDescription(resultset.getString("recipe_description"));
            recipe.setTypes(resultset.getString("types"));
            recipes.add(recipe);
        }
        logger.info("Received data from db and sending the response back to the method");
        return recipes;
    }

    @Override
    public RecipeDto getRecipeById(int recipeId) throws SQLException {
        logger.info("Started getRecipeById() method");

        this.testConnection();

        logger.info("Running select query to get recipe by recipeId");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Recipes WHERE Recipe_id=?");
        preparedStatement.setInt(1, recipeId);
        ResultSet resultSet = preparedStatement.executeQuery();

        RecipeDto recipe = extractRecipeFromResults(resultSet);

        logger.info("Returning recipeDto from recipe table as response");
        return recipe;
    }

    @Override
    public List<IngredientDto> getRecipeIngredients(int recipeId) throws SQLException {
        logger.info("Started getRecipeIngredients() method");
        List<IngredientDto> recipeIngredients = new ArrayList<>();
        this.testConnection();

        logger.info("Running select query to get ingredients by recipeId");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Ingredients WHERE Recipe_id=?");
        preparedStatement.setInt(1, recipeId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            recipeIngredients.add(this.extractIngredientFromResults(resultSet));
        }

        logger.info("Returning ingredients from recipe table as response");
        return recipeIngredients;
    }

    @Override
    public void updateRecipe(RecipeDto recipeDto) throws SQLException {
        logger.info("Entered dao method updateRecipe()");
        if (recipeDto == null || recipeDto.getRecipeId() == 0) {
            logger.error("Invalid recipe");
            throw new RecipeExceptions("Invalid recipe");
        }

        this.testConnection();

        if (!recipeDto.isEditable()) {
            logger.error("Cannot edit this recipe with id {}", recipeDto.getRecipeId());
            throw new RecipeExceptions("Cannot edit this recipe");
        }

        String query = "UPDATE Recipes SET Recipe_name=?, Recipe_description=?, Types=? WHERE Recipe_id=?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);

        logger.info("Creating query to update recipe");
        preparedStatement.setString(1, recipeDto.getRecipeName());
        preparedStatement.setString(2, recipeDto.getRecipeDescription());
        preparedStatement.setString(3, recipeDto.getTypes());
        preparedStatement.setInt(4, recipeDto.getRecipeId());
        preparedStatement.executeUpdate();
        logger.info("Updated recipe. recipeId: {}", recipeDto.getRecipeId());
    }


    private void testConnection() throws SQLException {
        if (database == null && connection == null) {
            logger.error("SQL connection not found!");
            throw new SQLException("SQL connection not found!");
        }
        if (connection == null && this.database.getConnection() == null) {
            logger.error("SQL connection not found!");
            throw new SQLException("SQL connection not found!");
        } else {
            this.connection = this.database.getConnection();
        }
    }

    private void replaceStatementPlaceholders(RecipeDto recipe, PreparedStatement preparedStatement) throws
            SQLException {
        if (recipe == null || preparedStatement == null) {
            return;
        }

        preparedStatement.setString(1, recipe.getRecipeName());
        preparedStatement.setString(2, recipe.getRecipeDescription());
        preparedStatement.setString(3, recipe.getTypes());
        preparedStatement.setBoolean(4, recipe.isEditable());
    }

    private IngredientDto extractIngredientFromResults(ResultSet resultSet) throws SQLException {
        IngredientDto ingredient = new IngredientDto();
        if (resultSet != null) {
            ingredient.setIngredientId(resultSet.getInt("Ingredient_id"));
            ingredient.setIngredientName(resultSet.getString("Ingredient_name"));
            ingredient.setRecipeId(resultSet.getInt("Recipe_id"));
            ingredient.setQuantity(resultSet.getDouble("quantity"));
            ingredient.setQuantityUnit(resultSet.getString("quantity_unit"));
        }
        return ingredient;
    }

    private RecipeDto extractRecipeFromResults(ResultSet resultSet) throws SQLException {
        RecipeDto recipe = new RecipeDto();
        if (resultSet.next()) {
            recipe.setRecipeId(resultSet.getInt("Recipe_id"));
            recipe.setRecipeName(resultSet.getString("Recipe_name"));
            recipe.setRecipeDescription(resultSet.getString("Recipe_description"));
            recipe.setTypes(resultSet.getString("Types"));
            recipe.setEditable(resultSet.getBoolean("editable"));
        }
        return recipe;
    }
}
