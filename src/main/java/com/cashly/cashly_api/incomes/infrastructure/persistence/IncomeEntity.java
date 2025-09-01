package com.cashly.cashly_api.incomes.infrastructure.persistence;

import com.cashly.cashly_api.incomes.domain.entities.Income;
import com.cashly.cashly_api.incomes.domain.valueobjects.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * JPA Entity for Income persistence.
 * Maps between domain Income entity and database incomes table.
 */
@Entity
@Table(name = "incomes", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_user_category", columnList = "user_id, category"),
    @Index(name = "idx_user_created", columnList = "user_id, created_at")
})
public class IncomeEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @Column(name = "category", length = 50, nullable = false)
    private String category;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Default constructor for JPA.
     */
    public IncomeEntity() {
    }

    /**
     * Creates an IncomeEntity from a domain Income.
     * @param income the domain income entity
     * @return a new IncomeEntity
     * @throws IllegalArgumentException if income is null
     */
    public static IncomeEntity fromDomain(Income income) {
        if (income == null) {
            throw new IllegalArgumentException("Income cannot be null");
        }

        IncomeEntity entity = new IncomeEntity();
        entity.id = income.getId().getValue().toString();
        entity.amount = income.getAmount().getValue();
        entity.description = income.getDescription().getValue();
        entity.category = income.getCategory().getValue();
        entity.date = income.getDate();
        entity.userId = income.getUserId();
        entity.createdAt = income.getCreatedAt();
        entity.updatedAt = income.getUpdatedAt();
        
        return entity;
    }

    /**
     * Updates this entity from a domain Income.
     * Note: ID, date, userId, and createdAt are not updated as they are immutable.
     * @param income the domain income entity
     * @throws IllegalArgumentException if income is null
     */
    public void updateFromDomain(Income income) {
        if (income == null) {
            throw new IllegalArgumentException("Income cannot be null");
        }

        this.amount = income.getAmount().getValue();
        this.description = income.getDescription().getValue();
        this.category = income.getCategory().getValue();
        this.updatedAt = income.getUpdatedAt();
    }

    /**
     * Converts this entity to a domain Income.
     * @return a domain Income entity
     */
    public Income toDomain() {
        IncomeId incomeId = new IncomeId(UUID.fromString(this.id));
        Amount domainAmount = new Amount(this.amount);
        Description domainDescription = new Description(this.description);
        Category domainCategory = new Category(this.category);
        
        // Create income with constructor that sets createdAt and updatedAt
        Income income = new Income(incomeId, domainAmount, domainDescription, domainCategory, this.date, this.userId);
        
        // Use reflection to set the correct timestamps from the entity
        try {
            java.lang.reflect.Field createdAtField = Income.class.getDeclaredField("createdAt");
            java.lang.reflect.Field updatedAtField = Income.class.getDeclaredField("updatedAt");
            
            createdAtField.setAccessible(true);
            updatedAtField.setAccessible(true);
            
            createdAtField.set(income, this.createdAt);
            updatedAtField.set(income, this.updatedAt);
            
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // This should not happen in normal circumstances
            throw new RuntimeException("Failed to set timestamps on domain entity", e);
        }
        
        return income;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IncomeEntity that = (IncomeEntity) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "IncomeEntity{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", date=" + date +
                ", userId='" + userId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}