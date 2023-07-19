package com.flavourfit.Recipes;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.ResponsesDTO.PutResponse;
import com.flavourfit.User.IUserService;
import com.flavourfit.User.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private static Logger logger = LoggerFactory.getLogger(RecipeController.class);
    private IRecipeService recipeService;

    @Autowired
    public RecipeController(IRecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/types")
    public ResponseEntity<PutResponse> fetchAllRecipeTypes(){
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
}
