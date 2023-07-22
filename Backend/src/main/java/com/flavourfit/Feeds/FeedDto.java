package com.flavourfit.Feeds;

public class FeedDto {
    private int feedId;
    private String feedContent;
    private int likeCount;

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

    @Override
    public String toString() {
        return "RecipeDto{" +
                "feedId=" + feedId +
                ", feedContent='" + feedContent + '\'' +
                ", likes='" + likeCount + '\'' +
                '}';
    }
}
