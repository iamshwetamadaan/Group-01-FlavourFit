package com.flavourfit;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.User.IUserDao;
import com.flavourfit.User.UserDaoImpl;
import com.flavourfit.User.UserDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;
import java.util.List;

@SpringBootApplication
public class FlavourFit {
    public static void main(String[] args) {
        IDatabaseManager database = new DatabaseManagerImpl();
        database.connect();

        IUserDao userDao = new UserDaoImpl(database);
        try {
//            UserDto user = new UserDto();
//            user.setFirstName("Jane");
//            user.setLastName("Doe");
//            user.setEmail("jane@doe.com");
//            user.setPhone("+1121231212");
//            user.setCurrentWeight(60.0d);
//            user.setCity("Halifax");
//            userDao.addUser(user);
            List<UserDto> users = userDao.getAllUsers();
            for (UserDto user : users) {
                System.out.println(user.toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        SpringApplication.run(FlavourFit.class, args);
        database.disconnect();
    }

}
