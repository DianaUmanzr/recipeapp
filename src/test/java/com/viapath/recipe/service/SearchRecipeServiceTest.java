package com.viapath.recipe.service;

import com.viapath.recipe.dto.RecipeResponseDTO;
import com.viapath.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SearchRecipeServiceTest {

    @MockBean
    private RecipeRepository recipeRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Autowired
    @InjectMocks
    private SearchRecipeService searchRecipeService;

    @BeforeEach
    void setUp() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void searchRecipes_returnsRecipeResponseDTO() {
        String query = "chocolate";
        RecipeResponseDTO mockResponse = new RecipeResponseDTO();
        mockResponse.setTotalResults(1);

        when(responseSpec.bodyToMono(RecipeResponseDTO.class)).thenReturn(Mono.just(mockResponse));

        RecipeResponseDTO responseMono = searchRecipeService.searchRecipes(query);

        assertNotNull(responseMono.getResults());
    }
}
