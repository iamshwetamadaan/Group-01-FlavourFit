package com.flavourfit.Homepage;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.User.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class HomepageDaoImpl {

    private static Logger logger = LoggerFactory.getLogger(HomepageDaoImpl.class);

    private final IDatabaseManager database;

    private Connection connection;
    @Autowired
    public HomepageDaoImpl() {
        this.database = DatabaseManagerImpl.getInstance();
        if (this.database != null && this.database.getConnection() != null) {
            this.connection = this.database.getConnection();
        }
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
