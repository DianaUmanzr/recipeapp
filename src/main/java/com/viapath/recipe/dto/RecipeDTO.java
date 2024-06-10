package com.viapath.recipe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDTO {

    private int readyInMinutes;
    private String sourceUrl;
    private String image;
    private int servings;
    private long id;
    private String title;
}
