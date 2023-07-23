package com.flavourfit.Feeds.Comments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.ArrayList;

public class CommentServiceImpl implements ICommentsService{
    private static Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final ICommentsDao commentsDao;
    @Autowired
    public CommentServiceImpl(ICommentsDao commentsDao) {
        this.commentsDao = commentsDao;
    }
    @Override
    public ArrayList<CommentDto> getCommentsByFeeds(int feedId) throws SQLException {
        logger.info("Started method getCommentsByFeeds()");
        return commentsDao.getCommentsByFeedId(feedId);
    }


}
