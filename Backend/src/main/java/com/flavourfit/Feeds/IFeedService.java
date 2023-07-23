package com.flavourfit.Feeds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IFeedService {

    public FeedDto getFeedsByID(int feedId) throws SQLException;

    public int increaseFeedLikes(int feedId) throws SQLException;

    FeedDto removeCommentFromFeed(int commentId) throws SQLException;

    public List<FeedDto> getFeedsByUser(int userID, int offset) throws SQLException;

}
