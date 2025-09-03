package com.cashly.cashly_api.expenses.infrastructure.persistence;

import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.valueobjects.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "expenses", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_user_category", columnList = "user_id, category"),
    @Index(name = "idx_user_created", columnList = "user_id, created_at")
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class ExpenseEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    @EqualsAndHashCode.Include
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


    public static ExpenseEntity fromDomain(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense cannot be null");
        }

        ExpenseEntity entity = new ExpenseEntity();
        entity.id = expense.getId().getValue().toString();
        entity.amount = expense.getAmount().getValue();
        entity.description = expense.getDescription().getValue();
        entity.category = expense.getCategory().getValue();
        entity.date = expense.getDate();
        entity.userId = expense.getUserId();
        entity.createdAt = expense.getCreatedAt();
        entity.updatedAt = expense.getUpdatedAt();
        
        return entity;
    }

    public void updateFromDomain(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense cannot be null");
        }

        this.amount = expense.getAmount().getValue();
        this.description = expense.getDescription().getValue();
        this.category = expense.getCategory().getValue();
        this.updatedAt = expense.getUpdatedAt();
    }

    public Expense toDomain() {
        ExpenseId expenseId = new ExpenseId(UUID.fromString(this.id));
        Amount domainAmount = new Amount(this.amount);
        Description domainDescription = new Description(this.description);
        Category domainCategory = new Category(this.category);
        
        Expense expense = new Expense(expenseId, domainAmount, domainDescription, domainCategory, this.date, this.userId);
        
        try {
            java.lang.reflect.Field createdAtField = Expense.class.getDeclaredField("createdAt");
            java.lang.reflect.Field updatedAtField = Expense.class.getDeclaredField("updatedAt");
            
            createdAtField.setAccessible(true);
            updatedAtField.setAccessible(true);
            
            createdAtField.set(expense, this.createdAt);
            updatedAtField.set(expense, this.updatedAt);
            
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set timestamps on domain entity", e);
        }
        
        return expense;
    }

}