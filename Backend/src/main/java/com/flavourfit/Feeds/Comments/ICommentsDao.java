package com.flavourfit.Feeds.Comments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ICommentsDao {
    public List<CommentDto> getCommentsByFeedId(int feedId) throws SQLException;

    public boolean removeCommentInFeed(int feedId, int commentId) throws SQLException;

    void addComment(CommentDto commentDto) throws SQLException;

    void updateComment(CommentDto commentDto) throws SQLException;
}
