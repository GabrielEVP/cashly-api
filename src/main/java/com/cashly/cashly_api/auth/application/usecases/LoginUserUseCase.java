package com.cashly.cashly_api.auth.application.usecases;

import com.cashly.cashly_api.auth.application.dto.AuthenticationResponse;
import com.cashly.cashly_api.auth.application.dto.LoginRequest;
import com.cashly.cashly_api.auth.application.dto.UserResponse;
import com.cashly.cashly_api.auth.application.ports.PasswordEncoder;
import com.cashly.cashly_api.auth.application.ports.RefreshTokenRepository;
import com.cashly.cashly_api.auth.application.ports.TokenService;
import com.cashly.cashly_api.auth.application.ports.UserRepository;
import com.cashly.cashly_api.auth.domain.entities.RefreshToken;
import com.cashly.cashly_api.auth.domain.entities.User;
import com.cashly.cashly_api.auth.domain.services.AuthenticationDomainService;
import com.cashly.cashly_api.auth.domain.valueobjects.Email;
import com.cashly.cashly_api.shared.exceptions.InvalidCredentialsException;
import com.cashly.cashly_api.shared.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationDomainService authenticationDomainService;

    public LoginUserUseCase(
        UserRepository userRepository,
        RefreshTokenRepository refreshTokenRepository,
        TokenService tokenService,
        PasswordEncoder passwordEncoder,
        AuthenticationDomainService authenticationDomainService
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationDomainService = authenticationDomainService;
    }

    @Transactional
    public AuthenticationResponse execute(LoginRequest request) {
        Email email = new Email(request.email());
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.email()));

        if (!authenticationDomainService.canUserAuthenticate(user)) {
            throw new InvalidCredentialsException("User account is not active");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword().getHashedValue())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String accessToken = tokenService.generateAccessToken(user);

        RefreshToken refreshToken = tokenService.generateRefreshToken(user);
        refreshTokenRepository.save(refreshToken);

        UserResponse userResponse = UserResponse.from(user);
        return AuthenticationResponse.of(
            accessToken,
            refreshToken.getToken(),
            tokenService.getAccessTokenExpiration(),
            userResponse
        );
    }
}
