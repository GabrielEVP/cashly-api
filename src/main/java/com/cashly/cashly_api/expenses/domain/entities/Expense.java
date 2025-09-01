package com.cashly.cashly_api.expenses.domain.entities;

import com.cashly.cashly_api.expenses.domain.valueobjects.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Objects;

public class Expense {
    private final ExpenseId id;
    private Amount amount;
    private Description description;
    private Category category;
    private final LocalDate date;
    private final String userId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Expense(ExpenseId id, Amount amount, Description description, Category category, 
                   LocalDate date, String userId) {
        validateParameters(id, amount, description, category, date, userId);
        
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.date = date;
        this.userId = userId.trim();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private void validateParameters(ExpenseId id, Amount amount, Description description, 
                                  Category category, LocalDate date, String userId) {
        if (id == null) {
            throw new IllegalArgumentException("Expense ID cannot be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Expense date cannot be in the future");
        }
    }

    public void updateAmount(Amount newAmount) {
        if (newAmount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        this.amount = newAmount;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDescription(Description newDescription) {
        if (newDescription == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        this.description = newDescription;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateCategory(Category newCategory) {
        if (newCategory == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        this.category = newCategory;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean belongsToUser(String userId) {
        if (userId == null) {
            return false;
        }
        return this.userId.equals(userId);
    }

    public boolean isFromSamePeriod(LocalDate otherDate) {
        if (otherDate == null) {
            return false;
        }
        YearMonth thisMonth = YearMonth.from(this.date);
        YearMonth otherMonth = YearMonth.from(otherDate);
        return thisMonth.equals(otherMonth);
    }

    // Getters
    public ExpenseId getId() {
        return id;
    }

    public Amount getAmount() {
        return amount;
    }

    public Description getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
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
        Expense expense = (Expense) obj;
        return Objects.equals(id, expense.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", amount=" + amount +
                ", description=" + description +
                ", category=" + category +
                ", date=" + date +
                ", userId='" + userId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}