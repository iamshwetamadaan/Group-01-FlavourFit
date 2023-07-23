package com.flavourfit.Feeds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedServiceImpl implements IFeedService {
    private static Logger logger = LoggerFactory.getLogger(FeedServiceImpl.class);

    private final IFeedDao feedDao;
    private final IFeedService feedsService;

    public FeedServiceImpl(IFeedDao feedDao, IFeedService feedsService) {
        this.feedDao = feedDao;
        this.feedsService = feedsService;
    }

    @Override
    public List<String> fetchAllFeeds() throws SQLException {
        logger.info("Started method fetchAllFeeds()");
        return feedDao.getAllFeeds();
    }

    @Override
    public ArrayList<FeedDto> getFeedsByUser(int count, int userId) throws SQLException {
        logger.info("Started method getFeedsByUser()");
        return feedDao.getFeedsById(userId);
    }
}
