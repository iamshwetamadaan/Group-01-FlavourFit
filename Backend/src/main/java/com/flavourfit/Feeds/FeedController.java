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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
