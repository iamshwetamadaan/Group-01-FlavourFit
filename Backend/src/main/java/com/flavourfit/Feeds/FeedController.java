package com.flavourfit.Feeds;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.CalorieHistoryException;
import com.flavourfit.Feeds.Comments.ICommentsService;
import com.flavourfit.ResponsesDTO.GetResponse;
import com.flavourfit.Trackers.Calories.CalorieGraphDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/feeds")
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

    @DeleteMapping("comment-feed")
    public ResponseEntity<GetResponse> removeCommentsByFeedID(@RequestParam("feedID") int feedID, @RequestParam("commentID") int commentID) {
        logger.info("Entered controller method removeCommentsByFeedID()");
        try {
            FeedDto feed = this.feedService.removeCommentFromFeed(commentID);
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully removed comment"));
        } catch (Exception e) {
            logger.error("Failed to remove the comment from feed");
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to remove comment:" + e.getMessage()));
        }
    }

}
