package com.flavourfit.User;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private IUserDao userDao;
    private IDatabaseManager database;

    public UserController() {
        this.database = new DatabaseManagerImpl();
        this.database.connect();
        this.userDao = new UserDaoImpl(this.database);
        this.userService = new UserService(this.userDao);
    }

    @RequestMapping("/fetch-all")
    public String fetchAllUsers() throws SQLException {
        return userService.fetchAllUsers();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.database.disconnect();
    }
}
