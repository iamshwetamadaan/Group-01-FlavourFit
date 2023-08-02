package com.flavourfit.User;

import com.flavourfit.Exceptions.UserNotFoundException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IUserDao {
    public List<UserDto> getAllUsers() throws SQLException;

    public UserDto getUserById(int userId) throws SQLException;

    public void addUser(UserDto user) throws SQLException;

    public boolean resetUserPassword(int userID, String newPassword) throws SQLException;
    
    public PremiumUserDto getUserBymembership(int userId) throws SQLException;

    public int updateUser(UserDto user) throws SQLException;

    UserDto getUserByEmail(String email) throws SQLException;

    public int userToPremiumPayment(int userId, PremiumUserPaymentDetailsDto details) throws SQLException;

    public int startExtendPremiumMembership(int userId, String startDate, String expiryDate, int paymentID) throws SQLException;

    public boolean updateUserPayment(int userId, int paymentID, int premiumMembershipID) throws SQLException;

    void clearGuestPassword(String email) throws UserNotFoundException;


    void updateUserWeight(double weight, int userId) throws SQLException;

    double getUserCurrentWeight(int userId) throws SQLException;
}
