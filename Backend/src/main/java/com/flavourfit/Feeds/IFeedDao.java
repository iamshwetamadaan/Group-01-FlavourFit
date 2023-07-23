package com.flavourfit.Feeds;

import java.sql.SQLException;
import java.util.List;

public interface IFeedDao {
    List<String> getAllFeeds() throws SQLException;
}
