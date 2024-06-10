package com.viapath.recipe.controller;

import com.viapath.recipe.dto.RecipeResponseDTO;
import com.viapath.recipe.service.SearchRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling recipe-related API requests.
 * Provides endpoints for searching recipes based on query parameters.
 */
@RestController
@RequestMapping("/api/recipes") // Sets the base path for all methods in this controller.
public class RecipeController {

    private final SearchRecipeService recipeService;

    /**
     * Constructs a RecipeController with a specified SearchRecipeService.
     *
     * @param recipeService the service used to handle the business logic for recipe operations
     */
    @Autowired
    public RecipeController(SearchRecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * Searches for recipes based on a user-provided query string.
     *
     * @param query the search keyword used to find recipes
     * @return a RecipeResponseDTO containing the search results
     */
    @GetMapping("/search")  // Maps HTTP GET requests to the specified path.
    public RecipeResponseDTO searchRecipes(@RequestParam String query) {
        // Delegates the search operation to the SearchRecipeService and returns the results.
        return recipeService.searchRecipes(query);
    }
}
