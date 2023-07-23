package com.flavourfit.Feeds;

import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Feeds.Comments.CommentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
public class FeedDaoImpl implements IFeedDao {
    private static Logger logger = LoggerFactory.getLogger(FeedDaoImpl.class);
    private final IDatabaseManager database;
    private Connection connection;

    @Autowired
    public FeedDaoImpl(IDatabaseManager database) {
        this.database = database;
        this.connection = database.getConnection();
    }

    @Override
    public FeedDto getFeedsById(int feedID) throws SQLException {
        logger.info("Started getFeedsById() method");
        FeedDto userFeeds = null;

        this.testConnection();

        logger.info("Running select query to get user by userId");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Feeds WHERE Feed_id=?");
        preparedStatement.setInt(1, feedID);
        ResultSet resultSet = preparedStatement.executeQuery();

        userFeeds = this.extractUserFeedsFromResult(resultSet);

        logger.info("Returning received user feeds as response");
        return userFeeds;
    }

    @Override
    public int updateFeedLikes(int feedId) throws SQLException {

        int likesUpdated = 0;
        logger.info("Started updateFeedLikes() method");

        if (database != null) {
            Connection connection = this.database.getConnection();

            if (connection == null) {
                logger.error("SQL connection not found!");
                throw new SQLException("SQL connection not found!");
            }

            logger.info("Creating a prepared statement to first get the record for which like have to increase.");
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * from Feeds WHERE Feed_id=?");
            preparedStatement1.setInt(1, feedId);
            ResultSet resultSet = preparedStatement1.executeQuery();

            likesUpdated = this.extractUserFeedsFromResult(resultSet).getLikeCount();
            likesUpdated =+ 1;

            logger.info("Creating a prepared statement to update record.");
            String query = "UPDATE Feeds SET like_count = ? where Feed_id = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            logger.info("Replacing values in prepared statement with actual values to be inserted");
            preparedStatement2.setInt(1, likesUpdated);
            preparedStatement2.setInt(2, feedId);
            logger.info("Execute the update of record to the table");
            preparedStatement2.executeUpdate();

            ResultSet keys = preparedStatement2.getGeneratedKeys();
            long updatedLikesFeedID;
            while (keys.next()) {
                updatedLikesFeedID = keys.getLong(1);
                logger.info("Updated likes with feedId: {}, to the Feeds table!", updatedLikesFeedID);
            }
        }

        return likesUpdated;
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
        if (resultSet.next()) {
            userFeeds.setFeedId(resultSet.getInt("Feed_id"));
            userFeeds.setFeedContent(resultSet.getString("Feed_content"));
            userFeeds.setLikeCount(resultSet.getInt("Like_count"));
            userFeeds.setUserId(resultSet.getInt("User_id"));
            userFeeds.setComments(new ArrayList<CommentDto>());
        }
        return userFeeds;
    }

    private List<FeedDto> getUserFeedsList(ResultSet resultSet) throws SQLException {
        List<FeedDto> userFeedsList = new ArrayList<>();
        while (resultSet.next()) {
            FeedDto userFeeds = new FeedDto();
            userFeeds.setFeedId(resultSet.getInt("Feed_id"));
            userFeeds.setFeedContent(resultSet.getString("Feed_content"));
            userFeeds.setLikeCount(resultSet.getInt("Like_count"));
            userFeeds.setUserId(resultSet.getInt("User_id"));
            userFeeds.setComments(new ArrayList<CommentDto>());
            userFeedsList.add(userFeeds);

        }
        return userFeedsList;
    }
}
