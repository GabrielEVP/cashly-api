package com.cashly.cashly_api.transactions.domain.valueobjects;

import java.util.Arrays;

public enum TransactionStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED;

    public boolean canTransitionTo(TransactionStatus newStatus) {
        if (this == PENDING) {
            return newStatus == COMPLETED || newStatus == FAILED || newStatus == CANCELLED;
        }
        return false;
    }

    public boolean isFinal() {
        return this == COMPLETED || this == FAILED || this == CANCELLED;
    }

    public static TransactionStatus fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Transaction status cannot be null");
        }
        try {
            return TransactionStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction status: " + value +
                ". Must be one of: " + Arrays.toString(TransactionStatus.values()));
        }
    }
}
