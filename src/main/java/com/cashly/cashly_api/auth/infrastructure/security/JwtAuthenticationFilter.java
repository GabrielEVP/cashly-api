package com.cashly.cashly_api.auth.infrastructure.security;

import com.cashly.cashly_api.auth.application.ports.TokenService;
import com.cashly.cashly_api.auth.application.ports.UserRepository;
import com.cashly.cashly_api.auth.domain.entities.User;
import com.cashly.cashly_api.auth.domain.valueobjects.UserId;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String jwt = extractJwtFromRequest(request);

            if (jwt != null) {
                logger.info("JWT token found for request: " + request.getRequestURI());

                if (tokenService.validateToken(jwt)) {
                    logger.info("JWT token is valid");
                    UserId userId = tokenService.extractUserId(jwt);
                    logger.info("Extracted userId: " + userId);

                    User user = userRepository.findById(userId).orElse(null);

                    if (user != null) {
                        logger.info("User found: " + user.getEmail().getValue());

                        if (user.canAuthenticate()) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                user.getId().getValue().toString(),
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            );

                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            logger.info("Authentication set successfully for user: " + user.getEmail().getValue());
                        } else {
                            logger.error("User cannot authenticate (inactive or deleted): " + user.getEmail().getValue());
                        }
                    } else {
                        logger.error("User not found in database for userId: " + userId);
                    }
                } else {
                    logger.error("JWT token validation failed");
                }
            } else {
                logger.info("No JWT token in request: " + request.getRequestURI());
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: " + e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
