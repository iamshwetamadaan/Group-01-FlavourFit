package com.flavourfit.Feeds;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Recipes.IRecipeService;
import com.flavourfit.Recipes.RecipeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feeds")
public class FeedController {

    private static Logger logger = LoggerFactory.getLogger(RecipeController.class);
    private IRecipeService recipeService;

    private IAuthService authService;

}
