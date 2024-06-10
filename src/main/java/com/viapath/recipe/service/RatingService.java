package com.viapath.recipe.service;

import com.viapath.recipe.dto.RecipeDTO;
import com.viapath.recipe.model.Rating;
import com.viapath.recipe.model.Recipe;
import com.viapath.recipe.repository.RatingRepository;
import com.viapath.recipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public record RatingService(RatingRepository ratingRepository, RecipeRepository recipeRepository) {

    @Autowired
    public RatingService {
    }

    public RecipeDTO getRecipeSummary(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        return convertToDto(recipe);
    }

    public Recipe addRating(Long id, int rating) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe not found"));
        Rating rating1 = Rating.builder().stars(rating).build();
        recipe.setRating(rating1);
        return recipeRepository.save(recipe);
    }

    private RecipeDTO convertToDto(Recipe recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(recipe.getId());
        dto.setTitle(recipe.getTitle());
        dto.setImage(recipe.getImage());
        dto.setReadyInMinutes(recipe.getReadyInMinutes());
        dto.setServings(recipe.getServings());
        dto.setSourceUrl(recipe.getSourceUrl());
        return dto;
    }
}
