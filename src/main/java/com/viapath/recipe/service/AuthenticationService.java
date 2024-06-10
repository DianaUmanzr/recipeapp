package com.viapath.recipe.service;

import com.viapath.recipe.model.AuthenticationResponse;
import com.viapath.recipe.model.Token;
import com.viapath.recipe.model.User;
import com.viapath.recipe.repository.TokenRepository;
import com.viapath.recipe.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public record AuthenticationService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        TokenRepository tokenRepository,
        AuthenticationManager authenticationManager) {

    public AuthenticationResponse register(User request) {
        return userRepository.findByUsername(request.getUsername())
                .map(user -> new AuthenticationResponse(null, null, "User already exists"))
                .orElseGet(() -> createNewUser(request));
    }

    private AuthenticationResponse createNewUser(User request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken, "User registration was successful");
    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("No user found with username: " + request.getUsername()));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokensByUser(user);
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken, "User login was successful");
    }

    private void revokeAllTokensByUser(User user) {
        List<Token> validTokens = tokenRepository.findActiveTokensByUserId(user.getId());
        validTokens.forEach(token -> token.setLoggedOut(true));
        tokenRepository.saveAll(validTokens);
    }

    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String refreshToken = authHeader.substring(7);
        return userRepository.findByUsername(jwtService.extractUsername(refreshToken))
                .filter(user -> jwtService.isValidRefreshToken(refreshToken, user))
                .map(user -> {
                    String accessToken = jwtService.generateAccessToken(user);
                    String newRefreshToken = jwtService.generateRefreshToken(user);
                    revokeAllTokensByUser(user);
                    saveUserToken(accessToken, newRefreshToken, user);
                    return ResponseEntity.ok(new AuthenticationResponse(accessToken, newRefreshToken, "New token generated"));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
