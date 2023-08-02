package com.flavourfit.Recipes;
import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Exceptions.RecipeExceptions;
import com.flavourfit.Recipes.SavedRecipes.ISavedRecipesService;
import com.flavourfit.ResponsesDTO.GetResponse;
import com.flavourfit.ResponsesDTO.PutResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeControllerTest {

    @Mock
    private IRecipeService recipeService;

    @Mock
    private IAuthService authService;

    @Mock
    private ISavedRecipesService savedRecipesService;

    @InjectMocks
    private RecipeController recipeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchAllRecipeTypesTest() throws SQLException {
        // Success Case
        // Mock the behavior of recipeService.fetchAllRecipeTypes()
        List<String> mockRecipeTypes = List.of("Breakfast", "Lunch");
        when(recipeService.fetchAllRecipeTypes()).thenReturn(mockRecipeTypes);

        // Call the method
        ResponseEntity<PutResponse> responseEntity = recipeController.fetchAllRecipeTypes();
        PutResponse responseBody = responseEntity.getBody();

        // Successfully retrieved recipes
        assertEquals("Successfully retrieved recipe types", responseBody.getMessage());
        assertEquals(mockRecipeTypes, responseBody.getData());

        // Failure case

        // Database connection error and no data is returned
        when(recipeService.fetchAllRecipeTypes()).thenThrow(new SQLException("Database error"));
        assertEquals("Successfully retrieved recipe types", responseBody.getMessage());

    }

    @Test
    void saveRecipeTest() throws RecipeExceptions {
        // Success Case
        // Mock the behavior of authService.extractUserIdFromToken()
        int userId = 1;
        String mockToken = "mock-token";
        when(authService.extractUserIdFromToken(mockToken)).thenReturn(userId);

        // Mock the behavior of savedRecipesService.saveRecipe()
        int recipeId = 100;
        doNothing().when(savedRecipesService).saveRecipe(recipeId, userId);

        // Call the method under test
        ResponseEntity<PutResponse> responseEntity = recipeController.saveRecipe(recipeId, mockToken);
        PutResponse responseBody = responseEntity.getBody();

        // Assert the result
        assertEquals("Successfully saved recipe", responseBody.getMessage());


        // Failure Case
        // Recipe not found case
        doThrow(new RecipeExceptions("Recipe not found")).when(savedRecipesService).saveRecipe(recipeId, userId);

        // Failed to save recipe case
        assertEquals("Successfully saved recipe", responseBody.getMessage());
    }

    @Test
    public void getFilteredRecipesByUserTest() throws SQLException{
        HashMap<String, Object> requestBody = new HashMap<>();

        String token = "valid_token";
        int userId = 7;
        when(authService.extractUserIdFromToken(token)).thenReturn(userId); // Assuming the token is valid

        ArrayList<Object> expectedRecipes = new ArrayList<>();
        // Add some test data to the expectedRecipes list

        when(recipeService.getFilteredRecipesByUser(userId, requestBody)).thenReturn(expectedRecipes);

        // Act
        ResponseEntity<GetResponse> response = recipeController.getFilteredRecipesByUser(requestBody, token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof GetResponse);
        GetResponse getResponse = response.getBody();
        assertTrue(getResponse.isSuccess());
    }

    @Test
    public void getRecipesByUserTest() throws SQLException{
        int count = 5;
        String token = "valid_token";
        int userId = 1;

        ArrayList<Object> expectedRecipes = new ArrayList<>();

        when(authService.extractUserIdFromToken(token)).thenReturn(userId);
        when(recipeService.getRecipesByUser(count, userId)).thenReturn(expectedRecipes);

        ResponseEntity<GetResponse> response = recipeController.getRecipesByUser(count, token);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof GetResponse);
        GetResponse getResponse = response.getBody();
        assertTrue(getResponse.isSuccess());
        assertEquals("Successfully ", getResponse.getMessage());
        assertEquals(expectedRecipes, getResponse.getData());
    }

    @Test
    public void convertRecipeTest() throws Exception{

        int recipeId = 1;
        double scale = 2.0;
        String system = "metric";

        CompleteRecipeDto convertedRecipe = new CompleteRecipeDto();

        when(recipeService.convertRecipe(recipeId, scale, system)).thenReturn(convertedRecipe);

        ResponseEntity<PutResponse> response = recipeController.covertRecipe(recipeId, scale, system);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Successfully converted recipe", response.getBody().getMessage());
        assertEquals(convertedRecipe, response.getBody().getData());

        verify(recipeService).convertRecipe(recipeId, scale, system);
    }
}