package com.viapath.recipe.controller;

import com.viapath.recipe.dto.RecipeDTO;
import com.viapath.recipe.model.Recipe;
import com.viapath.recipe.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling data and ratings related to recipes.
 * Provides endpoints for retrieving recipe summaries and adding ratings to recipes.
 */
@RestController
@RequestMapping("/api/data")  // Base URI for all requests handled by this controller.
public class ServerDataController {

    private final RatingService ratingService;

    /**
     * Constructs a ServerDataController with a specified RatingService.
     *
     * @param ratingService the service used to handle operations related to recipe ratings
     */
    @Autowired
    public ServerDataController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * Retrieves a summary of a recipe based on the given recipe ID.
     *
     * @param searchTerm the ID of the recipe to search for
     * @return a RecipeDTO containing summarized information about the recipe
     */
    @GetMapping("/search/{searchTerm}")
    public RecipeDTO searchRecipes(@PathVariable Long searchTerm) {
        // Calls RatingService to get a summary of the recipe identified by searchTerm.
        return ratingService.getRecipeSummary(searchTerm);
    }

    /**
     * Adds a new rating to a recipe identified by the given ID.
     *
     * @param id the ID of the recipe to which the rating will be added
     * @param rating the rating value to be added to the recipe
     * @return the updated Recipe object with the new rating
     */
    @PostMapping("/{id}/rating")
    public Recipe addRating(@PathVariable Long id, @RequestParam int rating) {
        // Calls RatingService to add a new rating to the specified recipe.
        return ratingService.addRating(id, rating);
    }
}
