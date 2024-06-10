package com.viapath.recipe.repository;

import com.viapath.recipe.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link User} entities to handle data access layer operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the found user if they exist, or empty otherwise
     */
    Optional<User> findByUsername(String username);
}