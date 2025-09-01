package com.cashly.cashly_api.incomes.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object for Income responses.
 * Contains all income information to be sent to clients.
 */
public class IncomeResponse {
    
    private final String id;
    private final BigDecimal amount;
    private final String description;
    private final String category;
    private final String userId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    
    public IncomeResponse(String id, BigDecimal amount, String description, String category, 
                         String userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public String getId() {
        return id;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IncomeResponse that = (IncomeResponse) obj;
        return Objects.equals(id, that.id) &&
               Objects.equals(amount, that.amount) &&
               Objects.equals(description, that.description) &&
               Objects.equals(category, that.category) &&
               Objects.equals(userId, that.userId) &&
               Objects.equals(createdAt, that.createdAt) &&
               Objects.equals(updatedAt, that.updatedAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, amount, description, category, userId, createdAt, updatedAt);
    }
    
    @Override
    public String toString() {
        return "IncomeResponse{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", userId='" + userId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}