package com.cashly.cashly_api.incomes.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public class IncomeId {
    private final UUID value;
    
    public IncomeId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Income ID cannot be null");
        }
        this.value = value;
    }
    
    public UUID getValue() {
        return value;
    }
    
    public static IncomeId generate() {
        return new IncomeId(UUID.randomUUID());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IncomeId incomeId = (IncomeId) obj;
        return Objects.equals(value, incomeId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
    
    @Override
    public String toString() {
        return "IncomeId{" + "value=" + value + '}';
    }
}