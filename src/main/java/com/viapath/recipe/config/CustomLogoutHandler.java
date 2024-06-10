package com.viapath.recipe.config;

import com.viapath.recipe.model.Token;
import com.viapath.recipe.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles the custom logout logic to invalidate authentication tokens stored in the database.
 */
/**
 * Handles the custom logout logic to invalidate authentication tokens stored in the database.
 */
@Configuration
public class CustomLogoutHandler implements LogoutHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomLogoutHandler.class);
    private final TokenRepository tokenRepository;

    public CustomLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * Custom logout logic that invalidates the user's authentication token.
     *
     * @param request        The HTTP request.
     * @param response       The HTTP response.
     * @param authentication The current authentication object.
     */
    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.error("Invalid Authorization header or token missing.");
            return;
        }

        String token = authHeader.substring(7);
        tokenRepository.findByAccessToken(token).ifPresent(this::invalidateToken);
    }

    /**
     * Marks the provided token as logged out in the database.
     *
     * @param token The token to invalidate.
     */
    private void invalidateToken(Token token) {
        token.setLoggedOut(true);
        tokenRepository.save(token);
        logger.info("Token invalidated successfully for user: {}", token.getUser().getUsername());
    }
}
