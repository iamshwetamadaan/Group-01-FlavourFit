package com.flavourfit.Feeds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IFeedDao {
    List<String> getAllFeeds() throws SQLException;

    ArrayList<FeedDto> getFeedsById(int userId) throws SQLException;
}
