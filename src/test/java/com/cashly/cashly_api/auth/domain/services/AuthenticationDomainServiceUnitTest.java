package com.cashly.cashly_api.auth.domain.services;

import com.cashly.cashly_api.auth.domain.entities.User;
import com.cashly.cashly_api.auth.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationDomainServiceUnitTest {

    private AuthenticationDomainService authenticationDomainService;
    private User activeUser;
    private User inactiveUser;
    private User unverifiedUser;

    @BeforeEach
    void setUp() {
        authenticationDomainService = new AuthenticationDomainService();

        UserId userId1 = UserId.generate();
        Email email1 = new Email("active@example.com");
        Password password = Password.fromHash("$2a$12$hashedPassword");
        UserProfile profile = new UserProfile("John", "Doe");

        activeUser = new User(userId1, email1, password, profile);
        activeUser.verifyEmail();

        UserId userId2 = UserId.generate();
        Email email2 = new Email("inactive@example.com");
        inactiveUser = new User(userId2, email2, password, profile);
        inactiveUser.deactivate();

        UserId userId3 = UserId.generate();
        Email email3 = new Email("unverified@example.com");
        unverifiedUser = new User(userId3, email3, password, profile);
    }

    @Test
    void should_ReturnTrue_When_ActiveUserCanAuthenticate() {
        boolean canAuthenticate = authenticationDomainService.canUserAuthenticate(activeUser);

        assertTrue(canAuthenticate);
    }

    @Test
    void should_ReturnFalse_When_InactiveUserCannotAuthenticate() {
        boolean canAuthenticate = authenticationDomainService.canUserAuthenticate(inactiveUser);

        assertFalse(canAuthenticate);
    }

    @Test
    void should_ThrowException_When_NullUserProvidedForCanAuthenticate() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> authenticationDomainService.canUserAuthenticate(null)
        );
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void should_ReturnTrue_When_ActiveVerifiedUserCanPerformSensitiveOperations() {
        boolean canPerform = authenticationDomainService.canPerformSensitiveOperations(activeUser);

        assertTrue(canPerform);
    }

    @Test
    void should_ReturnFalse_When_InactiveUserCannotPerformSensitiveOperations() {
        boolean canPerform = authenticationDomainService.canPerformSensitiveOperations(inactiveUser);

        assertFalse(canPerform);
    }

    @Test
    void should_ReturnFalse_When_UnverifiedUserCannotPerformSensitiveOperations() {
        boolean canPerform = authenticationDomainService.canPerformSensitiveOperations(unverifiedUser);

        assertFalse(canPerform);
    }

    @Test
    void should_ThrowException_When_NullUserProvidedForSensitiveOperations() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> authenticationDomainService.canPerformSensitiveOperations(null)
        );
        assertEquals("User cannot be null", exception.getMessage());
    }
}
