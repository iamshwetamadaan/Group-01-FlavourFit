package com.flavourfit.Feeds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IFeedDao {

    public FeedDto getFeedsById(int feetId) throws SQLException;


    int updateFeedLikes(int feedId) throws SQLException;

    public ArrayList<FeedDto> getFeedsByUser(int userId,int offset) throws SQLException;

    int addPost(FeedDto feed) throws SQLException;

    void updatePost(FeedDto feed) throws SQLException;
}
