package com.flavourfit.Feeds;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.ResponsesDTO.GetResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class FeedControllerTest {

    @InjectMocks
    FeedController feedController;

    @Mock
    IFeedService feedService;

    @Mock
    IAuthService authService;

    @BeforeEach
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllFeedsByUserTest() throws SQLException {
        String offset = "10";
        String token = "valid_token";
        int userId = 1;
        int offsetNumber = Integer.parseInt(offset);

        ArrayList<FeedDto> expectedFeeds = new ArrayList<>();

        when(authService.extractUserIdFromToken(token)).thenReturn(userId); // Assuming the token is valid
        when(feedService.getFeedsByUser(userId, offsetNumber)).thenReturn(expectedFeeds);

        ResponseEntity<GetResponse> response = feedController.getAllFeedsByUser(offset, token);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof GetResponse);
        GetResponse getResponse = response.getBody();
        assertTrue(getResponse.isSuccess());
        assertEquals("Successfully retrieved feed", getResponse.getMessage());
        assertEquals(expectedFeeds, getResponse.getData());
    }
}
