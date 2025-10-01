package com.cashly.cashly_api.auth.infrastructure.persistence;

import com.cashly.cashly_api.auth.domain.entities.User;
import com.cashly.cashly_api.auth.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserEntity Unit Tests")
class UserEntityUnitTest {

    private User testUser;

    @BeforeEach
    void setUp() {
        UserId userId = UserId.generate();
        Email email = new Email("test@example.com");
        Password password = Password.fromHash("$2a$10$hashedPassword");
        UserProfile profile = new UserProfile("John", "Doe");

        testUser = new User(userId, email, password, profile);
    }

    @Test
    @DisplayName("should_ConvertFromDomain_When_ValidUserProvided")
    void should_ConvertFromDomain_When_ValidUserProvided() {
        UserEntity entity = UserEntity.fromDomain(testUser);

        assertNotNull(entity);
        assertEquals(testUser.getId().getValue().toString(), entity.getId());
        assertEquals(testUser.getEmail().getValue(), entity.getEmail());
        assertEquals(testUser.getPassword().getHashedValue(), entity.getPasswordHash());
        assertEquals(testUser.getProfile().getFirstName(), entity.getFirstName());
        assertEquals(testUser.getProfile().getLastName(), entity.getLastName());
        assertEquals(testUser.isActive(), entity.isActive());
        assertEquals(testUser.isEmailVerified(), entity.isEmailVerified());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    @DisplayName("should_ThrowException_When_ConvertingNullUser")
    void should_ThrowException_When_ConvertingNullUser() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> UserEntity.fromDomain(null)
        );

        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("should_ConvertToDomain_When_ValidEntityProvided")
    void should_ConvertToDomain_When_ValidEntityProvided() {
        UserEntity entity = UserEntity.fromDomain(testUser);

        User domainUser = entity.toDomain();

        assertNotNull(domainUser);
        assertEquals(testUser.getId(), domainUser.getId());
        assertEquals(testUser.getEmail(), domainUser.getEmail());
        assertEquals(testUser.getPassword().getHashedValue(), domainUser.getPassword().getHashedValue());
        assertEquals(testUser.getProfile().getFirstName(), domainUser.getProfile().getFirstName());
        assertEquals(testUser.getProfile().getLastName(), domainUser.getProfile().getLastName());
        assertEquals(testUser.isActive(), domainUser.isActive());
        assertEquals(testUser.isEmailVerified(), domainUser.isEmailVerified());
    }

    @Test
    @DisplayName("should_UpdateFromDomain_When_ValidUserProvided")
    void should_UpdateFromDomain_When_ValidUserProvided() {
        UserEntity entity = UserEntity.fromDomain(testUser);

        UserProfile newProfile = new UserProfile("Jane", "Smith");
        testUser.updateProfile(newProfile);
        testUser.verifyEmail();

        entity.updateFromDomain(testUser);

        assertEquals("Jane", entity.getFirstName());
        assertEquals("Smith", entity.getLastName());
        assertTrue(entity.isEmailVerified());
    }

    @Test
    @DisplayName("should_ThrowException_When_UpdatingWithNullUser")
    void should_ThrowException_When_UpdatingWithNullUser() {
        UserEntity entity = UserEntity.fromDomain(testUser);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> entity.updateFromDomain(null)
        );

        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("should_PreserveImmutableFields_When_UpdateFromDomain")
    void should_PreserveImmutableFields_When_UpdateFromDomain() {
        UserEntity entity = UserEntity.fromDomain(testUser);
        String originalId = entity.getId();
        String originalEmail = entity.getEmail();

        UserId newUserId = UserId.generate();
        Email newEmail = new Email("different@example.com");
        Password newPassword = Password.fromHash("$2a$10$differentHash");
        UserProfile newProfile = new UserProfile("Different", "Person");
        User differentUser = new User(newUserId, newEmail, newPassword, newProfile);

        entity.updateFromDomain(differentUser);

        assertEquals(originalId, entity.getId());
        assertEquals(originalEmail, entity.getEmail());
        assertEquals("Different", entity.getFirstName());
        assertEquals("Person", entity.getLastName());
    }

    @Test
    @DisplayName("should_HandleInactiveUser_When_Converting")
    void should_HandleInactiveUser_When_Converting() {
        testUser.deactivate();
        UserEntity entity = UserEntity.fromDomain(testUser);

        User domainUser = entity.toDomain();

        assertFalse(domainUser.isActive());
    }

    @Test
    @DisplayName("should_HandleVerifiedEmail_When_Converting")
    void should_HandleVerifiedEmail_When_Converting() {
        testUser.verifyEmail();
        UserEntity entity = UserEntity.fromDomain(testUser);

        User domainUser = entity.toDomain();

        assertTrue(domainUser.isEmailVerified());
    }

    @Test
    @DisplayName("should_RoundTripConversion_When_ConvertingBothWays")
    void should_RoundTripConversion_When_ConvertingBothWays() {
        UserEntity entity = UserEntity.fromDomain(testUser);

        User domainUser = entity.toDomain();
        UserEntity newEntity = UserEntity.fromDomain(domainUser);

        assertEquals(entity.getId(), newEntity.getId());
        assertEquals(entity.getEmail(), newEntity.getEmail());
        assertEquals(entity.getPasswordHash(), newEntity.getPasswordHash());
        assertEquals(entity.getFirstName(), newEntity.getFirstName());
        assertEquals(entity.getLastName(), newEntity.getLastName());
        assertEquals(entity.isActive(), newEntity.isActive());
        assertEquals(entity.isEmailVerified(), newEntity.isEmailVerified());
    }
}
