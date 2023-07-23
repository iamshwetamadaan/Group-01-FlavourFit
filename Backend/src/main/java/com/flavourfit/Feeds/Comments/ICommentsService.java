package com.flavourfit.Feeds.Comments;

import java.sql.SQLException;
import java.util.List;

public interface ICommentsService {
    public List<CommentDto> getCommentsByFeeds(int feedId) throws SQLException;

    public boolean removeCommentFromFeed(int feedID, int commentID)  throws SQLException;
}
