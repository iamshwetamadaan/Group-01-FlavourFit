package com.flavourfit.Recipes;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.RecipeExceptions;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.Recipes.SavedRecipes.ISavedRecipesService;
import com.flavourfit.ResponsesDTO.PutResponse;
import com.flavourfit.User.IUserService;
import com.flavourfit.User.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private static Logger logger = LoggerFactory.getLogger(RecipeController.class);
    private IRecipeService recipeService;

    private IAuthService authService;
    private final ISavedRecipesService savedRecipesService;

    @Autowired
    public RecipeController(IRecipeService recipeService, IAuthService authService, ISavedRecipesService savedRecipesService) {
        this.recipeService = recipeService;
        this.authService = authService;
        this.savedRecipesService = savedRecipesService;
    }

    @GetMapping("/types")
    public ResponseEntity<PutResponse> fetchAllRecipeTypes() {
        logger.info("Started fetchAllRecipeTypes() method");
        try {
            List<String> recipeDtoList = recipeService.fetchAllRecipeTypes();
            logger.info("Successfully collected  recipe types.");
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully retrieved recipe types", recipeDtoList));
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<PutResponse> recordRecipe(
            @RequestBody CompleteRecipeDto recipe, @RequestHeader("Authorization") String token
    ) {
        logger.info("Started recordRecipe() method");

        try {
            int userId = authService.extractUserIdFromToken(token);
            CompleteRecipeDto addedRecipe = this.recipeService.recordRecipe(recipe, userId);

            logger.info("Added recipe for userId:{}", userId);
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully added recipe", addedRecipe));
        } catch (RuntimeException e) {
            logger.error("Failed to add recipe");
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to add recipe:" + e.getMessage()));
        }
    }

    @PostMapping("/save-recipe")
    public ResponseEntity<PutResponse> saveRecipe(
            @RequestBody int recipeId, @RequestHeader("Authorization") String token
    ) {
        logger.info("Entered controller method saveRecipe()");
        int userId = authService.extractUserIdFromToken(token);

        try {
            this.savedRecipesService.saveRecipe(recipeId, userId);
            logger.info("Saved recipe for userId:{}", userId);
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully saved recipe"));
        } catch (RecipeExceptions e) {
            logger.error("Failed to Save recipe for userId:{}", userId);
            return ResponseEntity.internalServerError().body(new PutResponse(true, "Failed to save recipe"));
        }
    }
}
