package com.flavourfit.Authentication;

import com.flavourfit.Exceptions.DuplicateUserException;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.ResponsesDTO.AuthResponse;
import com.flavourfit.Security.JwtService;
import com.flavourfit.User.IUserDao;
import com.flavourfit.User.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;

public class AuthServiceTest {

    @Mock
    private IUserDao userDao;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void authenticateUserTest() throws SQLException {
        UserDto validUser = new UserDto();
        validUser.setEmail("test@example.com");
        validUser.setPassword("password");

        UserDto existingUser = new UserDto();
        existingUser.setEmail("test@example.com");

        Mockito.when(userDao.getUserByEmail(validUser.getEmail())).thenReturn(existingUser);
        Mockito.when(jwtService.generateToken(existingUser)).thenReturn("mockToken");

        // Valid User
        AuthResponse response = authService.authenticateUser(validUser);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("mockToken", response.getToken());
        Assertions.assertEquals(existingUser.getEmail(), response.getEmail());

        Mockito.verify(authenticationManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(userDao).getUserByEmail(validUser.getEmail());
        Mockito.verify(jwtService).generateToken(existingUser);

        // Invalid User
        UserDto invalidUser = new UserDto(); // Invalid user with missing email and password

        Mockito.when(userDao.getUserByEmail(Mockito.anyString())).thenThrow(new SQLException());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            authService.authenticateUser(invalidUser);
        });

        Mockito.verifyNoMoreInteractions(authenticationManager);
        Mockito.verify(userDao).getUserByEmail(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(jwtService);

    }

    @Test
    public void registerUserTest() throws SQLException {
        // Arrange
        UserDto validUser = new UserDto();
        validUser.setEmail("test@example.com");
        validUser.setPassword("hashedPassword");

        UserDto duplicateUser = new UserDto();
        duplicateUser.setEmail("test@example.com");
        duplicateUser.setPassword("hashedPassword");

        Mockito.when(passwordEncoder.encode(validUser.getPassword())).thenReturn("hashedPassword");
        Mockito.when(passwordEncoder.encode(duplicateUser.getPassword())).thenReturn("hashedPassword");

        Mockito.doNothing().when(userDao).addUser(validUser);

        Mockito.when(jwtService.generateToken(validUser)).thenReturn("mockToken");

        UserDto invalidUser = new UserDto(); // Invalid user with missing email and password

        // Act & Assert - Valid User
        AuthResponse response = authService.registerUser(validUser);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("mockToken", response.getToken());
        Assertions.assertEquals(validUser.getEmail(), response.getEmail());

        Mockito.verify(passwordEncoder).encode(validUser.getPassword());
        Mockito.verify(userDao).addUser(validUser);
        Mockito.verify(jwtService).generateToken(validUser);

        // Act & Assert - Duplicate User
        Mockito.doThrow(new SQLException()).when(userDao).addUser(duplicateUser);
        Assertions.assertThrows(DuplicateUserException.class, () -> {
            authService.registerUser(duplicateUser);
        });

        Mockito.verify(passwordEncoder, Mockito.times(2)).encode(Mockito.anyString());
        Mockito.verify(userDao, Mockito.times(2)).addUser(duplicateUser);
        Mockito.verifyNoMoreInteractions(jwtService);

        // Act & Assert - Invalid User
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            authService.registerUser(invalidUser);
        });

        Mockito.verifyNoMoreInteractions(passwordEncoder);
        Mockito.verifyNoMoreInteractions(userDao);
        Mockito.verifyNoMoreInteractions(jwtService);
    }
}
