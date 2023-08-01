package com.flavourfit.User;

import com.flavourfit.Exceptions.PaymentException;
import com.flavourfit.Exceptions.UserNotFoundException;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public interface IUserService {

    public boolean resetPassword(int userId, String newPassword) throws SQLException;

    public int updateUser(UserDto user) throws SQLException;

    UserDto fetchUserById(int id) throws UserNotFoundException;

    UserDto fetchUserByEmail(String email) throws UserNotFoundException;

    public PremiumUserDto getUserBymembership(int user) throws SQLException;

    public int paymentForPremium(int userID, PremiumUserPaymentDetailsDto details) throws PaymentException, SQLException;

    public boolean startExtendPremium(int userID, int paymentID) throws SQLException, ParseException;

    void clearPassword(String email) throws UserNotFoundException;


    void updateUserWeight(double weight, int userId) throws UserNotFoundException;

    double fetchUserCurrentWeight(int userId) throws UserNotFoundException;
}
