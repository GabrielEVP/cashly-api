package com.cashly.cashly_api.accounts.application.dto;

import java.math.BigDecimal;

public class CreateAccountRequest {
    private final String name;
    private final String type;
    private final BigDecimal balance;
    private final String currency;
    private final String userId;

    public CreateAccountRequest(String name, String type, BigDecimal balance,
                               String currency, String userId) {
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.currency = currency;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }
}
