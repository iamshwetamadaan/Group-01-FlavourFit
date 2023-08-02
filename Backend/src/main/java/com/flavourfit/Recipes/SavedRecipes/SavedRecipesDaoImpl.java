package com.flavourfit.Recipes.SavedRecipes;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class SavedRecipesDaoImpl implements ISavedRecipesDao {
    private static Logger logger = LoggerFactory.getLogger(SavedRecipesDaoImpl.class);
    private final IDatabaseManager database;
    private Connection connection;

    @Autowired
    public SavedRecipesDaoImpl(DatabaseManagerImpl database) {
        this.database = database;
        if (this.database != null && this.database.getConnection() != null) {
            this.connection = this.database.getConnection();
        }
    }

    @Override
    public SavedRecipesDto addSavedRecipe(int recipeId, int userId) throws SQLException {
        SavedRecipesDto savedRecipesDto = new SavedRecipesDto(recipeId, userId);
        logger.info("Started addSavedRecipe() method");

        this.testConnection();

        logger.info("Creating a prepared statement to insert record.");
        String query = "INSERT INTO Saved_Recipes (Recipe_id,User_id) values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        logger.info("Replacing values in prepared statement with actual values to be inserted");
        preparedStatement.setInt(1, recipeId);
        preparedStatement.setInt(2, userId);

        logger.info("Execute the insertion of record to the table");
        preparedStatement.executeUpdate();
        return savedRecipesDto;
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
}

