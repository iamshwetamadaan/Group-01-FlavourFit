package com.flavourfit.User;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    IUserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }
}
