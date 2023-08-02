package com.flavourfit.DatabaseManager;

import com.flavourfit.Exceptions.DatabaseException;
import com.flavourfit.Resources.Helpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@Component
public class DatabaseManagerImpl implements IDatabaseManager {
    private static Logger logger = LoggerFactory.getLogger(DatabaseManagerImpl.class);

    private Connection connection = null;

    //Singleton instance
    private static DatabaseManagerImpl instance = null;

    private DatabaseManagerImpl() {
        logger.warn("========= Constructor called");
        try {
            this.connect();
        } catch (DatabaseException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //Method to get singleton instance
    public static synchronized DatabaseManagerImpl getInstance() {
        if (instance == null) {
            instance = new DatabaseManagerImpl();
        }
        return instance;
    }

    /**
     * Method to connect to the project database
     *
     * @return -- true if the connection is successful
     * @throws RuntimeException
     */
    @Override
    public boolean connect() throws DatabaseException {
        logger.info("Entered connect() method");

        try {
            logger.info("Retrieving database credentials from properties file.");
            Properties appProperties = Helpers.getAppProperties();

            String username = appProperties.getProperty("db.username");
            String password = appProperties.getProperty("db.password");
            String dbUrl = appProperties.getProperty("db.url");

            logger.info("Creating a database connection using the retrieved credentials.");
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(dbUrl, username, password);
            logger.info("Database connection successful");

            String schema = appProperties.getProperty("db.schema");
            this.setSchema(this.connection, schema);

            logger.info("Connect() method Ended");
            return true;
        } catch (IOException e) {
            throw new DatabaseException(e);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } catch (ClassNotFoundException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Method to disconnect from the connected database
     *
     * @return -- true if disconnected
     * @throws RuntimeException
     */
    @Override
    public boolean disconnect() throws DatabaseException {
        if (this.connection != null) {
            try {
                logger.warn("Disconnecting from the database!!");
                this.connection.close();
                logger.info("Database disconnected");
                return true;
            } catch (SQLException e) {
                logger.error(e.getMessage());
                throw new DatabaseException(e);
            }
        }
        return true;
    }


    @Override
    public Connection getConnection() {
        return this.connection;
    }

    private void setSchema(Connection connection, String schema) throws SQLException {
        logger.info("Setting database to required schema");
        Statement statement = connection.createStatement();
        statement.execute("USE " + schema);
        logger.info("Schema set successfully");

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.disconnect();
    }
}
