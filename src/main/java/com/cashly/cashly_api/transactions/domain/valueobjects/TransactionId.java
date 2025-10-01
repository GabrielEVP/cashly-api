package com.cashly.cashly_api.transactions.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public class TransactionId {
    private final UUID value;

    public TransactionId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Transaction ID cannot be null");
        }
        this.value = value;
    }

    public static TransactionId generate() {
        return new TransactionId(UUID.randomUUID());
    }

    public static TransactionId from(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Transaction ID string cannot be null");
        }
        try {
            return new TransactionId(UUID.fromString(value.trim()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction ID format", e);
        }
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TransactionId that = (TransactionId) obj;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "TransactionId{" + "value=" + value + '}';
    }
}
