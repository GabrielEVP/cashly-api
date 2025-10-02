package com.cashly.cashly_api.auth.infrastructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cashly.cashly_api.auth.application.dto.AuthenticationResponse;
import com.cashly.cashly_api.auth.application.dto.LoginRequest;
import com.cashly.cashly_api.auth.application.dto.LogoutRequest;
import com.cashly.cashly_api.auth.application.dto.RefreshTokenRequest;
import com.cashly.cashly_api.auth.application.dto.RegisterUserRequest;
import com.cashly.cashly_api.auth.application.dto.UserResponse;
import com.cashly.cashly_api.auth.application.usecases.LoginUserUseCase;
import com.cashly.cashly_api.auth.application.usecases.LogoutUserUseCase;
import com.cashly.cashly_api.auth.application.usecases.RefreshTokenUseCase;
import com.cashly.cashly_api.auth.application.usecases.RegisterUserUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUserUseCase logoutUserUseCase;

    public AuthController(
        RegisterUserUseCase registerUserUseCase,
        LoginUserUseCase loginUserUseCase,
        RefreshTokenUseCase refreshTokenUseCase,
        LogoutUserUseCase logoutUserUseCase
    ) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.logoutUserUseCase = logoutUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse response = registerUserUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthenticationResponse response = loginUserUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        AuthenticationResponse response = refreshTokenUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        logoutUserUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }
}
