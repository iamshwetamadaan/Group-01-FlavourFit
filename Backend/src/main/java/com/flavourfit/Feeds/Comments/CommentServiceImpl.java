package com.flavourfit.Feeds.Comments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentsService{
    private static Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final ICommentsDao commentsDao;
    @Autowired
    public CommentServiceImpl(ICommentsDao commentsDao) {
        this.commentsDao = commentsDao;
    }
    @Override
    public List<CommentDto> getCommentsByFeeds(int feedId) throws SQLException {
        logger.info("Started method getCommentsByFeeds()");
        return commentsDao.getCommentsByFeedId(feedId);
    }
    @Override
    public boolean removeCommentFromFeed(int feedID, int commentID) throws SQLException {
        logger.info("Started method deleteCommentFromFeed()");
        return this.commentsDao.removeCommentInFeed(feedID, commentID);
    }
}
