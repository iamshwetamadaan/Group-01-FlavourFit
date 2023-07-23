package com.flavourfit.Feeds.Comments;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Repository
public class CommentDaoImpl implements ICommentsDao {
    private static Logger logger = LoggerFactory.getLogger(CommentDaoImpl.class);
    private final IDatabaseManager database;
    private Connection connection;

    @Autowired
    public CommentDaoImpl() {
        this.database = DatabaseManagerImpl.getInstance();
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
    public ArrayList<CommentDto> getCommentsByFeedId(int feedId) throws SQLException {
        logger.info("Started getCommentsByFeedId() method");
        ArrayList<CommentDto> userFeeds = new ArrayList<CommentDto>();

        this.testConnection();

        logger.info("Running select query to get user by userId");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Comments WHERE Feed_id=? ORDER BY Comment_id DESC");
        preparedStatement.setInt(1, feedId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            userFeeds.add(this.extractCommentsFromResult(resultSet));
        }
        logger.info("Returning received user comments as response");
        return userFeeds;
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
        if (resultSet != null) {
            comments.setCommentID(resultSet.getInt("Comment_id"));
            comments.setCommentContent(resultSet.getString("Comment_content"));
            comments.setUserId(resultSet.getInt("User_id"));
            comments.setFeedId(resultSet.getInt("Feed_id"));
        }
        return comments;
    }
}
