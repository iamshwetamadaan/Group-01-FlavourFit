package com.flavourfit.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDao {
    public List<UserDto> getAllUsers() throws SQLException;

    public UserDto getUserById(int userId) throws SQLException;

    public void addUser(UserDto user) throws SQLException;
}
