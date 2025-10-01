package com.cashly.cashly_api.auth.application.usecases;

import com.cashly.cashly_api.auth.application.dto.UserResponse;
import com.cashly.cashly_api.auth.application.ports.UserRepository;
import com.cashly.cashly_api.auth.domain.entities.User;
import com.cashly.cashly_api.auth.domain.valueobjects.*;
import com.cashly.cashly_api.shared.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetUserByIdUseCase Unit Tests")
class GetUserByIdUseCaseUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserByIdUseCase getUserByIdUseCase;

    private User testUser;
    private UserId testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UserId.generate();
        Email email = new Email("test@example.com");
        Password password = Password.fromHash("$2a$10$hashedPassword");
        UserProfile profile = new UserProfile("John", "Doe");

        testUser = new User(testUserId, email, password, profile);
    }

    @Test
    @DisplayName("should_ReturnUser_When_UserExists")
    void should_ReturnUser_When_UserExists() {
        String userIdStr = testUserId.getValue().toString();
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        UserResponse response = getUserByIdUseCase.execute(userIdStr);

        assertNotNull(response);
        assertEquals("test@example.com", response.email());
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertTrue(response.active());
        verify(userRepository, times(1)).findById(any(UserId.class));
    }

    @Test
    @DisplayName("should_ThrowException_When_UserNotFound")
    void should_ThrowException_When_UserNotFound() {
        String userIdStr = testUserId.getValue().toString();
        when(userRepository.findById(any(UserId.class))).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> getUserByIdUseCase.execute(userIdStr)
        );

        assertTrue(exception.getMessage().contains("User not found with ID"));
        verify(userRepository, times(1)).findById(any(UserId.class));
    }

    @Test
    @DisplayName("should_ThrowException_When_UserIdIsNull")
    void should_ThrowException_When_UserIdIsNull() {
        assertThrows(
            Exception.class,
            () -> getUserByIdUseCase.execute(null)
        );

        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("should_ReturnUserData_When_UserFound")
    void should_ReturnUserData_When_UserFound() {
        String userIdStr = testUserId.getValue().toString();
        when(userRepository.findById(any(UserId.class))).thenReturn(Optional.of(testUser));

        UserResponse response = getUserByIdUseCase.execute(userIdStr);

        assertEquals(testUserId.getValue().toString(), response.id());
        assertEquals(testUser.getEmail().getValue(), response.email());
        assertEquals(testUser.getProfile().getFirstName(), response.firstName());
        assertEquals(testUser.getProfile().getLastName(), response.lastName());
        assertEquals(testUser.isActive(), response.active());
        assertEquals(testUser.isEmailVerified(), response.emailVerified());
    }

    @Test
    @DisplayName("should_HandleInactiveUser_When_UserIsInactive")
    void should_HandleInactiveUser_When_UserIsInactive() {
        String userIdStr = testUserId.getValue().toString();
        testUser.deactivate();
        when(userRepository.findById(any(UserId.class))).thenReturn(Optional.of(testUser));

        UserResponse response = getUserByIdUseCase.execute(userIdStr);

        assertFalse(response.active());
        verify(userRepository, times(1)).findById(any(UserId.class));
    }
}
