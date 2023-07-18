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

    public boolean resetPassword(int userID, String newPassword) throws SQLException {
        if (newPassword == null || newPassword.isEmpty()) {
            throw new RuntimeException("Invalid Password");
        }
        return this.userDao.resetUserPassword(userID, newPassword);
    }

    public void registerUser(UserDto user) throws SQLException {
        if (user.getEmail() != null && user.getPassword() != null) {
            if (userDao.getUserById(user.getUserId()) == null) {
                this.userDao.addUser(user);
            } else {
                throw new RuntimeException("User already exists");
            }
        } else {
            throw new RuntimeException("Invalid details");
        }
    }

    @Override
    public UserDto getUserbyID(int user) throws SQLException {
        return this.userDao.getUserById(user);
    }

    @Override
    public PremiumUserDto getUserBymembership(int user) throws SQLException {
        return this.userDao.getUserBymembership(user);
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
