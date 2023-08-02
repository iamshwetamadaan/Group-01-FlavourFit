package com.flavourfit.Feeds;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
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
    public FeedDaoImpl(DatabaseManagerImpl database) {
        this.database = database;
        if (this.database != null && this.database.getConnection() != null) {
            this.connection = this.database.getConnection();
        }
    }

    @Override
    public FeedDto getFeedsById(int feedID) throws SQLException {
        logger.info("Started getFeedsById() method");
        FeedDto userFeeds = new FeedDto();
        this.testConnection();

        logger.info("Running select query to get feeds by feedId");

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Feeds WHERE Feed_id=?");
        preparedStatement.setInt(1, feedID);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet!=null) {
             userFeeds = this.extractUserFeedsFromResult(resultSet);
        }


        logger.info("Returning received user feeds as response");
        return userFeeds;
    }

    @Override
    public int updateFeedLikes(int feedId) throws SQLException {
        logger.info("Started updateFeedLikes() method");

        this.testConnection();

        logger.info("Creating a prepared statement to first get the record for which like have to increase.");
        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * from Feeds WHERE Feed_id=?");
        preparedStatement1.setInt(1, feedId);
        ResultSet resultSet = preparedStatement1.executeQuery();

        int likesUpdated = this.extractUserFeedsFromResult(resultSet).getLikeCount() + 1;

        logger.info("Creating a prepared statement to update record.");
        String query = "UPDATE Feeds SET like_count = ? where Feed_id = ?";
        PreparedStatement preparedStatement2 = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        logger.info("Replacing values in prepared statement with actual values to be inserted");
        preparedStatement2.setInt(1, likesUpdated);
        preparedStatement2.setInt(2, feedId);
        logger.info("Execute the update of record to the table");
        preparedStatement2.executeUpdate();

        logger.info("Updated likes with feedId: {}, to the Feeds table!", feedId, likesUpdated);

        return feedId;
    }

    @Override
    public ArrayList<FeedDto> getFeedsByUser(int userId, int offset) throws SQLException {
        logger.info("Started getFeedsByUser() method");
        ArrayList<FeedDto> userFeeds = new ArrayList<FeedDto>();

        this.testConnection();

        logger.info("Running select query to get feeds by user");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Feeds limit 100 offset ?");
        preparedStatement.setInt(1, offset);
//        preparedStatement.setInt(2, offset);
        ResultSet resultSet = preparedStatement.executeQuery();


        logger.info("Obtained the result of select query");
        while (resultSet.next()) {
            FeedDto feed = new FeedDto();
            feed = this.extractUserFeedsFromResult(resultSet);
            userFeeds.add(feed);
        }

        logger.info("Returning received user feeds as response");
        return userFeeds;
    }

    @Override
    public int addPost(FeedDto feed) throws SQLException {
        logger.info("Started addPost() method");


        if (feed == null || feed.getFeedContent() == null || feed.getUserId() == 0) {
            logger.error("Feed object not valid!!");
            throw new SQLException("Feed object not valid!!");
        }

        this.testConnection();

        logger.info("Creating a prepared statement to insert record.");
        String query = "INSERT INTO Feeds (Feed_content,User_id) "
                + " VALUES(?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        logger.info("Replacing values in prepared statement with actual values to be inserted");
        preparedStatement.setString(1, feed.getFeedContent());
        preparedStatement.setInt(2, feed.getUserId());
        logger.info("Execute the insertion of record to the table");
        preparedStatement.executeUpdate();

        ResultSet keys = preparedStatement.getGeneratedKeys();
        long newFeedId = 0;
        while (keys.next()) {
            newFeedId = keys.getLong(1);
            logger.info("Added feed with feedId: {}, to the Feeds table!", newFeedId);
        }
        return (int) newFeedId;
    }

    @Override
    public void updatePost(FeedDto feed) throws SQLException {
        logger.info("Started updatePost() method");


        if (feed == null || feed.getFeedContent() == null || feed.getUserId() == 0 || feed.getFeedId() == 0) {
            logger.error("Feed object not valid!!");
            throw new SQLException("Feed object not valid!!");
        }

        this.testConnection();

        logger.info("Creating a prepared statement to insert record.");
        String query = "UPDATE Feeds SET Feed_content=?, Like_count=? WHERE Feed_Id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        logger.info("Replacing values in prepared statement with actual values to be inserted");
        preparedStatement.setString(1, feed.getFeedContent());
        preparedStatement.setInt(2, feed.getLikeCount());
        preparedStatement.setInt(3, feed.getFeedId());
        logger.info("Execute the update of record to the table");
        preparedStatement.executeUpdate();
        logger.info("Updated feed with feedId: {}, to the Feeds table!", feed.getFeedId());
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

    private void replaceStatementPlaceholders(FeedDto feed, PreparedStatement preparedStatement) throws SQLException {
        if (feed == null || preparedStatement == null) {
            return;
        }

        preparedStatement.setString(1, feed.getFeedContent());
        preparedStatement.setInt(2, feed.getLikeCount());
        preparedStatement.setArray(3, (Array) feed.getComments());
    }

    public FeedDto extractUserFeedsFromResult(ResultSet resultSet) throws SQLException {
        FeedDto userFeeds = new FeedDto();
        if (resultSet.next()) {
            userFeeds.setFeedId(resultSet.getInt("Feed_id"));
            userFeeds.setFeedContent(resultSet.getString("Feed_content"));
            userFeeds.setLikeCount(resultSet.getInt("Like_count"));
            userFeeds.setUserId(resultSet.getInt("User_id"));
            userFeeds.setComments(new ArrayList<CommentDto>());
            return userFeeds;
        }
        return null;
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
