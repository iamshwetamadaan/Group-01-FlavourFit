package com.flavourfit.Recipes;

import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RecipeDaoImpl implements IRecipeDao{
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
        while(resultset.next()){
            recipeTypes.add(resultset.getString("types"));
        }
        logger.info("Received data from db and added types to recipeTypes list.");
        return recipeTypes;
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
