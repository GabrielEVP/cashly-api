package com.cashly.cashly_api.incomes.application.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Data Transfer Object for creating a new Income.
 * Contains all the necessary information to create an income entity.
 * Validation will be handled at the use case level.
 */
public class CreateIncomeRequest {
    
    private final BigDecimal amount;
    private final String description;
    private final String category;
    private final String userId;
    
    public CreateIncomeRequest(BigDecimal amount, String description, String category, String userId) {
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.userId = userId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public String getUserId() {
        return userId;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CreateIncomeRequest that = (CreateIncomeRequest) obj;
        return Objects.equals(amount, that.amount) &&
               Objects.equals(description, that.description) &&
               Objects.equals(category, that.category) &&
               Objects.equals(userId, that.userId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount, description, category, userId);
    }
    
    @Override
    public String toString() {
        return "CreateIncomeRequest{" +
                "amount=" + amount +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}