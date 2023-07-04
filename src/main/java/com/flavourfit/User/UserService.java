package com.flavourfit.User;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {
    private final IUserDao userDao;
    private IDatabaseManager database;

    public UserService(IUserDao userDao) {
        this.userDao = userDao;

    }

    public String fetchAllUsers() throws SQLException {
        List<UserDto> users = this.userDao.getAllUsers();
        StringBuilder usersStr = new StringBuilder();
        for (UserDto user : users) {
            usersStr.append(user.toString());
            usersStr.append("\n");
        }
        return usersStr.toString();
    }

    public int UpdateUser(UserDto user) throws SQLException{
        return this.userDao.updateUser(user);

    }



}
