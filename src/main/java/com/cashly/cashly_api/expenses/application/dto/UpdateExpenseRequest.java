package com.cashly.cashly_api.expenses.application.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class UpdateExpenseRequest {
    
    private final BigDecimal amount;
    private final String description;
    private final String category;
    
    public UpdateExpenseRequest(BigDecimal amount, String description, String category) {
        this.amount = amount;
        this.description = description;
        this.category = category;
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
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UpdateExpenseRequest that = (UpdateExpenseRequest) obj;
        return Objects.equals(amount, that.amount) &&
               Objects.equals(description, that.description) &&
               Objects.equals(category, that.category);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount, description, category);
    }
    
    @Override
    public String toString() {
        return "UpdateExpenseRequest{" +
                "amount=" + amount +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}