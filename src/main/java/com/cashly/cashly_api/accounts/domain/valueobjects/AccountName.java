package com.cashly.cashly_api.accounts.domain.valueobjects;

import java.util.Objects;

public class AccountName {
    private static final int MAX_LENGTH = 100;
    private final String value;

    public AccountName(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Account name cannot be null");
        }
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("Account name cannot be empty");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Account name cannot exceed " + MAX_LENGTH + " characters");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccountName accountName = (AccountName) obj;
        return Objects.equals(value, accountName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "AccountName{" + "value='" + value + '\'' + '}';
    }
}
