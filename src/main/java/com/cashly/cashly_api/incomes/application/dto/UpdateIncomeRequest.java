package com.cashly.cashly_api.incomes.application.dto;

import java.util.Objects;

/**
 * Data Transfer Object for updating an existing Income.
 * Contains only the fields that can be updated: description and category.
 * Amount is not updatable as per business rules.
 */
public class UpdateIncomeRequest {
    
    private final String description;
    private final String category;
    
    public UpdateIncomeRequest(String description, String category) {
        this.description = description;
        this.category = category;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getCategory() {
        return category;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UpdateIncomeRequest that = (UpdateIncomeRequest) obj;
        return Objects.equals(description, that.description) &&
               Objects.equals(category, that.category);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(description, category);
    }
    
    @Override
    public String toString() {
        return "UpdateIncomeRequest{" +
                "description='" + description + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}