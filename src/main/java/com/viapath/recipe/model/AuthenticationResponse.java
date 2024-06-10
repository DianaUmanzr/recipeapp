package com.viapath.recipe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Record for representing the response of authentication operations, encapsulating access and refresh tokens */
public record AuthenticationResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("message") String message
) {}