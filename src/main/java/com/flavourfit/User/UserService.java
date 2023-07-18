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

    public int updateUser(UserDto user) throws SQLException{
        return this.userDao.updateUser(user);
    }

    @Override
    public UserDto getUserbyID(int user) throws SQLException{
        return this.userDao.getUserById(user);
    }

    @Override
    public PremiumUserDto getUserBymembership(int user) throws SQLException{
        return this.userDao.getUserBymembership(user);
    }

}
