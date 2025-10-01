package com.cashly.cashly_api.transactions.domain.valueobjects;

import java.util.Arrays;

public enum TransactionType {
    TRANSFER,
    DEPOSIT,
    WITHDRAWAL,
    PAYMENT,
    REFUND;

    public boolean requiresSourceAccount() {
        return this == TRANSFER || this == WITHDRAWAL || this == PAYMENT;
    }

    public boolean requiresDestinationAccount() {
        return this == TRANSFER || this == DEPOSIT || this == REFUND;
    }

    public boolean canLinkToExpense() {
        return this == PAYMENT;
    }

    public boolean canLinkToIncome() {
        return this == REFUND;
    }

    public static TransactionType fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        try {
            return TransactionType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction type: " + value +
                ". Must be one of: " + Arrays.toString(TransactionType.values()));
        }
    }
}
