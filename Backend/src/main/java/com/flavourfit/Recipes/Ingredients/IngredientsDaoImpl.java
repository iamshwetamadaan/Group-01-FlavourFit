package com.flavourfit.Recipes.Ingredients;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Exceptions.RecipeExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientsDaoImpl implements IIngredientsDao {
    private static Logger logger = LoggerFactory.getLogger(IngredientsDaoImpl.class);
    private final IDatabaseManager database;
    private Connection connection;

    @Autowired
    public IngredientsDaoImpl(DatabaseManagerImpl database) {
        this.database = database;
        if (this.database != null && this.database.getConnection() != null) {
            this.connection = this.database.getConnection();
        }
    }

    @Override
    public void addIngredients(List<IngredientDto> ingredients) throws SQLException {
        logger.info("Entered addIngredients() method");

        if (ingredients == null) {
            logger.error("Invalid ingredients");
            throw new SQLException("Invalid ingredients");
        }

        this.testConnection();

        String query = "INSERT INTO Ingredients (Ingredient_name,quantity,quantity_unit,Recipe_id) VALUES (?,?,?,?)";

        PreparedStatement preparedStatement = this.connection.prepareStatement(query);

        logger.info("Adding all ingredients to the prepared statement");
        for (IngredientDto ingredient : ingredients) {
            preparedStatement.setString(1, ingredient.getIngredientName());
            preparedStatement.setDouble(2, ingredient.getQuantity());
            preparedStatement.setString(3, ingredient.getQuantityUnit());
            preparedStatement.setInt(4, ingredient.getRecipeId());
            preparedStatement.addBatch();
        }

        logger.info("Updated records to ingredients table.");
        preparedStatement.executeBatch();
    }

    @Override
    public List<IngredientDto> getRecipeIngredients(int recipeId) throws SQLException {
        logger.info("Entered getRecipeIngredients() method");

        this.testConnection();

        List<IngredientDto> ingredients = null;

        logger.info("Executing query to get the ingredients list for recipe #{}", recipeId);
        String query = "SELECT * FROM Ingredients WHERE Recipe_id=?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setInt(1, recipeId);

        ResultSet resultSet = preparedStatement.executeQuery();

        ingredients = this.extractIngredients(resultSet);
        logger.info("Extracted ingredients from result set", recipeId);
        return ingredients;
    }

    @Override
    public void updateIngredients(List<IngredientDto> ingredients) throws SQLException {
        logger.info("Entered updateIngredients() method");

        this.testConnection();

        String query = "UPDATE Ingredients SET Ingredient_name=?,quantity=?,quantity_unit=? WHERE Ingredient_id=?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);

        logger.info("Adding all ingredients to the prepared statement");
        for (IngredientDto ingredient : ingredients) {
            if (ingredient.getRecipeId() == 0) {
                logger.error("Invalid recipe id for ingredient");
                throw new RecipeExceptions("Invalid recipe id for ingredient");
            }
            preparedStatement.setString(1, ingredient.getIngredientName());
            preparedStatement.setDouble(2, ingredient.getQuantity());
            preparedStatement.setString(3, ingredient.getQuantityUnit());
            preparedStatement.setInt(4, ingredient.getIngredientId());
            preparedStatement.addBatch();
        }

        logger.info("Updated records to ingredients table.");
        preparedStatement.executeBatch();
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

    private List<IngredientDto> extractIngredients(ResultSet resultSet) throws SQLException {
        List<IngredientDto> ingredients = new ArrayList<>();
        while (resultSet.next()) {
            IngredientDto ingredient = new IngredientDto();
            ingredient.setIngredientId(resultSet.getInt("Ingredient_id"));
            ingredient.setIngredientName(resultSet.getString("Ingredient_name"));
            ingredient.setQuantity(resultSet.getDouble("quantity"));
            ingredient.setQuantityUnit(resultSet.getString("quantity_unit"));
            ingredient.setRecipeId(resultSet.getInt("Recipe_id"));
            ingredients.add(ingredient);
        }
        return ingredients;
    }
}
