package com.flavourfit.Authentication;

import com.flavourfit.Emails.IEmailService;
import com.flavourfit.Exceptions.AuthException;
import com.flavourfit.Exceptions.DuplicateUserException;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.ResponsesDTO.AuthResponse;
import com.flavourfit.Security.JwtService;
import com.flavourfit.User.IUserDao;
import com.flavourfit.User.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    IUserDao userDao;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtService jwtService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    Authentication authentication;

    @Mock
    private IEmailService emailService;


    UserDto user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserDto();
        user.setEmail("test@email.com");
        user.setPassword("testpassword");
    }

    @Test
    void authenticateUserTest() throws SQLException {
        String token = "testtoken";
        when(jwtService.generateToken(any(UserDto.class))).thenReturn(token);
        when(userDao.getUserByEmail(user.getEmail())).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(
                authentication);

        AuthResponse response = null;
        try {
            response = authService.authenticateUser(user);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDao).getUserByEmail(user.getEmail());
        verify(jwtService).generateToken(any(UserDto.class));
        assertNotNull(response);
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(token, response.getToken());
        assertTrue(response.isSuccess());

        // Test SQLException path
        when(userDao.getUserByEmail(user.getEmail())).thenThrow(SQLException.class);
        assertThrows(UserNotFoundException.class, () -> authService.authenticateUser(user));

        // Test invalid user
        user.setEmail(null);
        assertThrows(UserNotFoundException.class, () -> authService.authenticateUser(user));
    }

    @Test
    void registerUserTest() throws SQLException {
        String token = "testtoken";
        String encodedPassword = "encodedpassword";
        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        when(jwtService.generateToken(any(UserDto.class))).thenReturn(token);
        doNothing().when(userDao).addUser(user);

        AuthResponse response = null;
        try {
            response = authService.registerUser(user);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        verify(userDao).addUser(any(UserDto.class));
        verify(passwordEncoder).encode(anyString());
        verify(jwtService).generateToken(any(UserDto.class));
        assertNotNull(response);
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(token, response.getToken());
        assertTrue(response.isSuccess());

        // Test SQLException path
        doThrow(SQLException.class).when(userDao).addUser(user);
        assertThrows(DuplicateUserException.class, () -> authService.registerUser(user));

        // Test invalid user
        user.setEmail(null);
        assertThrows(UserNotFoundException.class, () -> authService.registerUser(user));
    }

    @Test
    void extractUserIdFromTokenTest() throws SQLException {
        String token = "Bearer testtoken";
        int userId = 123;
        when(userDao.getUserByEmail(anyString())).thenReturn(user);
        when(jwtService.extractUsername(token.replace("Bearer ", ""))).thenReturn(user.getEmail());
        user.setUserId(userId);

        int resultId = -1;
        try {
            resultId = authService.extractUserIdFromToken(token);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        verify(jwtService).extractUsername(anyString());
        verify(userDao).getUserByEmail(anyString());
        assertEquals(userId, resultId);

        // Test SQLException path
        when(userDao.getUserByEmail(anyString())).thenThrow(SQLException.class);
        assertThrows(UserNotFoundException.class, () -> authService.extractUserIdFromToken(token));

        // Test invalid token
        assertThrows(RuntimeException.class, () -> authService.extractUserIdFromToken(null));
        assertThrows(RuntimeException.class, () -> authService.extractUserIdFromToken(""));
    }

    @Test
    void sendOtpMailTest() throws Exception {
        String email = "test@example.com";
        String otp = "123456";

        when(passwordEncoder.encode(any())).thenReturn("encoded_otp");
        when(userDao.getUserByEmail(email)).thenReturn(null); // Simulate that the user doesn't exist

        authService.sendOtpMail(email);
        verify(emailService).sendMail(any());
        verify(userDao).addUser(any());

        try {
            authService.sendOtpMail(null);
        } catch (AuthException e) {
            assertEquals("Invalid email", e.getMessage());
        }

        UserDto registeredUser = new UserDto();
        registeredUser.setEmail(email);
        registeredUser.setType("registered");

        when(userDao.getUserByEmail(email)).thenReturn(registeredUser); // Simulate that the user already exists and is registered

        try {
            authService.sendOtpMail(email);
        } catch (AuthException e) {
            assertEquals("User is already registered", e.getMessage());
        }
    }
}
