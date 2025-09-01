package com.cashly.cashly_api.incomes.domain.valueobjects;

import java.util.Objects;

public class Description {
    private static final int MAX_LENGTH = 255;
    private final String value;
    
    public Description(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        
        String trimmedValue = value.trim();
        if (trimmedValue.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        
        if (trimmedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Description cannot be longer than 255 characters");
        }
        
        this.value = trimmedValue;
    }
    
    public String getValue() {
        return value;
    }
    
    public boolean contains(String keyword) {
        if (keyword == null) {
            return false;
        }
        return value.toLowerCase().contains(keyword.toLowerCase());
    }
    
    public int length() {
        return value.length();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Description description = (Description) obj;
        return Objects.equals(value, description.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
    
    @Override
    public String toString() {
        return "Description{" + "value='" + value + '\'' + '}';
    }
}