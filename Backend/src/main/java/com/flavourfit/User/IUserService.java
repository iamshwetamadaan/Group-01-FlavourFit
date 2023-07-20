package com.flavourfit.User;

import com.flavourfit.Exceptions.PaymentException;
import com.flavourfit.Exceptions.UserNotFoundException;

import java.sql.SQLException;

public interface IUserService {

    public boolean resetPassword(int userId, String newPassword) throws SQLException;

    public int updateUser(UserDto user) throws SQLException;

    UserDto fetchUserById(int id) throws UserNotFoundException;

    UserDto fetchUserByEmail(String email) throws UserNotFoundException;

    public PremiumUserDto getUserBymembership(int user) throws SQLException;

    public boolean paymentForPremium(int userID, String cardNumber, String mmyy, String cvv) throws PaymentException,
                                                                                                    SQLException;
}
