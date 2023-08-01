package com.flavourfit.Feeds;

import com.flavourfit.Feeds.Comments.CommentDto;

import java.util.ArrayList;
import java.util.List;

public class FeedDto {
    private int feedId;
    private String feedContent;
    private int likeCount;
    private List<CommentDto> comments = new ArrayList<>();
    private int userId;
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    public String getFeedContent() {
        return feedContent;
    }

    public void setFeedContent(String feedContent) {
        this.feedContent = feedContent;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {

        return "FeedDto {" +
                "feedId=" + feedId +
                ", feedContent='" + feedContent + '\'' +
                ", likes='" + likeCount + '\'' +
                ", comments='" + comments.toString() +
                '}';
    }
}
