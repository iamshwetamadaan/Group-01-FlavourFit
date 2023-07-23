package com.flavourfit.Feeds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IFeedService {
    public List<String> fetchAllFeeds() throws SQLException;

    public ArrayList<FeedDto> getFeedsByUser(int count, int userId) throws SQLException;
}
