package com.flavourfit.Feeds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IFeedDao {

    public FeedDto getFeedsById(int feetId) throws SQLException;

    public ArrayList<FeedDto> getFeedsByUser(int userId,int offset) throws SQLException;
}
