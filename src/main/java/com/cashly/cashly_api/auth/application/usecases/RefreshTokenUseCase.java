package com.cashly.cashly_api.auth.application.usecases;

import com.cashly.cashly_api.auth.application.dto.AuthenticationResponse;
import com.cashly.cashly_api.auth.application.dto.RefreshTokenRequest;
import com.cashly.cashly_api.auth.application.dto.UserResponse;
import com.cashly.cashly_api.auth.application.ports.RefreshTokenRepository;
import com.cashly.cashly_api.auth.application.ports.TokenService;
import com.cashly.cashly_api.auth.application.ports.UserRepository;
import com.cashly.cashly_api.auth.domain.entities.RefreshToken;
import com.cashly.cashly_api.auth.domain.entities.User;
import com.cashly.cashly_api.shared.exceptions.InvalidTokenException;
import com.cashly.cashly_api.shared.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public RefreshTokenUseCase(
        RefreshTokenRepository refreshTokenRepository,
        UserRepository userRepository,
        TokenService tokenService
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public AuthenticationResponse execute(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
            .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        if (!refreshToken.isValid()) {
            throw new InvalidTokenException("Refresh token is expired or revoked");
        }

        User user = userRepository.findById(refreshToken.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.canAuthenticate()) {
            throw new InvalidTokenException("User account is not active");
        }

        refreshToken.revoke();
        refreshTokenRepository.save(refreshToken);

        String accessToken = tokenService.generateAccessToken(user);
        RefreshToken newRefreshToken = tokenService.generateRefreshToken(user);
        refreshTokenRepository.save(newRefreshToken);

        UserResponse userResponse = UserResponse.from(user);
        return AuthenticationResponse.of(
            accessToken,
            newRefreshToken.getToken(),
            tokenService.getAccessTokenExpiration(),
            userResponse
        );
    }
}
