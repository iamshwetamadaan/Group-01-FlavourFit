package com.flavourfit.User;

import com.flavourfit.Exceptions.UserNotFoundException;
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

    @Override
    public int updateUser(UserDto user) throws SQLException {
        return this.userDao.updateUser(user);
    }

    @Override
    public UserDto fetchUserById(int id) throws UserNotFoundException {
        try {
            UserDto user = this.userDao.getUserById(id);
            return user;
        } catch (SQLException e) {
            throw new UserNotFoundException(e);
        }
    }

    @Override
    public UserDto fetchUserByEmail(String email) throws UserNotFoundException {
        try {
            UserDto user = this.userDao.getUserByEmail(email);
            return user;
        } catch (SQLException e) {
            throw new UserNotFoundException(e);
        }
    }
}
