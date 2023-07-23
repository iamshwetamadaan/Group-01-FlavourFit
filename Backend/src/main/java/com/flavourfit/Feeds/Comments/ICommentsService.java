package com.flavourfit.Feeds.Comments;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ICommentsService {
    public ArrayList<CommentDto> getCommentsByFeeds(int feedId) throws SQLException;
}
