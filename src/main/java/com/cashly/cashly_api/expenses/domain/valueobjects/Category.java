package com.cashly.cashly_api.expenses.domain.valueobjects;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class Category {
    private static final String[] VALID_CATEGORIES = {
        "FOOD_DINING", "TRANSPORTATION", "HOUSING", "HEALTHCARE", 
        "ENTERTAINMENT", "SHOPPING", "EDUCATION", "TRAVEL", "OTHER"
    };
    private static final Set<String> VALID_CATEGORIES_SET = Set.of(VALID_CATEGORIES);
    
    private final String value;
    
    public Category(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        
        String normalizedValue = value.trim().toUpperCase();
        if (normalizedValue.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        
        if (!VALID_CATEGORIES_SET.contains(normalizedValue)) {
            throw new IllegalArgumentException(
                "Invalid category: " + normalizedValue + 
                ". Valid categories are: " + String.join(", ", VALID_CATEGORIES)
            );
        }
        
        this.value = normalizedValue;
    }
    
    public String getValue() {
        return value;
    }
    
    public boolean isFoodDining() {
        return "FOOD_DINING".equals(value);
    }
    
    public boolean isTransportation() {
        return "TRANSPORTATION".equals(value);
    }
    
    public boolean isHousing() {
        return "HOUSING".equals(value);
    }
    
    public boolean isHealthcare() {
        return "HEALTHCARE".equals(value);
    }
    
    public boolean isEntertainment() {
        return "ENTERTAINMENT".equals(value);
    }
    
    public boolean isShopping() {
        return "SHOPPING".equals(value);
    }
    
    public boolean isEducation() {
        return "EDUCATION".equals(value);
    }
    
    public boolean isTravel() {
        return "TRAVEL".equals(value);
    }
    
    public boolean isOther() {
        return "OTHER".equals(value);
    }
    
    public static String[] getValidCategories() {
        return Arrays.copyOf(VALID_CATEGORIES, VALID_CATEGORIES.length);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return Objects.equals(value, category.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
    
    @Override
    public String toString() {
        return "Category{" + "value='" + value + '\'' + '}';
    }
}