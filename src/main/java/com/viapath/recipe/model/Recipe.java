package com.viapath.recipe.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "recipe")
public class Recipe {

    public Recipe() {

    }

    @Id
    @Column(name = "recipe_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "ready_in_minutes")
    private int readyInMinutes;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "image")
    private String image;

    @Column(name = "servings")
    private int servings;

    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Rating rating;

}
