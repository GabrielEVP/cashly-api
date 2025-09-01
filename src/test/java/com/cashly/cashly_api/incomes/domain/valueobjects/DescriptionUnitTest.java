package com.cashly.cashly_api.incomes.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescriptionUnitTest {

    @Test
    void should_CreateDescription_When_ValidValueProvided() {
        // Arrange
        String validDescription = "Salary payment";
        
        // Act
        Description description = new Description(validDescription);
        
        // Assert
        assertNotNull(description);
        assertEquals(validDescription, description.getValue());
    }

    @Test
    void should_ThrowException_When_NullValueProvided() {
        // Arrange
        String nullDescription = null;
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Description(nullDescription)
        );
        
        assertEquals("Description cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_EmptyValueProvided() {
        // Arrange
        String emptyDescription = "";
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Description(emptyDescription)
        );
        
        assertEquals("Description cannot be empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_BlankValueProvided() {
        // Arrange
        String blankDescription = "   ";
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Description(blankDescription)
        );
        
        assertEquals("Description cannot be empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_TooLongValueProvided() {
        // Arrange
        String tooLongDescription = "A".repeat(256); // Assume max length is 255
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Description(tooLongDescription)
        );
        
        assertEquals("Description cannot be longer than 255 characters", exception.getMessage());
    }

    @Test
    void should_AcceptMaxLengthValue_When_ExactlyAtLimit() {
        // Arrange
        String maxLengthDescription = "A".repeat(255);
        
        // Act
        Description description = new Description(maxLengthDescription);
        
        // Assert
        assertNotNull(description);
        assertEquals(maxLengthDescription, description.getValue());
    }

    @Test
    void should_TrimWhitespace_When_ValueHasLeadingTrailingSpaces() {
        // Arrange
        String descriptionWithSpaces = "  Salary payment  ";
        String expectedTrimmed = "Salary payment";
        
        // Act
        Description description = new Description(descriptionWithSpaces);
        
        // Assert
        assertEquals(expectedTrimmed, description.getValue());
    }

    @Test
    void should_BeEqual_When_SameDescriptionValue() {
        // Arrange
        String value = "Salary payment";
        Description description1 = new Description(value);
        Description description2 = new Description(value);
        
        // Act & Assert
        assertEquals(description1, description2);
        assertEquals(description1.hashCode(), description2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentDescriptionValues() {
        // Arrange
        Description description1 = new Description("Salary payment");
        Description description2 = new Description("Bonus payment");
        
        // Act & Assert
        assertNotEquals(description1, description2);
    }

    @Test
    void should_ReturnTrue_When_ContainsKeyword() {
        // Arrange
        Description description = new Description("Monthly salary payment");
        
        // Act & Assert
        assertTrue(description.contains("salary"));
        assertTrue(description.contains("SALARY")); // Case insensitive
        assertFalse(description.contains("bonus"));
    }

    @Test
    void should_ReturnProperLength_When_Asked() {
        // Arrange
        Description description = new Description("Hello");
        
        // Act & Assert
        assertEquals(5, description.length());
    }
}