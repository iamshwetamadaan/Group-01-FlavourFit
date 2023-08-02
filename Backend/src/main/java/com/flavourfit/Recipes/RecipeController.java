package com.flavourfit.Recipes;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.RecipeExceptions;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.Recipes.SavedRecipes.ISavedRecipesService;
import com.flavourfit.ResponsesDTO.GetResponse;
import com.flavourfit.ResponsesDTO.PutResponse;
import com.flavourfit.User.IUserService;
import com.flavourfit.User.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class
RecipeController {
    private static Logger logger = LoggerFactory.getLogger(RecipeController.class);
    private IRecipeService recipeService;
    private IAuthService authService;
    private final ISavedRecipesService savedRecipesService;

    @Autowired
    public RecipeController(
            IRecipeService recipeService, IAuthService authService, ISavedRecipesService savedRecipesService
    ) {
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

    @GetMapping("/saved-recipes")
    public ResponseEntity<GetResponse> getRecipesByUser(
            @RequestParam("count") int count, @RequestHeader("Authorization") String token
    ) {
        ArrayList<Object> recipes = new ArrayList<Object>();
        logger.info("Started getRecipeByUser() method");

        try {
            int userId = authService.extractUserIdFromToken(token);
            recipes = this.recipeService.getRecipesByUser(count, userId);

            logger.info("Added recipe for userId:{}", userId);
            return ResponseEntity.ok().body(new GetResponse(true,
                    "Successfully ", recipes));
        } catch (SQLException e) {
            logger.error("Unable to get the recipes");
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to get the recipes" + e.getMessage()));
        } catch (RuntimeException e) {
            logger.error("Failed to add recipe");
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to get recipes" + e.getMessage()));
        }

    }

    @PostMapping("/list")
    public ResponseEntity<GetResponse> getFilteredRecipesByUser(
            @RequestBody HashMap<String, Object> requestBody,@RequestHeader("Authorization") String token
    ) {
        ArrayList<Object> recipes = new ArrayList<Object>();
        int userId = authService.extractUserIdFromToken(token);
        logger.info("Started getFilteredRecipeByUser() method");
        try {
            recipes = this.recipeService.getFilteredRecipesByUser(userId, requestBody);

            logger.info("Fetched the records for the user: ", userId);
            return ResponseEntity.ok().body(new GetResponse(true,
                    "Successfully ", recipes));
        } catch (SQLException e) {
            logger.error("Unable to get filtered recipes the recipes");
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to get the recipes" + e.getMessage()));
        } catch (RuntimeException e) {
            logger.error("Failed to get the recipe");
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to get recipes" + e.getMessage()));
        }

    }

    @PostMapping("/record")
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
            @RequestParam("recipeId") int recipeId, @RequestHeader("Authorization") String token
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

    @PostMapping("/convert-recipe")
    public ResponseEntity<PutResponse> covertRecipe(
            @RequestParam("recipeID") int recipeId, @RequestParam("scale") double scale,
            @RequestParam("system") String system
    ) {
        logger.info("Entered controller method covertRecipe()");
        try {
            CompleteRecipeDto convertedRecipe = this.recipeService.convertRecipe(recipeId, scale, system);
            logger.info("Converted recipe for recipeId:{}", recipeId);
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully converted recipe", convertedRecipe));
        } catch (RecipeExceptions e) {
            logger.error("Failed to convert recipe for recipeId:{}", recipeId);
            return ResponseEntity.internalServerError().body(new PutResponse(true, "Failed to convert recipe"));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<PutResponse> updateExistingRecipe(
            @RequestBody CompleteRecipeDto recipe
    ) {
        logger.info("Entered controller method updateExistingRecipe()");

        try {
            CompleteRecipeDto updatedRecipe = this.recipeService.updateRecipe(recipe);
            logger.info("Updated recipe successfully");
            return ResponseEntity.ok().body(new PutResponse(true, "Updated recipe successfully", updatedRecipe));
        } catch (Exception e) {
            logger.error("Failed to update recipe: ", e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to update recipe: " + e.getMessage()));
        }
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<GetResponse> fetchRecipeById(@RequestParam("recipeId") int recipeId) {
        logger.info("Entered controller method fetchRecipeById()");

        try {
            CompleteRecipeDto recipe = this.recipeService.fetchRecipeByRecipeId(recipeId);
            logger.info("Fetch recipe with id {} successfully", recipeId);
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully fetched recipe", recipe));
        } catch (Exception e) {
            logger.error("Failed to fetch recipe: ", e.getMessage());
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to fetch recipe: " + e.getMessage()));
        }
    }
}
