package com.flavourfit.Feeds.Comments;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ICommentsDao {
    public ArrayList<CommentDto> getCommentsByFeedId(int feedId) throws SQLException;
}
