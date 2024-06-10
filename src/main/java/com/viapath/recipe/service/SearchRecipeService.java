package com.viapath.recipe.service;

import com.viapath.recipe.config.WebClientConfig;
import com.viapath.recipe.dto.RecipeDTO;
import com.viapath.recipe.dto.RecipeResponseDTO;
import com.viapath.recipe.model.Recipe;
import com.viapath.recipe.repository.RecipeRepository;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class SearchRecipeService {

    private final RecipeRepository recipeRepository;
    private final WebClient webClient;
    private final WebClientConfig webClientConfig;

    @Value("${recipe.api.key}")
    private String apiKey;

    @Autowired
    public SearchRecipeService(RecipeRepository recipeRepository, WebClient webClient, WebClientConfig webClientConfig) {
        this.recipeRepository = recipeRepository;
        this.webClient = webClient;
        this.webClientConfig = webClientConfig;
    }

   public RecipeResponseDTO searchRecipes(String query) {
       System.out.println("Making API call with key: " + apiKey);

       RecipeResponseDTO response =  webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/recipes/search")
                        .queryParam("query", query)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(RecipeResponseDTO.class)
                .timeout(Duration.ofSeconds(10))
               .block();

       response.getResults().forEach(this::processAndSaveRecipe);
       return response;
    }


    private void processAndSaveRecipe(RecipeDTO data) {
        Recipe recipe = convertToRecipe(data);
        Mono.just(recipe)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(this::saveRecipe);
    }

    private Recipe convertToRecipe(RecipeDTO data) {
        return Recipe.builder()
                .id(data.getId())
                .readyInMinutes(data.getReadyInMinutes())
                .title(data.getTitle())
                .servings(data.getServings())
                .image(data.getImage())
                .sourceUrl(data.getSourceUrl()).build();
    }

    private void saveRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
        System.out.println("Recipe saved: " + recipe);
    }
}
