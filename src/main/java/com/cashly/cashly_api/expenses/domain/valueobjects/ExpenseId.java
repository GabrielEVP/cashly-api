package com.cashly.cashly_api.expenses.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public class ExpenseId {
    private final UUID value;
    
    public ExpenseId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Expense ID cannot be null");
        }
        this.value = value;
    }
    
    public UUID getValue() {
        return value;
    }
    
    public static ExpenseId generate() {
        return new ExpenseId(UUID.randomUUID());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ExpenseId expenseId = (ExpenseId) obj;
        return Objects.equals(value, expenseId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
    
    @Override
    public String toString() {
        return "ExpenseId{" + "value=" + value + '}';
    }
}