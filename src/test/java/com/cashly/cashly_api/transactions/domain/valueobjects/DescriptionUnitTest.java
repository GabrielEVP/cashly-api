package com.cashly.cashly_api.transactions.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescriptionUnitTest {

    @Test
    void should_CreateDescription_When_ValidValueProvided() {
        // Arrange
        String value = "Transaction for groceries";

        // Act
        Description description = new Description(value);

        // Assert
        assertNotNull(description);
        assertEquals(value, description.getValue());
    }

    @Test
    void should_ThrowException_When_NullValueProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Description(null)
        );
        assertEquals("Description cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_EmptyValueProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Description("")
        );
        assertEquals("Description cannot be empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_WhitespaceOnlyProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Description("   ")
        );
        assertEquals("Description cannot be empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_ValueExceedsMaxLength() {
        // Arrange
        String longValue = "a".repeat(256);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Description(longValue)
        );
        assertEquals("Description cannot be longer than 255 characters", exception.getMessage());
    }

    @Test
    void should_TrimWhitespace_When_ValueWithSpacesProvided() {
        // Arrange
        String value = "  Transaction description  ";

        // Act
        Description description = new Description(value);

        // Assert
        assertEquals("Transaction description", description.getValue());
    }

    @Test
    void should_AcceptMaxLengthValue_When_ExactlyAtLimit() {
        // Arrange
        String maxValue = "a".repeat(255);

        // Act
        Description description = new Description(maxValue);

        // Assert
        assertEquals(255, description.length());
    }

    @Test
    void should_ReturnTrue_When_DescriptionContainsKeyword() {
        // Arrange
        Description description = new Description("Payment for groceries");

        // Act & Assert
        assertTrue(description.contains("groceries"));
        assertTrue(description.contains("GROCERIES"));
        assertTrue(description.contains("Payment"));
    }

    @Test
    void should_ReturnFalse_When_DescriptionDoesNotContainKeyword() {
        // Arrange
        Description description = new Description("Payment for groceries");

        // Act & Assert
        assertFalse(description.contains("electronics"));
    }

    @Test
    void should_ReturnFalse_When_KeywordIsNull() {
        // Arrange
        Description description = new Description("Payment for groceries");

        // Act & Assert
        assertFalse(description.contains(null));
    }

    @Test
    void should_ReturnLength_When_LengthCalled() {
        // Arrange
        Description description = new Description("Test description");

        // Act
        int length = description.length();

        // Assert
        assertEquals(16, length);
    }

    @Test
    void should_BeEqual_When_SameValueUsed() {
        // Arrange
        Description description1 = new Description("Same description");
        Description description2 = new Description("Same description");

        // Act & Assert
        assertEquals(description1, description2);
        assertEquals(description1.hashCode(), description2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentValueUsed() {
        // Arrange
        Description description1 = new Description("Description 1");
        Description description2 = new Description("Description 2");

        // Act & Assert
        assertNotEquals(description1, description2);
    }

    @Test
    void should_ReturnStringRepresentation_When_ToStringCalled() {
        // Arrange
        Description description = new Description("Test description");

        // Act
        String result = description.toString();

        // Assert
        assertTrue(result.contains("Description"));
        assertTrue(result.contains("Test description"));
    }
}
