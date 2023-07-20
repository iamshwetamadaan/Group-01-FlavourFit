package com.flavourfit.Recipes;

import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.ResponsesDTO.PutResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecipeControllerTest {

    @Mock
    private IRecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFetchAllRecipeTypes_Success() throws SQLException, UserNotFoundException {
        // Mocking the behavior of recipeService.fetchAllRecipeTypes()
        List<String> mockRecipeTypes = List.of("Breakfast", "Lunch", "Dinner");
        when(recipeService.fetchAllRecipeTypes()).thenReturn(mockRecipeTypes);

        // Calling the method under test
        ResponseEntity<PutResponse> responseEntity = recipeController.fetchAllRecipeTypes();

        // Asserting the response status code
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Asserting the response body
        PutResponse responseBody = responseEntity.getBody();
        assertEquals(true, responseBody.isSuccess());
        assertEquals("Successfully retrieved recipe types", responseBody.getMessage());
        assertEquals(mockRecipeTypes, responseBody.getData());
    }

    @Test
    void testFetchAllRecipeTypes_UserNotFoundException() throws SQLException, UserNotFoundException {
        // Mocking the behavior of recipeService.fetchAllRecipeTypes() to throw UserNotFoundException
        when(recipeService.fetchAllRecipeTypes()).thenThrow(new UserNotFoundException("User not found"));

        // Calling the method under test
        ResponseEntity<PutResponse> responseEntity = recipeController.fetchAllRecipeTypes();

        // Asserting the response status code
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        // Asserting the response body
        PutResponse responseBody = responseEntity.getBody();
        assertEquals(false, responseBody.isSuccess());
        assertEquals("User not found", responseBody.getMessage());
    }

    @Test
    void testFetchAllRecipeTypes_SqlException() throws SQLException, UserNotFoundException {
        // Mocking the behavior of recipeService.fetchAllRecipeTypes() to throw SQLException
        when(recipeService.fetchAllRecipeTypes()).thenThrow(new SQLException("SQL exception"));

        // Asserting that the method throws a RuntimeException with the cause as the original SQLException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> recipeController.fetchAllRecipeTypes());
        assertEquals(SQLException.class, exception.getCause().getClass());
        assertEquals("SQL exception", exception.getCause().getMessage());
    }
}
