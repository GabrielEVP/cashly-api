package com.cashly.cashly_api.transactions.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String id;
    private String userId;
    private String type;
    private String status;
    private BigDecimal amount;
    private String currency;
    private String description;
    private LocalDate transactionDate;
    private String sourceAccountId;
    private String destinationAccountId;
    private String expenseId;
    private String incomeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
