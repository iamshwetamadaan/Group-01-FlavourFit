package com.flavourfit.Authentication;

import com.flavourfit.ResponsesDTO.AuthResponse;
import com.flavourfit.User.UserDto;

public interface IAuthService {
    AuthResponse authenticateUser(UserDto user);

    AuthResponse registerUser(UserDto userDto);

    int extractUserIdFromToken(String token);
}
