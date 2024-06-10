package com.viapath.recipe.service;

import com.viapath.recipe.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service to manage user details loading by username for authentication purposes.
 */
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs a UserDetailsServiceImpl with the necessary user repository.
     *
     * @param userRepository the repository to use for user data access
     */
    public UserDetailsServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user-specific data necessary for authentication.
     *
     * @param username the username identifying the user whose data is required
     * @return a UserDetails object containing the user's data
     * @throws UsernameNotFoundException if the user cannot be found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
