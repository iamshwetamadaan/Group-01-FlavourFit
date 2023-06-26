package com.flavourfit;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.User.IUserDao;
import com.flavourfit.User.UserDaoImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class FlavourFit {
    public static void main(String[] args) {
        IDatabaseManager database = new DatabaseManagerImpl();
        database.connect();

        IUserDao userDao = new UserDaoImpl(database);
        try {
            userDao.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        SpringApplication.run(FlavourFit.class, args);
        database.disconnect();
    }

}
