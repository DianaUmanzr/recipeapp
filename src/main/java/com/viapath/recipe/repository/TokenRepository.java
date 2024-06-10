package com.viapath.recipe.repository;

import com.viapath.recipe.model.Token;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for {@link Token} objects. Provides handling for retrieval of token data.
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    /**
     * Finds all active access tokens for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of {@link Token} objects that are active (not logged out)
     */
    @Query("SELECT t FROM Token t WHERE t.user.id = :userId AND t.loggedOut = false")
    List<Token> findActiveTokensByUserId(Integer userId);

    /**
     * Finds a token based on the access token string.
     *
     * @param accessToken the token string to search for
     * @return an {@link Optional} containing the token if found, or empty otherwise
     */
    Optional<Token> findByAccessToken(String accessToken);

    /**
     * Finds a token based on the refresh token string.
     *
     * @param refreshToken the token string to search for
     * @return an {@link Optional} containing the token if found, or empty otherwise
     */
    Optional<Token> findByRefreshToken(String refreshToken);
}