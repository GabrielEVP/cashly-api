package com.cashly.cashly_api.auth.application.usecases;

import com.cashly.cashly_api.auth.application.dto.RegisterUserRequest;
import com.cashly.cashly_api.auth.application.dto.UserResponse;
import com.cashly.cashly_api.auth.application.ports.PasswordEncoder;
import com.cashly.cashly_api.auth.application.ports.UserRepository;
import com.cashly.cashly_api.auth.domain.entities.User;
import com.cashly.cashly_api.auth.domain.valueobjects.*;
import com.cashly.cashly_api.shared.exceptions.DuplicateEmailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse execute(RegisterUserRequest request) {
        Email email = new Email(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already registered: " + request.email());
        }

        Password.validateRawPassword(request.password());

        String hashedPassword = passwordEncoder.encode(request.password());
        Password password = Password.fromHash(hashedPassword);

        UserProfile profile = new UserProfile(request.firstName(), request.lastName());

        User user = new User(UserId.generate(), email, password, profile);

        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }
}
