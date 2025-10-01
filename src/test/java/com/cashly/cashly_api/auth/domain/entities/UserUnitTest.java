package com.cashly.cashly_api.auth.domain.entities;

import com.cashly.cashly_api.auth.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserUnitTest {

    private UserId userId;
    private Email email;
    private Password password;
    private UserProfile profile;

    @BeforeEach
    void setUp() {
        userId = UserId.generate();
        email = new Email("test@example.com");
        password = Password.fromHash("$2a$12$hashedPassword");
        profile = new UserProfile("John", "Doe");
    }

    @Test
    void should_CreateUser_When_ValidParametersProvided() {
        User user = new User(userId, email, password, profile);

        assertNotNull(user);
        assertEquals(userId, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(profile, user.getProfile());
        assertTrue(user.isActive());
        assertFalse(user.isEmailVerified());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void should_ThrowException_When_NullUserIdProvided() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new User(null, email, password, profile)
        );
        assertEquals("User ID cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullEmailProvided() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new User(userId, null, password, profile)
        );
        assertEquals("Email cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullPasswordProvided() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new User(userId, email, null, profile)
        );
        assertEquals("Password cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullProfileProvided() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new User(userId, email, password, null)
        );
        assertEquals("User profile cannot be null", exception.getMessage());
    }

    @Test
    void should_ChangePassword_When_NewPasswordProvided() {
        User user = new User(userId, email, password, profile);
        Password newPassword = Password.fromHash("$2a$12$newHashedPassword");

        user.changePassword(newPassword);

        assertEquals(newPassword, user.getPassword());
    }

    @Test
    void should_ThrowException_When_NullPasswordProvidedForChange() {
        User user = new User(userId, email, password, profile);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.changePassword(null)
        );
        assertEquals("New password cannot be null", exception.getMessage());
    }

    @Test
    void should_UpdateProfile_When_NewProfileProvided() {
        User user = new User(userId, email, password, profile);
        UserProfile newProfile = new UserProfile("Jane", "Smith");

        user.updateProfile(newProfile);

        assertEquals(newProfile, user.getProfile());
    }

    @Test
    void should_ThrowException_When_NullProfileProvidedForUpdate() {
        User user = new User(userId, email, password, profile);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.updateProfile(null)
        );
        assertEquals("User profile cannot be null", exception.getMessage());
    }

    @Test
    void should_ActivateUser_When_ActivateMethodCalled() {
        User user = new User(userId, email, password, profile);
        user.deactivate();

        user.activate();

        assertTrue(user.isActive());
    }

    @Test
    void should_DeactivateUser_When_DeactivateMethodCalled() {
        User user = new User(userId, email, password, profile);

        user.deactivate();

        assertFalse(user.isActive());
    }

    @Test
    void should_VerifyEmail_When_VerifyEmailMethodCalled() {
        User user = new User(userId, email, password, profile);

        user.verifyEmail();

        assertTrue(user.isEmailVerified());
    }

    @Test
    void should_ReturnTrue_When_UserCanAuthenticate() {
        User user = new User(userId, email, password, profile);

        boolean canAuthenticate = user.canAuthenticate();

        assertTrue(canAuthenticate);
    }

    @Test
    void should_ReturnFalse_When_UserIsDeactivated() {
        User user = new User(userId, email, password, profile);
        user.deactivate();

        boolean canAuthenticate = user.canAuthenticate();

        assertFalse(canAuthenticate);
    }

    @Test
    void should_ReturnTrue_When_UserHasGivenEmail() {
        User user = new User(userId, email, password, profile);

        boolean hasEmail = user.hasEmail(email);

        assertTrue(hasEmail);
    }

    @Test
    void should_ReturnFalse_When_UserDoesNotHaveGivenEmail() {
        User user = new User(userId, email, password, profile);
        Email differentEmail = new Email("other@example.com");

        boolean hasEmail = user.hasEmail(differentEmail);

        assertFalse(hasEmail);
    }

    @Test
    void should_ReturnFalse_When_NullEmailProvidedForHasEmail() {
        User user = new User(userId, email, password, profile);

        boolean hasEmail = user.hasEmail(null);

        assertFalse(hasEmail);
    }

    @Test
    void should_BeEqual_When_SameUserIdProvided() {
        User user1 = new User(userId, email, password, profile);
        User user2 = new User(userId, new Email("different@example.com"), password, profile);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentUserIdProvided() {
        User user1 = new User(UserId.generate(), email, password, profile);
        User user2 = new User(UserId.generate(), email, password, profile);

        assertNotEquals(user1, user2);
    }

    @Test
    void should_HidePasswordInToString_When_UserCreated() {
        User user = new User(userId, email, password, profile);

        String result = user.toString();

        assertNotNull(result);
        assertTrue(result.contains("User"));
        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("password=***"));
        assertFalse(result.contains("$2a$12$hashedPassword"));
    }
}
