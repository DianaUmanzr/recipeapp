package com.viapath.recipe.service;

import com.viapath.recipe.config.WebClientConfig;
import com.viapath.recipe.model.Recipe;
import com.viapath.recipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UpdateRecipeService{

    private final RecipeRepository recipeRepository;
    private final WebClient webClient;
    private final WebClientConfig webClientConfig;

    public UpdateRecipeService(RecipeRepository recipeRepository, WebClient webClient, WebClientConfig webClientConfig) {
        this.recipeRepository = recipeRepository;
        this.webClient = webClient;
        this.webClientConfig = webClientConfig;
    }

    @Value("${recipe.api.key}")
    private String apiKey;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateRecipes() {
        recipeRepository.findAll().forEach(recipe -> {
            updateRecipeInformation(recipe.getId());
        });
    }

    private void updateRecipeInformation(Long recipeId) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes/{recipeId}/information")
                        .queryParam("apiKey", apiKey)
                        .build(recipeId))
                .retrieve()
                .bodyToMono(Recipe.class)
                .subscribe(updatedRecipe -> {
                    saveUpdatedRecipe(updatedRecipe);
                });
    }


    private void saveUpdatedRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
        System.out.println("Updated recipe: " + recipe.getTitle());
    }
}
