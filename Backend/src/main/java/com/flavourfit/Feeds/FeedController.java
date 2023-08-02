package com.flavourfit.Feeds;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Feeds.Comments.CommentDto;
import com.flavourfit.Feeds.Comments.ICommentsService;
import com.flavourfit.ResponsesDTO.GetResponse;
import com.flavourfit.ResponsesDTO.PutResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/feeds")
@CrossOrigin
public class FeedController {
    private static Logger logger = LoggerFactory.getLogger(FeedController.class);
    private IFeedService feedService;
    private IAuthService authService;
    private ICommentsService commentsService;

    @Autowired
    public FeedController(IFeedService feedService, IAuthService authService, ICommentsService commentsService) {
        this.feedService = feedService;
        this.authService = authService;
        this.commentsService = commentsService;
    }

    @GetMapping("/get-feedDetails")
    public ResponseEntity<GetResponse> fetchFeedsByID(@RequestParam("feedID") int feedID) {
        logger.info("Entered controller method fetchFeedsByID()");
        try {
            FeedDto feed = this.feedService.getFeedsByID(feedID);
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully retrieved feed", feed));
        } catch (Exception e) {
            logger.error("Failed to retrieve feed");
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to retrieve feed:" + e.getMessage()));
        }
    }

    @PatchMapping("/like-feeds")
    public ResponseEntity<GetResponse> updateLikesByFeedID(@RequestParam("feedID") int feedID) {
        logger.info("Entered controller method updateLikesByFeedID()");
        try {
            int updatedFeedLikes = this.feedService.increaseFeedLikes(feedID);
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully updated feed likes", updatedFeedLikes));
        } catch (Exception e) {
            logger.error("Failed to increase likes for feed");
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to update feed likes:" + e.getMessage()));
        }
    }

    @DeleteMapping("/comment-feed")
    public ResponseEntity<GetResponse> removeCommentsByFeedID(
            @RequestParam("feedID") int feedID, @RequestParam("commentID") int commentID
    ) {
        logger.info("Entered controller method removeCommentsByFeedID()");
        try {
            FeedDto feed = this.feedService.removeCommentFromFeed(feedID,commentID);
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully removed comment", feed));
        } catch (Exception e) {
            logger.error("Failed to remove the comment from feed");
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to remove comment:" + e.getMessage()));
        }
    }

    @GetMapping("/get-all-feeds")
    public ResponseEntity<GetResponse> getAllFeedsByUser(
            @RequestParam("offset") String offset, @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method getAllFeedsByUser()");
        int userId;

        try {
            userId = authService.extractUserIdFromToken(token);
        } catch (Exception e) {
            logger.error("Failed to retrieve feed");
            return ResponseEntity.badRequest().body(new GetResponse(false, "Token not valid" + e.getMessage()));
        }

        try {
            int offsetNumber = Integer.parseInt(offset);
            ArrayList<FeedDto> feeds = this.feedService.getFeedsByUser(userId, offsetNumber);
            logger.info("Retrieved all the feeds");
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully retrieved feed", feeds));
        } catch (Exception e) {
            logger.error("Failed to retrieve the feeds");
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to retrieve feed:" + e.getMessage()));

        }
    }

    @PutMapping("/record-post")
    public ResponseEntity<PutResponse> recordPost(
            @RequestBody FeedDto feedDto, @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method recordPost()");
        int userId;

        try {
            userId = authService.extractUserIdFromToken(token);
        } catch (Exception e) {
            logger.error("Failed to record feed: ", e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, "Token not valid" + e.getMessage()));
        }

        try {
            FeedDto updatedFeed = this.feedService.recordPost(feedDto, userId);
            logger.info("Recorded feed with id {}", updatedFeed.getFeedId());
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully recorded feed", updatedFeed));
        } catch (Exception e) {
            logger.error("Failed to record the feed");
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to record the feed:" + e.getMessage()));
        }
    }

    @PutMapping("/record-comment")
    public ResponseEntity<PutResponse> recordComment(
            @RequestBody CommentDto commentDto, @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method recordComment()");
        int userId;

        try {
            userId = authService.extractUserIdFromToken(token);
        } catch (Exception e) {
            logger.error("Failed to record comment: ", e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, "Token not valid"));
        }

        try {
            this.commentsService.recordComment(commentDto, userId);
            logger.info("Recorded comment");
            List<CommentDto> updatedComments = this.commentsService.getCommentsByFeeds(commentDto.getFeedId());
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully recorded comment", updatedComments));
        } catch (Exception e) {
            logger.error("Failed to record the comment");
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to record the comment:"));
        }
    }

    @PutMapping("/post-recipe")
    public ResponseEntity<PutResponse> postRecipeOnFeed(
            @RequestParam("recipeId") int recipeId, @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method postRecipeOnFeed()");
        int userId;

        try {
            userId = authService.extractUserIdFromToken(token);
        } catch (Exception e) {
            logger.error("Failed to record comment: ", e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, "Token not valid"));
        }

        try {
            logger.info("Posting recipe with id {} on feed", recipeId);
            FeedDto newFeed = this.feedService.postRecipe(recipeId, userId);
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully posted recipe", newFeed));
        } catch (Exception e) {
            logger.error("Failed to post recipe: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to post recipe: "));
        }
    }

}
