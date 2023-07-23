package com.flavourfit.Feeds;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Feeds.Comments.ICommentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
