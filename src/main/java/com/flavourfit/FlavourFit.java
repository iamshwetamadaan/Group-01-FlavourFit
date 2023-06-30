package com.flavourfit;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.User.IUserDao;
import com.flavourfit.User.UserDaoImpl;
import com.flavourfit.User.UserDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@SpringBootApplication
public class FlavourFit {
    public static void main(String[] args) {
        SpringApplication.run(FlavourFit.class, args);
    }

}
