package com.cashly.cashly_api.auth.domain.valueobjects;

import java.util.Objects;

public class UserProfile {
    private static final int MAX_NAME_LENGTH = 100;

    private final String firstName;
    private final String lastName;

    public UserProfile(String firstName, String lastName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }

        String trimmedFirstName = firstName.trim();
        String trimmedLastName = lastName.trim();

        if (trimmedFirstName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("First name cannot exceed " + MAX_NAME_LENGTH + " characters");
        }

        if (trimmedLastName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Last name cannot exceed " + MAX_NAME_LENGTH + " characters");
        }

        this.firstName = trimmedFirstName;
        this.lastName = trimmedLastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserProfile that = (UserProfile) obj;
        return Objects.equals(firstName, that.firstName) &&
               Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
