package com.flavourfit.User;

import com.flavourfit.Exceptions.PaymentException;
import com.flavourfit.Exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;


@Service
public class

UserService implements IUserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final IUserDao userDao;

    @Autowired
    public UserService(IUserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int updateUser(UserDto user) throws SQLException {
        logger.info("Started updateUser() method");
        if (user == null) {
            logger.error("Invalid user parameter");
            throw new RuntimeException("Invalid user");
        }

        return this.userDao.updateUser(user);
    }

    @Override
    public UserDto fetchUserById(int id) throws UserNotFoundException {
        logger.info("Started fetchUserById() method");

        try {
            UserDto user = this.userDao.getUserById(id);
            logger.info("Exiting fetchUserById() method");
            return user;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new UserNotFoundException(e);
        }
    }

    public boolean resetPassword(int userID, String newPassword) throws SQLException {
        logger.info("Started resetPassword() method");
        if (newPassword == null || newPassword.isEmpty()) {
            logger.warn("Invalid password parameter");
            throw new RuntimeException("Invalid Password");
        }
        newPassword = passwordEncoder.encode(newPassword);
        return this.userDao.resetUserPassword(userID, newPassword);
    }

    @Override
    public PremiumUserDto getUserBymembership(int user) throws SQLException {
        logger.info("Started getUserByMembership() method");
        return this.userDao.getUserBymembership(user);
    }

    @Override
    public UserDto fetchUserByEmail(String email) throws UserNotFoundException {
        logger.info("Started fetchUserByEmail() method");

        try {
            logger.info("Calling getUserByEmail() method of userDao");

            UserDto user = this.userDao.getUserByEmail(email);
            return user;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new UserNotFoundException(e);
        }
    }

    public int paymentForPremium(int userID, PremiumUserPaymentDetailsDto details) throws PaymentException,
            SQLException {

        String cardNumber = details.getCardNumber();
        String cvv = details.getCvv();
        String expiryMonth = details.getExpiryMonth();
        String expiryYear = details.getExpiryYear();

        logger.info("Started paymentForPremiumCheck() method");
        if (cardNumber.length() != 16) {
            logger.warn("Invalid card number length");
            throw new PaymentException("Invalid Payment: Card Number entered is not valid");
        }
        if (cvv.length() != 3) {
            logger.warn("Invalid cvv length");
            throw new PaymentException("Invalid Payment: CVV entered is not valid");
        }

        if (expiryMonth.length() != 2 && expiryYear.length() != 2) {
            logger.warn("Invalid MM/YY syntax");
            throw new PaymentException("Invalid Payment: MM/YY syntax entered is not valid");
        } else {
            int month = Integer.parseInt(expiryMonth);
            int year = Integer.parseInt(expiryYear);

            if (!(month >= 12 && month < 00) || !(year >= 30 && month < 10)) {
                logger.warn("Invalid MM/YY ranges");
                throw new PaymentException("Invalid Payment: MM/YY entered is not in valid ranges");
            }
        }
        return this.userDao.userToPremiumPayment(userID, details);
    }

    public boolean startExtendPremium(int userID, int paymentID) throws SQLException {
        boolean hasStartExtend = false;

        logger.info("Started startExtendPremium() method");

        if (userID != 0 && paymentID != 0) {

            //default time zone
            ZoneId defaultZoneId = ZoneId.systemDefault();

            LocalDate currentDate = LocalDate.now();

            Date startDate = (Date) Date.from(currentDate.atStartOfDay(defaultZoneId).toInstant());

            LocalDate nextYearDate = currentDate.plusYears(1);

            Date expiryDate = (Date) Date.from(nextYearDate.atStartOfDay(defaultZoneId).toInstant());

            int premiumMembershipID = this.userDao.startExtendPremiumMembership(userID, startDate, expiryDate, paymentID);

            if (premiumMembershipID != 0) {
                logger.info("Successful PremiumMemberShip Table Insert");
                boolean paymentTableUpdated = this.userDao.updateUserPayment(userID, paymentID, premiumMembershipID);

                if (paymentTableUpdated) {
                    hasStartExtend = true;
                    logger.info("Successful Payment Table Update");
                } else {
                    logger.warn("Invalid Updating Payment Table");
                }
            } else {
                logger.warn("Invalid adding to PremiumMemberShip Table");
            }
        }
        return hasStartExtend;
    }

    @Override
    public void clearPassword(String email) throws UserNotFoundException {
        logger.info("Entered clearPassword() method");

        if (email == null || email.isEmpty()) {
            logger.error("Invalid email {} while clearing password", email);
            throw new UserNotFoundException("Invalid email " + email);
        }

        this.userDao.clearGuestPassword(email);
        logger.info("End clearPassword() method");
    }

    @Override
    public void updateUserWeight(double weight, int userId) throws UserNotFoundException {
        logger.info("Enter service method updateUserWeight()");
        if (userId == 0) {
            logger.error("Invalid user {}", userId);
            throw new UserNotFoundException("Invalid user");
        }

        if (weight <= 0) {
            logger.error("Invalid weight value");
            throw new RuntimeException("Invalid weight value");
        }
        try {
            this.userDao.updateUserWeight(weight, userId);
            logger.info("Updated weight for user {}", userId);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new UserNotFoundException(e);
        }
    }

    @Override
    public double fetchUserCurrentWeight(int userId) throws UserNotFoundException {
        logger.info("Enter service method fetchUserCurrentWeight()");
        if (userId == 0) {
            logger.error("Invalid user {}", userId);
            throw new UserNotFoundException("Invalid user");
        }

        try {
            double weight = this.userDao.getUserCurrentWeight(userId);
            logger.info("Fetched weight for user {} successfully", userId);
            return weight;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
