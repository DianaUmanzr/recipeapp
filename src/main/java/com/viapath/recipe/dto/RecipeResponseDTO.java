package com.viapath.recipe.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeResponseDTO {

    private List<RecipeDTO> results;
    private String baseUri;
    private int offset;
    private int number;
    private int totalResults;
    private long processingTimeMs;
    private long expires;
    private boolean isStale;
}
