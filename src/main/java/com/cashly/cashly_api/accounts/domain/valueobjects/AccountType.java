package com.cashly.cashly_api.accounts.domain.valueobjects;

import java.util.Arrays;
import java.util.Objects;

public class AccountType {
    public enum Type {
        CHECKING,
        SAVINGS,
        CREDIT_CARD,
        CASH,
        INVESTMENT
    }

    private final Type value;

    public AccountType(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }
        try {
            this.value = Type.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account type. Must be one of: " +
                Arrays.toString(Type.values()));
        }
    }

    public AccountType(Type value) {
        if (value == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }
        this.value = value;
    }

    public String getValue() {
        return value.name();
    }

    public Type getType() {
        return value;
    }

    public boolean isCreditType() {
        return value == Type.CREDIT_CARD;
    }

    public boolean isDebitType() {
        return value == Type.CHECKING || value == Type.SAVINGS ||
               value == Type.CASH || value == Type.INVESTMENT;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccountType accountType = (AccountType) obj;
        return value == accountType.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "AccountType{" + "value=" + value + '}';
    }
}
