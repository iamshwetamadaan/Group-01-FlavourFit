package com.flavourfit.Feeds;

import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Feeds.Comments.CommentDto;
import com.flavourfit.Feeds.Comments.ICommentsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedDaoImpl implements IFeedDao {
    private static Logger logger = LoggerFactory.getLogger(FeedDaoImpl.class);
    private final IDatabaseManager database;
    private Connection connection;

    private ICommentsDao commentsDao;

    @Autowired
    public FeedDaoImpl(IDatabaseManager database) {
        this.database = database;
        this.connection = database.getConnection();
    }

    @Override
    public List<String> getAllFeeds() throws SQLException {
        logger.info("Started getAllFeeds() method");
        List<String> allFeeds = new ArrayList<>();
        this.testConnection();

        Statement statement = connection.createStatement();
        logger.info("Running query to fetch different types from the recipes");
        ResultSet resultset = statement.executeQuery("SELECT Feed_Content FROM Feeds;");
        while (resultset.next()) {
            allFeeds.add(resultset.getString("Feed_Content"));
        }
        logger.info("Received data from db and added types to allFeeds list.");
        return allFeeds;
    }

    @Override
    public ArrayList<FeedDto> getFeedsById(int userId) throws SQLException {
        logger.info("Started getFeedsById() method");
        ArrayList<FeedDto> userFeeds = null;

        this.testConnection();

        logger.info("Running select query to get user by userId");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Feeds WHERE User_id=?");
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            userFeeds.add(this.extractUserFeedsFromResult(resultSet));
        }
        logger.info("Returning received user feeds as response");
        return userFeeds;
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

    private void replaceStatementPlaceholders(FeedDto feed, PreparedStatement preparedStatement) throws
                                                                                                     SQLException {
        if (feed == null || preparedStatement == null) {
            return;
        }

        preparedStatement.setString(1, feed.getFeedContent());
        preparedStatement.setInt(2, feed.getLikeCount());
        preparedStatement.setArray(3, (Array) feed.getComments());
    }

    private FeedDto extractUserFeedsFromResult(ResultSet resultSet) throws SQLException {
        FeedDto userFeeds = new FeedDto();
        if (resultSet != null) {
            userFeeds.setFeedId(resultSet.getInt("Feed_id"));
            userFeeds.setFeedContent(resultSet.getString("Feed_content"));
            userFeeds.setLikeCount(resultSet.getInt("Like_count"));
            userFeeds.setUserId(resultSet.getInt("User_id"));
            userFeeds.setComments(new ArrayList<CommentDto>());
        }
        return userFeeds;
    }
}
