package com.flavourfit.Feeds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IFeedDao {

    public FeedDto getFeedsById(int feetId) throws SQLException;


    int updateFeedLikes(int feedId) throws SQLException;

    public List<FeedDto> getFeedsByUser(int userId,int offset) throws SQLException;

}
