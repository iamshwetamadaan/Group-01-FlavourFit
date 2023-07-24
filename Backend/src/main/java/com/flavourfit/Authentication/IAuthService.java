package com.flavourfit.Authentication;

import com.flavourfit.Exceptions.AuthException;
import com.flavourfit.ResponsesDTO.AuthResponse;
import com.flavourfit.User.UserDto;

public interface IAuthService {
    AuthResponse authenticateUser(UserDto user);

    AuthResponse registerUser(UserDto userDto);

    int extractUserIdFromToken(String token);

    void sendOtpMail(String email) throws AuthException;
}
