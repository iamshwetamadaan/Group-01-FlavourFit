package com.flavourfit.Recipes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecipeServiceImplTest {

    @Mock
    private IRecipeDao recipeDao;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFetchAllRecipeTypes_Success() throws SQLException {
        // Mocking the behavior of recipeDao.getAllRecipesTypes()
        List<String> mockRecipeTypes = List.of("Vegetarian", "Non-Vegetarian", "Vegan");
        when(recipeDao.getAllRecipesTypes()).thenReturn(mockRecipeTypes);

        List<String> result = recipeService.fetchAllRecipeTypes();

        // Asserting the result
        assertEquals(mockRecipeTypes, result);
    }
}
