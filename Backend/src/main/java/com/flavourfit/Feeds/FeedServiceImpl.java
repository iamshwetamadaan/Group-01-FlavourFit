package com.flavourfit.Feeds;

import com.flavourfit.Exceptions.FeedsException;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.Feeds.Comments.CommentDto;
import com.flavourfit.Feeds.Comments.ICommentsService;
import com.flavourfit.Helpers.FeedHelper;
import com.flavourfit.Recipes.CompleteRecipeDto;
import com.flavourfit.Recipes.IRecipeService;
import com.flavourfit.Recipes.Ingredients.IIngredientsService;
import com.flavourfit.Recipes.Ingredients.IngredientDto;
import com.flavourfit.Recipes.RecipeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedServiceImpl implements IFeedService {
    private static Logger logger = LoggerFactory.getLogger(FeedServiceImpl.class);
    private final IFeedDao feedDao;
    private final ICommentsService commentsService;
    private final IRecipeService recipeService;
    private final IIngredientsService ingredientsService;

    @Autowired
    public FeedServiceImpl(
            IFeedDao feedDao, ICommentsService commentsService, IRecipeService recipeService,
            IIngredientsService ingredientsService
    ) {
        this.feedDao = feedDao;
        this.commentsService = commentsService;
        this.recipeService = recipeService;
        this.ingredientsService = ingredientsService;
    }

    @Override
    public FeedDto getFeedsByID(int feedId) throws SQLException {
        logger.info("Started method getFeedsByID()");
        FeedDto feed = feedDao.getFeedsById(feedId);
        List<CommentDto> commentsForFeed = commentsService.getCommentsByFeeds(feedId);
        feed.setComments(commentsForFeed);
        return feed;
    }

    @Override
    public int increaseFeedLikes(int feedId) throws SQLException {
        logger.info("Started method increaseFeedLikes()");

        int updatedFeedLikes = feedDao.updateFeedLikes(feedId);
        return updatedFeedLikes;
    }

    @Override
    public FeedDto removeCommentFromFeed(int feedId, int commentId) throws SQLException {
        logger.info("Started method removeCommentFromFeed()");

        FeedDto feed = feedDao.getFeedsById(feedId);

        boolean commentRemove = commentsService.removeCommentFromFeed(feedId, commentId);

        if (commentRemove) {
            List<CommentDto> updatedCommentsForFeed = commentsService.getCommentsByFeeds(feedId);
            feed.setComments(updatedCommentsForFeed);
        } else {
            logger.warn("Invalid commentId parameter");
            throw new RuntimeException("Invalid commentId");
        }

        return feed;
    }

    @Override
    public ArrayList<FeedDto> getFeedsByUser(int userID, int offset) throws SQLException {
        logger.info("Started getFeedsByUser method()");
        ArrayList<FeedDto> feeds = feedDao.getFeedsByUser(userID, offset);

        logger.info("Received the feeds");
        for (FeedDto feed : feeds) {
            List<CommentDto> commentsForFeed = commentsService.getCommentsByFeeds(feed.getFeedId());
            feed.setComments(commentsForFeed);
        }

        logger.info("Receiving the feeds");
        return feeds;
    }

    @Override
    public FeedDto recordPost(FeedDto feedDto, int userId) throws FeedsException {
        logger.info("Started recordPost method()");
        try {
            if (userId == 0) {
                logger.error("Invalid userId for feed");
                throw new FeedsException("Invalid userId for feed");
            }

            feedDto.setUserId(userId);
            int feedId = 0;
            if (feedDto.getFeedId() != 0) {
                logger.info("Updating existing feed with id {}", feedDto.getFeedId());
                this.feedDao.updatePost(feedDto);
                feedId = feedDto.getFeedId();
            } else {
                logger.info("Adding new feed post");
                feedId = this.feedDao.addPost(feedDto);
                logger.info("Added feed with id {}", feedId);
            }

            logger.info("Fetching feed with id {}", feedDto.getFeedId());
            FeedDto updatedFeed = this.getFeedsByID(feedId);
            return updatedFeed;
        } catch (SQLException e) {
            throw new FeedsException(e);
        }
    }

    @Override
    public FeedDto postRecipe(int recipeId, int userId) throws FeedsException {
        logger.info("Started postRecipe method()");
        if (recipeId == 0) {
            logger.error("Invalid recipe");
            throw new FeedsException("Invalid recipe");
        }

        if (userId == 0) {
            logger.error("Invalid user");
            throw new UserNotFoundException("Invalid user");
        }

        logger.info("Fetching recipe with id {}", recipeId);
        CompleteRecipeDto recipe = this.recipeService.fetchRecipeByRecipeId(recipeId);

        logger.info("Fetching ingredients for recipe with id {}", recipeId);
        List<IngredientDto> ingredients = recipe.getIngredients();

        logger.info("Converting recipe to feed");
        FeedDto feedDto = FeedHelper.convertRecipeToFeed(recipe.getRecipe(), ingredients);
        feedDto.setUserId(userId);

        logger.info("Recording recipe post to feed");
        FeedDto newFeed = this.recordPost(feedDto, userId);

        return newFeed;
    }
}
