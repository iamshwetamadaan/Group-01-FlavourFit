package com.flavourfit.Authentication;

import com.flavourfit.Exceptions.DuplicateUserException;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.Resources.Helpers;
import com.flavourfit.ResponsesDTO.AuthResponse;
import com.flavourfit.Security.JwtService;
import com.flavourfit.User.IUserDao;
import com.flavourfit.User.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class AuthService implements IAuthService {
    private final IUserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthService(
            IUserDao userDao, PasswordEncoder passwordEncoder, JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Method to authenticate the given user
     *
     * @param user -- User to be authenticated
     * @return AuthResponse denoting login
     */
    @Override
    public AuthResponse authenticateUser(UserDto user) {
        logger.info("Entered service method authenticateUser()");
        if (!Helpers.isValidUser(user)) {
            logger.error("Invalid user");
            throw new UserNotFoundException("Invalid user");
        }

        logger.info("Authenticating user.");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        AuthResponse response = new AuthResponse();

        try {
            UserDto currentUser = this.userDao.getUserByEmail(user.getEmail());

            if (currentUser != null) {
                var authToken = jwtService.generateToken(currentUser);
                response.setToken(authToken);
                response.setEmail(currentUser.getEmail());
                response.setSuccess(true);
                logger.info("Successfully authenticated user");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new UserNotFoundException(e);
        }

        return response;
    }

    /**
     * Method to register a user
     *
     * @param user
     * @return
     */
    @Override
    public AuthResponse registerUser(UserDto user) {
        logger.info("Entered service method registerUser()");
        AuthResponse response = new AuthResponse();

        if (!Helpers.isValidUser(user)) {
            logger.error("Invalid user.");
            throw new UserNotFoundException("Invalid user");
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            this.userDao.addUser(user);
            var authToken = jwtService.generateToken(user);
            response.setToken(authToken);
            response.setEmail(user.getEmail());
            response.setSuccess(true);
            logger.info("User added successfully.");
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DuplicateUserException(e);
        }
        return response;
    }

    @Override
    public int extractUserIdFromToken(String token) throws UserNotFoundException {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }

        String accessToken = token.replace("Bearer ", "");
        String email = jwtService.extractUsername(accessToken);
        try {
            int userId = userDao.getUserByEmail(email).getUserId();
            return userId;
        } catch (SQLException e) {
            throw new UserNotFoundException(e);
        }
    }
}
