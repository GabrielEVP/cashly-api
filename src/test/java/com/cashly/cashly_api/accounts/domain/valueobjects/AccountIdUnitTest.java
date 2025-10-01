package com.cashly.cashly_api.accounts.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountIdUnitTest {

    @Test
    void should_CreateAccountId_When_ValidUUIDProvided() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        // Act
        AccountId accountId = new AccountId(uuid);

        // Assert
        assertNotNull(accountId);
        assertEquals(uuid, accountId.getValue());
    }

    @Test
    void should_ThrowException_When_NullUUIDProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new AccountId(null)
        );
        assertEquals("Account ID cannot be null", exception.getMessage());
    }

    @Test
    void should_GenerateUniqueAccountId_When_GenerateMethodCalled() {
        // Act
        AccountId accountId1 = AccountId.generate();
        AccountId accountId2 = AccountId.generate();

        // Assert
        assertNotNull(accountId1);
        assertNotNull(accountId2);
        assertNotEquals(accountId1, accountId2);
    }

    @Test
    void should_BeEqual_When_SameUUIDValue() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        AccountId accountId1 = new AccountId(uuid);
        AccountId accountId2 = new AccountId(uuid);

        // Act & Assert
        assertEquals(accountId1, accountId2);
        assertEquals(accountId1.hashCode(), accountId2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentUUIDValue() {
        // Arrange
        AccountId accountId1 = AccountId.generate();
        AccountId accountId2 = AccountId.generate();

        // Act & Assert
        assertNotEquals(accountId1, accountId2);
    }

    @Test
    void should_ReturnStringRepresentation_When_ToStringCalled() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        AccountId accountId = new AccountId(uuid);

        // Act
        String result = accountId.toString();

        // Assert
        assertTrue(result.contains("AccountId"));
        assertTrue(result.contains(uuid.toString()));
    }
}
