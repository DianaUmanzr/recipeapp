package com.viapath.recipe.controller;

import com.viapath.recipe.model.AuthenticationResponse;
import com.viapath.recipe.model.User;
import com.viapath.recipe.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles authentication requests for registering, logging in, and refreshing tokens.
 */
@RestController
public class AuthenticationController {

    private final AuthenticationService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new user in the system.
     *
     * @param request User data for registration
     * @return ResponseEntity with authentication details
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody User request) {
        logger.info("Registering user");
        AuthenticationResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticates a user and provides a token.
     *
     * @param request User login data
     * @return ResponseEntity with authentication details
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody User request) {
        logger.info("User login attempt");
        AuthenticationResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Refreshes the authentication token.
     *
     * @param request HttpServletRequest
     * @return ResponseEntity with the new token
     */
    @PostMapping("/refresh_token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        logger.info("Refreshing authentication token");
        return authService.refreshToken(request);
    }
}
