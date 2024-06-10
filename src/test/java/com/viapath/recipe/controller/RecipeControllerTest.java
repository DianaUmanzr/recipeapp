package com.viapath.recipe.controller;

import com.viapath.recipe.dto.RecipeResponseDTO;
import com.viapath.recipe.service.SearchRecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class RecipeControllerTest {

    @Mock
    private SearchRecipeService searchRecipeService;

    @InjectMocks
    private RecipeController recipeController;

    @Test
    public void testSearchRecipes() {
        String query = "pasta";
        RecipeResponseDTO dto = new RecipeResponseDTO();
        dto.setTotalResults(10);

        when(searchRecipeService.searchRecipes(query)).thenReturn(dto);

        RecipeResponseDTO response = recipeController.searchRecipes(query);

        assertNotNull(response);
        assertEquals(10, response.getTotalResults());
        verify(searchRecipeService, times(1)).searchRecipes(query);
    }
}
