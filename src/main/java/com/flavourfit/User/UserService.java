package com.flavourfit.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService implements IUserService {
    private final IUserDao userDao;

    @Autowired
    public UserService(IUserDao userDao) {
        this.userDao = userDao;

    }

    @Override
    public String fetchAllUsers() throws SQLException {
        List<UserDto> users = this.userDao.getAllUsers();
        StringBuilder usersStr = new StringBuilder();
        for (UserDto user : users) {
            usersStr.append(user.toString());
            usersStr.append("\n");
        }
        return usersStr.toString();
    }

    public boolean resetPassword(int userID,String newPassword) throws SQLException {
        if (newPassword == null || newPassword.isEmpty()) {
            throw new RuntimeException("Invalid Password");
        }
        return this.userDao.resetUserPassword(userID, newPassword);
    }
    public int updateUser(UserDto user) throws SQLException{
        return this.userDao.updateUser(user);
    }

}
