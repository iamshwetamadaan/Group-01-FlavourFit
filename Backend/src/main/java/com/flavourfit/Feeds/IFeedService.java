package com.flavourfit.Feeds;

import com.flavourfit.Exceptions.FeedsException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IFeedService {

    public FeedDto getFeedsByID(int feedId) throws SQLException;

    public int increaseFeedLikes(int feedId) throws SQLException;

    FeedDto removeCommentFromFeed(int feedId, int commentId) throws SQLException;

    public ArrayList<FeedDto> getFeedsByUser(int userID, int offset) throws SQLException;

    FeedDto recordPost(FeedDto feedDto, int userId) throws FeedsException;

    FeedDto postRecipe(int recipeId, int userId) throws FeedsException;
}
