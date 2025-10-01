package com.cashly.cashly_api.incomes.domain.valueobjects;

import java.math.BigDecimal;
import java.util.Objects;

public class Amount {
    private final BigDecimal value;
    
    public Amount(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.value = value;
    }
    
    public BigDecimal getValue() {
        return new BigDecimal(value.toString());
    }
    
    public Amount add(Amount other) {
        if (other == null) {
            throw new IllegalArgumentException("Other amount cannot be null");
        }
        return new Amount(this.value.add(other.value));
    }
    
    public Amount subtract(Amount other) {
        if (other == null) {
            throw new IllegalArgumentException("Other amount cannot be null");
        }
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Subtraction result cannot be negative");
        }
        return new Amount(result);
    }
    
    public boolean isGreaterThan(Amount other) {
        if (other == null) {
            throw new IllegalArgumentException("Other amount cannot be null");
        }
        return this.value.compareTo(other.value) > 0;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Amount amount = (Amount) obj;
        return Objects.equals(value, amount.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
    
    @Override
    public String toString() {
        return "Amount{" + "value=" + value + '}';
    }
}