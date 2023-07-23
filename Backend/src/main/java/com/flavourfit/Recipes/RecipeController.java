package com.flavourfit.Recipes;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.UserNotFoundException;
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
public class RecipeController {
    private static Logger logger = LoggerFactory.getLogger(RecipeController.class);
    private IRecipeService recipeService;

    private IAuthService authService;

    @Autowired
    public RecipeController(IRecipeService recipeService, IAuthService authService) {
        this.recipeService = recipeService;
        this.authService = authService;
    }

    @GetMapping("/types")
    public ResponseEntity<PutResponse> fetchAllRecipeTypes() {
        logger.info("Started fetchAllRecipeTypes() method");
        try {
            List<String> recipeDtoList = recipeService.fetchAllRecipeTypes();
            logger.info("Successfully collected  recipe types.");
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully retrieved recipe types", recipeDtoList));
        } catch (UserNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(new PutResponse(false, e.getMessage()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
//            int userId = 7;
            recipes = this.recipeService.getRecipesByUser(count,userId);
//            CompleteRecipeDto addedRecipe = this.recipeService.recordRecipe(recipe, userId);

            logger.info("Added recipe for userId:{}", userId);
            return ResponseEntity.ok().body(new GetResponse(true,
                    "Successfully ", recipes));
        }catch (SQLException e){
            logger.error("Unable to get the recipes");
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to get the recipes" + e.getMessage()));
        }
        catch (RuntimeException e) {
            logger.error("Failed to add recipe");
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to get recipes" + e.getMessage()));
        }

    }

//    @PostMapping("/list")
//    public ResponseEntity<GetResponse> getFilteredRecipesByUser(
//            @RequestBody HashMap<String , Object> requestBody
//            ) {
//        ArrayList<Object> recipes = new ArrayList<Object>();
//        logger.info("Started getFilteredRecipeByUser() method");
//
//        try {
////            int userId = authService.extractUserIdFromToken(token);
//            int userId = 7;
//            recipes = this.recipeService.getFilteredRecipesByUser(userId, requestBody);
//
//            logger.info("Got the filtered recipe", userId);
//            return ResponseEntity.ok().body(new GetResponse(true,
//                    "Successfully ", recipes));
//        }
//        catch (SQLException e){
//            logger.error("Unable to get the filtered recipes");
//            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to get the recipes" + e.getMessage()));
//        }
//        catch (RuntimeException e) {
//            logger.error("Failed to get the filtered recipe");
//            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to get recipes" + e.getMessage()));
//        }
//
//    }

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
}
