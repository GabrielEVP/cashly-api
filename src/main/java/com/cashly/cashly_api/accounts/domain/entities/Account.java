package com.cashly.cashly_api.accounts.domain.entities;

import com.cashly.cashly_api.accounts.domain.valueobjects.*;

import java.time.LocalDateTime;
import java.util.Objects;

public class Account {
    private final AccountId id;
    private AccountName name;
    private final AccountType type;
    private Balance balance;
    private final Currency currency;
    private boolean active;
    private final String userId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Account(AccountId id, AccountName name, AccountType type, Balance balance,
                   Currency currency, String userId) {
        validateParameters(id, name, type, balance, currency, userId);

        this.id = id;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.currency = currency;
        this.userId = userId.trim();
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private void validateParameters(AccountId id, AccountName name, AccountType type,
                                   Balance balance, Currency currency, String userId) {
        if (id == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("Account name cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }
        if (balance == null) {
            throw new IllegalArgumentException("Balance cannot be null");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
    }

    public void updateName(AccountName newName) {
        if (newName == null) {
            throw new IllegalArgumentException("Account name cannot be null");
        }
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateBalance(Balance newBalance) {
        if (newBalance == null) {
            throw new IllegalArgumentException("Balance cannot be null");
        }
        this.balance = newBalance;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean belongsToUser(String userId) {
        if (userId == null) {
            return false;
        }
        return this.userId.equals(userId);
    }

    public boolean canAcceptTransactions() {
        return this.active;
    }

    public boolean isDebitAccount() {
        return type.isDebitType();
    }

    public boolean isCreditAccount() {
        return type.isCreditType();
    }

    // Getters
    public AccountId getId() {
        return id;
    }

    public AccountName getName() {
        return name;
    }

    public AccountType getType() {
        return type;
    }

    public Balance getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public boolean isActive() {
        return active;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Account account = (Account) obj;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name=" + name +
                ", type=" + type +
                ", balance=" + balance +
                ", currency=" + currency +
                ", active=" + active +
                ", userId='" + userId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
