package com.flavourfit.Feeds;

import com.flavourfit.Feeds.Comments.CommentDto;
import com.flavourfit.Feeds.Comments.ICommentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class FeedServiceImpl implements IFeedService {
    private static Logger logger = LoggerFactory.getLogger(FeedServiceImpl.class);
    private final IFeedDao feedDao;
    private final ICommentsService commentsService;

    @Autowired
    public FeedServiceImpl(IFeedDao feedDao, ICommentsService commentsService) {
        this.feedDao = feedDao;
        this.commentsService = commentsService;
    }

    @Override
    public FeedDto getFeedsByID(int feedId) throws SQLException {
        logger.info("Started method getFeedsByID()");

        FeedDto feed = feedDao.getFeedsById(feedId);
        List<CommentDto> commentsForFeed = commentsService.getCommentsByFeeds(feedId);
        feed.setComments(commentsForFeed);

        return feed;
    }

    @Override
    public int increaseFeedLikes(int feedId) throws SQLException {
        logger.info("Started method increaseFeedLikes()");

        int updatedFeedLikes = feedDao.updateFeedLikes(feedId);
        return updatedFeedLikes;
    }

    @Override
    public FeedDto removeCommentFromFeed(int commentId) throws SQLException {
        logger.info("Started method removeCommentFromFeed()");

        FeedDto feed = feedDao.getFeedsById(commentId);
        int feedId = feed.getFeedId();
        boolean commentRemove = commentsService.removeCommentFromFeed(feedId, commentId);

        if (commentRemove) {
            List<CommentDto> updatedCommentsForFeed = commentsService.getCommentsByFeeds(feedId);
            feed.setComments(updatedCommentsForFeed);
        } else {
            logger.warn("Invalid commentId parameter");
            throw new RuntimeException("Invalid commentId");
        }

        return feed;
    }
}
