package com.flavourfit.Feeds;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Feeds.Comments.CommentDto;
import com.flavourfit.Feeds.Comments.ICommentsService;
import com.flavourfit.ResponsesDTO.GetResponse;
import com.mysql.cj.xdevapi.JsonString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FeedControllerTest {

    @Mock
    private IFeedService feedService;

    @Mock
    private IAuthService authService;

    @Mock
    private ICommentsService commentsService;

    @InjectMocks
    private FeedController feedController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(feedController).build();
    }

    @Test
    public void fetchFeedsByIDTest() throws Exception {
        int feedID = 1;
        FeedDto mockFeed = new FeedDto(); // Create a mock FeedDto object
        when(feedService.getFeedsByID(feedID)).thenReturn(mockFeed);
        mockMvc.perform(get("/feeds/get-feedDetails")
                        .param("feedID", String.valueOf(feedID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully retrieved feed"))
                .andExpect(jsonPath("$.data").exists());

        //Fail case
        when(feedService.getFeedsByID(feedID)).thenThrow(new RuntimeException("Feed not found"));
        mockMvc.perform(get("/feeds/get-feedDetails")
                        .param("feedID", String.valueOf(feedID)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Failed to retrieve feed:Feed not found"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    public void updateLikesByFeedIDTest() throws Exception {
        int feedID = 1;
        int updatedFeedLikes = 10;
        when(feedService.increaseFeedLikes(feedID)).thenReturn(updatedFeedLikes);
        mockMvc.perform(patch("/feeds/like-feeds")
                        .param("feedID", String.valueOf(feedID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully updated feed likes"))
                .andExpect(jsonPath("$.data").value(updatedFeedLikes));

        //Fail case
        when(feedService.increaseFeedLikes(feedID)).thenThrow(new RuntimeException("Failed to increase likes for feed"));
        mockMvc.perform(patch("/feeds/like-feeds")
                        .param("feedID", String.valueOf(feedID)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Failed to update feed likes:Failed to increase likes for feed"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    public void removeCommentsByFeedIDTest() throws Exception {
        int feedID = 1;
        int commentID = 1;
        FeedDto mockFeed = new FeedDto();
        when(feedService.removeCommentFromFeed(feedID,commentID)).thenReturn(mockFeed);
        mockMvc.perform(delete("/feeds/comment-feed")
                        .param("feedID", String.valueOf(feedID))
                        .param("commentID", String.valueOf(commentID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully removed comment"))
                .andExpect(jsonPath("$.data").exists());

        //Fail case
        when(feedService.removeCommentFromFeed(feedID,commentID)).thenThrow(new RuntimeException("Failed to remove the comment from feed"));
        mockMvc.perform(delete("/feeds/comment-feed")
                        .param("feedID", String.valueOf(feedID))
                        .param("commentID", String.valueOf(commentID)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Failed to remove comment:Failed to remove the comment from feed"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    public void getAllFeedsByUserTest() throws Exception {
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

    @Test
    public void recordPostTest() throws Exception {
        int userId = 1;
        String token = "dummyToken";
        FeedDto feedDto = new FeedDto();
        FeedDto updatedFeed = new FeedDto();
        when(authService.extractUserIdFromToken(token)).thenReturn(userId);

        //Invalid Token
        mockMvc.perform(put("/feeds/record-post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(feedDto))
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Failed to record the feed:Cannot invoke \"com.flavourfit.Feeds.FeedDto.getFeedId()\" because \"updatedFeed\" is null"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void recordCommentTest() throws Exception {
        int userId = 1;
        String token = "dummyToken";
        CommentDto commentDto = new CommentDto();
        List<CommentDto> updatedComments = new ArrayList<>();
        when(authService.extractUserIdFromToken(token)).thenReturn(userId);
        when(commentsService.getCommentsByFeeds(commentDto.getFeedId())).thenReturn(updatedComments);

        mockMvc.perform(put("/feeds/record-comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(commentDto))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully recorded comment"))
                .andExpect(jsonPath("$.data").exists());

        //Invalid Token
        token = "invalidToken";
        when(authService.extractUserIdFromToken(token)).thenThrow(new RuntimeException("Token not valid"));
        mockMvc.perform(put("/feeds/record-comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(commentDto))
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Token not valid"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void postRecipeOnFeedTest() throws Exception {
        int userId = 1;
        String token = "dummyToken";
        int recipeId = 123;
        FeedDto newFeed = new FeedDto(); // Create a mock FeedDto object
        when(authService.extractUserIdFromToken(token)).thenReturn(userId);
        when(feedService.postRecipe(recipeId, userId)).thenReturn(newFeed);

        mockMvc.perform(put("/feeds/post-recipe")
                        .param("recipeId", String.valueOf(recipeId))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully posted recipe"))
                .andExpect(jsonPath("$.data").exists());

        //Invalid Token
        token = "invalidToken";
        when(authService.extractUserIdFromToken(token)).thenThrow(new RuntimeException("Token not valid"));
        mockMvc.perform(put("/feeds/post-recipe")
                        .param("recipeId", String.valueOf(recipeId))
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Token not valid"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
