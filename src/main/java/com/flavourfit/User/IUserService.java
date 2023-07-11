package com.flavourfit.User;

import java.sql.SQLException;

public interface IUserService {
    public String fetchAllUsers() throws SQLException;

    public int updateUser(UserDto user) throws SQLException;

    UserDto getUserbyID(int user) throws SQLException;
}
