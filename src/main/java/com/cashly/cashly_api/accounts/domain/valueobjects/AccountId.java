package com.cashly.cashly_api.accounts.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public class AccountId {
    private final UUID value;

    public AccountId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        this.value = value;
    }

    public static AccountId generate() {
        return new AccountId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccountId accountId = (AccountId) obj;
        return Objects.equals(value, accountId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "AccountId{" + "value=" + value + '}';
    }
}
