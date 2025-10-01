package com.cashly.cashly_api.accounts.application.dto;

import java.math.BigDecimal;

public class UpdateAccountRequest {
    private final String name;
    private final BigDecimal balance;

    public UpdateAccountRequest(String name, BigDecimal balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
