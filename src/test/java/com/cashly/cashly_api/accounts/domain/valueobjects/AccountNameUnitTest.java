package com.cashly.cashly_api.accounts.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountNameUnitTest {

    @Test
    void should_CreateAccountName_When_ValidNameProvided() {
        // Arrange
        String name = "Main Checking Account";

        // Act
        AccountName accountName = new AccountName(name);

        // Assert
        assertNotNull(accountName);
        assertEquals(name, accountName.getValue());
    }

    @Test
    void should_TrimWhitespace_When_NameHasLeadingOrTrailingSpaces() {
        // Arrange
        String name = "  Savings Account  ";

        // Act
        AccountName accountName = new AccountName(name);

        // Assert
        assertEquals("Savings Account", accountName.getValue());
    }

    @Test
    void should_ThrowException_When_NullNameProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new AccountName(null)
        );
        assertEquals("Account name cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_EmptyNameProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new AccountName("")
        );
        assertEquals("Account name cannot be empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_WhitespaceOnlyNameProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new AccountName("   ")
        );
        assertEquals("Account name cannot be empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NameExceedsMaxLength() {
        // Arrange
        String longName = "A".repeat(101);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new AccountName(longName)
        );
        assertTrue(exception.getMessage().contains("cannot exceed 100 characters"));
    }

    @Test
    void should_CreateAccountName_When_NameIsExactlyMaxLength() {
        // Arrange
        String name = "A".repeat(100);

        // Act
        AccountName accountName = new AccountName(name);

        // Assert
        assertNotNull(accountName);
        assertEquals(100, accountName.getValue().length());
    }

    @Test
    void should_BeEqual_When_SameNameValue() {
        // Arrange
        AccountName name1 = new AccountName("My Account");
        AccountName name2 = new AccountName("My Account");

        // Act & Assert
        assertEquals(name1, name2);
        assertEquals(name1.hashCode(), name2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentNameValue() {
        // Arrange
        AccountName name1 = new AccountName("Account 1");
        AccountName name2 = new AccountName("Account 2");

        // Act & Assert
        assertNotEquals(name1, name2);
    }

    @Test
    void should_ReturnStringRepresentation_When_ToStringCalled() {
        // Arrange
        AccountName accountName = new AccountName("Test Account");

        // Act
        String result = accountName.toString();

        // Assert
        assertTrue(result.contains("AccountName"));
        assertTrue(result.contains("Test Account"));
    }
}
