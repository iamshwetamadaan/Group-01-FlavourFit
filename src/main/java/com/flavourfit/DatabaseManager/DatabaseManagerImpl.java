package com.flavourfit.DatabaseManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@Component
public class DatabaseManagerImpl implements IDatabaseManager {
    private static Logger logger = LoggerFactory.getLogger(DatabaseManagerImpl.class);

    private Connection connection = null;

    /**
     * Method to connect to the project database
     *
     * @return -- true if the connection is successful
     * @throws RuntimeException
     */
    @Override
    public boolean connect() throws RuntimeException {
        logger.info("Entered connect() method");

        Properties dbProperties = new Properties();
        try {
            logger.info("Retrieving database credentials from properties file.");
            InputStream stream = new FileInputStream("src/main/resources/application.properties");
            dbProperties.load(stream);
            String username = dbProperties.getProperty("db.username");
            String password = dbProperties.getProperty("db.password");
            String dbUrl = dbProperties.getProperty("db.url");

            logger.info("Creating a database connection using the retrieved credentials.");
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(dbUrl, username, password);
            logger.info("Database connection successful");

            String schema = dbProperties.getProperty("db.schema");
            this.setSchema(this.connection, schema);

            logger.info("Connect() method Ended");
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to disconnect from the connected database
     *
     * @return -- true if disconnected
     * @throws RuntimeException
     */
    @Override
    public boolean disconnect() throws RuntimeException {
        if (this.connection != null) {
            try {
                logger.warn("Disconnecting from the database!!");
                this.connection.close();
                logger.info("Database disconnected");
                return true;
            } catch (SQLException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
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
}
