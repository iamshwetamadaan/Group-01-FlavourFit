package com.flavourfit.Feeds.Comments;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Exceptions.FeedsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentDaoImpl implements ICommentsDao {
    private static Logger logger = LoggerFactory.getLogger(CommentDaoImpl.class);
    private final IDatabaseManager database;
    private Connection connection;

    @Autowired
    public CommentDaoImpl(DatabaseManagerImpl database) {
        this.database = database;
        if (this.database != null && this.database.getConnection() != null) {
            this.connection = this.database.getConnection();
        }
    }

    private void testConnection() throws SQLException {
        if (database == null && connection == null) {
            logger.error("SQL connection not found!");
            throw new SQLException("SQL connection not found!");
        }
        if (connection == null && this.database.getConnection() == null) {
            logger.error("SQL connection not found!");
            throw new SQLException("SQL connection not found!");
        } else {
            this.connection = this.database.getConnection();
        }
    }

    @Override
    public List<CommentDto> getCommentsByFeedId(int feedId) throws SQLException {
        logger.info("Started getCommentsByFeedId() method");

        List<CommentDto> commentsForFeedID = new ArrayList<>();

        this.testConnection();

        logger.info("Running select query to get comments for feedId");

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Comments WHERE Feed_id=? ORDER BY Comment_id DESC");
        preparedStatement.setInt(1, feedId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            commentsForFeedID.add(this.extractCommentsFromResult(resultSet));
            logger.info("Returning feed's list of comments as response");
        }

        return commentsForFeedID;
    }

    @Override
    public boolean removeCommentInFeed(int feedId, int commentId) throws SQLException {
        logger.info("Started deleteCommentInFeed() method");
        boolean commentDeleted = false;

        this.testConnection();

        logger.info("Running delete query to get comment for a specific feed");
        String query = "DELETE from Comments WHERE Comment_id=? AND Feed_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        logger.info("Replacing values in prepared statement with actual values to be inserted");
        preparedStatement.setInt(1, commentId);
        preparedStatement.setInt(2, feedId);
        logger.info("Execute the deletion of record to the table");
        int commentToBeDeleted = preparedStatement.executeUpdate();

        if (commentToBeDeleted > 0) {
            logger.info("Comment deleted successfully.");
            commentDeleted = true;
        } else {
            logger.error("Comment with the given comment ID not found to be deleted.");
        }

        logger.info("Returning boolean value to show whether feed's list of comments updated or not as response");
        return commentDeleted;
    }

    @Override
    public void addComment(CommentDto commentDto) throws SQLException {
        logger.info("Started addComment() method");

        if (commentDto.getCommentContent() == null || commentDto.getCommentContent().isEmpty()) {
            logger.error("Invalid data while adding comment");
            throw new FeedsException("Invalid comment");
        } else if (commentDto.getFeedId() == 0 || commentDto.getUserId() == 0) {
            logger.error("Invalid feed/user id while adding comment");
            throw new FeedsException("Invalid feed/user id");
        }

        this.testConnection();

        logger.info("Creating a prepared statement to insert record.");
        String query = "INSERT INTO Comments (Comment_content,Feed_id,User_id,Comment_username) VALUES (?,?,?,?)";

        logger.info("Replacing values in prepared statement with actual values to be inserted");
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setString(1, commentDto.getCommentContent());
        preparedStatement.setInt(2, commentDto.getFeedId());
        preparedStatement.setInt(3, commentDto.getUserId());
        preparedStatement.setString(4, commentDto.getUsername());

        logger.info("Execute the insertion of comment to the table");
        preparedStatement.executeUpdate();

        logger.info("Added comment with content: {}, by {}", commentDto.getCommentContent(), commentDto.getUsername());
    }

    @Override
    public void updateComment(CommentDto commentDto) throws SQLException {
        logger.info("Started updateComment() method");

        if (commentDto.getCommentContent() == null || commentDto.getCommentContent().isEmpty()) {
            logger.error("Invalid data while updating comment");
            throw new FeedsException("Invalid comment");
        } else if (commentDto.getCommentId() == 0) {
            logger.error("Invalid comment id while updating comment");
            throw new FeedsException("Invalid comment id");
        }

        this.testConnection();

        logger.info("Creating a prepared statement to update record.");
        String query = "UPDATE Comments SET Comment_content=? WHERE Comment_id=?";

        logger.info("Replacing values in prepared statement with actual values to be updated");
        PreparedStatement preparedStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, commentDto.getCommentContent());
        preparedStatement.setInt(2, commentDto.getCommentId());

        logger.info("Execute the update of comment to the table");
        preparedStatement.executeUpdate();

        logger.info("Updated comment with content: {}, by {}", commentDto.getCommentContent(), commentDto.getUsername());
    }

    private void replaceStatementPlaceholders(CommentDto commentDto, PreparedStatement preparedStatement) throws
            SQLException {
        if (commentDto == null || preparedStatement == null) {
            return;
        }

        preparedStatement.setString(1, commentDto.getCommentContent());
        preparedStatement.setString(2, commentDto.getUsername());
        preparedStatement.setInt(3, commentDto.getUserId());
    }

    private CommentDto extractCommentsFromResult(ResultSet resultSet) throws SQLException {
        CommentDto comments = new CommentDto();
        if (resultSet.next()) {
            comments.setCommentId(resultSet.getInt("Comment_id"));
            comments.setCommentContent(resultSet.getString("Comment_content"));
            comments.setUserId(resultSet.getInt("User_id"));
            comments.setFeedId(resultSet.getInt("Feed_id"));
            comments.setUsername(resultSet.getString("Comment_username"));
        }
        return comments;
    }
}
