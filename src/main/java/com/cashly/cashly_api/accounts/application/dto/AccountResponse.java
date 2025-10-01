package com.cashly.cashly_api.accounts.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountResponse {
    private final String id;
    private final String name;
    private final String type;
    private final BigDecimal balance;
    private final String currency;
    private final boolean active;
    private final String userId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public AccountResponse(String id, String name, String type, BigDecimal balance,
                          String currency, boolean active, String userId,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.currency = currency;
        this.active = active;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCurrency() {
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
}
