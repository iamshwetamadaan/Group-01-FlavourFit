package com.flavourfit.User;

import java.sql.SQLException;

public interface IUserService {
    public String fetchAllUsers() throws SQLException;
    public boolean resetPassword(int userId, String newPassword) throws SQLException;

}
