package com.flavourfit.Recipes;

import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RecipeDaoImpl implements IRecipeDao {
    private static Logger logger = LoggerFactory.getLogger(RecipeDaoImpl.class);
    private final IDatabaseManager database;
    private Connection connection;

    @Autowired
    public RecipeDaoImpl(IDatabaseManager database) {
        this.database = database;
        this.connection = database.getConnection();
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
    public RecipeDto addRecipe(RecipeDto recipeDto,int userId) throws SQLException {
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
}
