package com.flavourfit.Feeds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IFeedService {

    public FeedDto getFeedsByID(int feedId) throws SQLException;

    public ArrayList<FeedDto> getFeedsByUser(int userID, int offset) throws SQLException;
}
