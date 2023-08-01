package com.flavourfit.Feeds.Comments;

import com.flavourfit.Exceptions.FeedsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentsService {
    private static Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final ICommentsDao commentsDao;

    @Autowired
    public CommentServiceImpl(ICommentsDao commentsDao) {
        this.commentsDao = commentsDao;
    }

    @Override
    public List<CommentDto> getCommentsByFeeds(int feedId) throws SQLException {
        logger.info("Started method getCommentsByFeeds()");
        List<CommentDto> comments = this.commentsDao.getCommentsByFeedId(feedId);
        return comments;
    }

    @Override
    public boolean removeCommentFromFeed(int feedID, int commentID) throws SQLException {
        logger.info("Started method deleteCommentFromFeed()");
        return this.commentsDao.removeCommentInFeed(feedID, commentID);
    }

    @Override
    public void recordComment(CommentDto commentDto, int userId) throws FeedsException {
        logger.info("Started method recordComment()");
        try {
            if (userId == 0) {
                logger.error("Invalid userId {} while recording comment", userId);
                throw new FeedsException("Invalid userId while recording comment");
            }

            commentDto.setUserId(userId);

            if (commentDto.getCommentId() != 0) {
                logger.info("Updating comment with id {}", commentDto.getCommentId());
                this.commentsDao.updateComment(commentDto);
            } else {
                logger.info("Adding new comment by {}", commentDto.getUsername());
                this.commentsDao.addComment(commentDto);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
