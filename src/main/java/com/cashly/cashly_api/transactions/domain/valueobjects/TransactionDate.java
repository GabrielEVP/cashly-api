package com.cashly.cashly_api.transactions.domain.valueobjects;

import java.time.LocalDate;
import java.util.Objects;

public class TransactionDate {
    private final LocalDate value;

    public TransactionDate(LocalDate value) {
        if (value == null) {
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
        if (value.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Transaction date cannot be in the future");
        }
        this.value = value;
    }

    public static TransactionDate now() {
        return new TransactionDate(LocalDate.now());
    }

    public LocalDate getValue() {
        return value;
    }

    public boolean isBefore(TransactionDate other) {
        if (other == null) {
            throw new IllegalArgumentException("Other transaction date cannot be null");
        }
        return this.value.isBefore(other.value);
    }

    public boolean isAfter(TransactionDate other) {
        if (other == null) {
            throw new IllegalArgumentException("Other transaction date cannot be null");
        }
        return this.value.isAfter(other.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TransactionDate that = (TransactionDate) obj;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "TransactionDate{" + "value=" + value + '}';
    }
}
