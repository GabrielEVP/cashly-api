package com.cashly.cashly_api.auth.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileUnitTest {

    @Test
    void should_CreateUserProfile_When_ValidNamesProvided() {
        String firstName = "John";
        String lastName = "Doe";

        UserProfile profile = new UserProfile(firstName, lastName);

        assertNotNull(profile);
        assertEquals("John", profile.getFirstName());
        assertEquals("Doe", profile.getLastName());
        assertEquals("John Doe", profile.getFullName());
    }

    @Test
    void should_TrimWhitespace_When_NamesProvided() {
        String firstName = "  John  ";
        String lastName = "  Doe  ";

        UserProfile profile = new UserProfile(firstName, lastName);

        assertEquals("John", profile.getFirstName());
        assertEquals("Doe", profile.getLastName());
    }

    @Test
    void should_ThrowException_When_NullFirstNameProvided() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new UserProfile(null, "Doe")
        );
        assertEquals("First name cannot be null or empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_EmptyFirstNameProvided() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new UserProfile("   ", "Doe")
        );
        assertEquals("First name cannot be null or empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullLastNameProvided() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new UserProfile("John", null)
        );
        assertEquals("Last name cannot be null or empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_EmptyLastNameProvided() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new UserProfile("John", "   ")
        );
        assertEquals("Last name cannot be null or empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_FirstNameExceedsMaxLength() {
        String longFirstName = "a".repeat(101);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new UserProfile(longFirstName, "Doe")
        );
        assertEquals("First name cannot exceed 100 characters", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_LastNameExceedsMaxLength() {
        String longLastName = "a".repeat(101);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new UserProfile("John", longLastName)
        );
        assertEquals("Last name cannot exceed 100 characters", exception.getMessage());
    }

    @Test
    void should_AcceptMaxLengthNames_When_AtBoundary() {
        String maxLengthName = "a".repeat(100);

        UserProfile profile = new UserProfile(maxLengthName, maxLengthName);

        assertNotNull(profile);
        assertEquals(maxLengthName, profile.getFirstName());
        assertEquals(maxLengthName, profile.getLastName());
    }

    @Test
    void should_ReturnFullName_When_ProfileCreated() {
        UserProfile profile = new UserProfile("Jane", "Smith");

        String fullName = profile.getFullName();

        assertEquals("Jane Smith", fullName);
    }

    @Test
    void should_BeEqual_When_SameNamesProvided() {
        UserProfile profile1 = new UserProfile("John", "Doe");
        UserProfile profile2 = new UserProfile("John", "Doe");

        assertEquals(profile1, profile2);
        assertEquals(profile1.hashCode(), profile2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentFirstNameProvided() {
        UserProfile profile1 = new UserProfile("John", "Doe");
        UserProfile profile2 = new UserProfile("Jane", "Doe");

        assertNotEquals(profile1, profile2);
    }

    @Test
    void should_NotBeEqual_When_DifferentLastNameProvided() {
        UserProfile profile1 = new UserProfile("John", "Doe");
        UserProfile profile2 = new UserProfile("John", "Smith");

        assertNotEquals(profile1, profile2);
    }

    @Test
    void should_HaveToString_When_ProfileCreated() {
        UserProfile profile = new UserProfile("John", "Doe");

        String result = profile.toString();

        assertNotNull(result);
        assertTrue(result.contains("UserProfile"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
    }
}
