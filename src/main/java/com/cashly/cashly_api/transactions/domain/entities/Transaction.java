package com.cashly.cashly_api.transactions.domain.entities;

import com.cashly.cashly_api.transactions.domain.valueobjects.*;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private final TransactionId id;
    private final String userId;
    private final TransactionType type;
    private TransactionStatus status;
    private final Amount amount;
    private final String currency;
    private Description description;
    private final TransactionDate transactionDate;
    private final String sourceAccountId;
    private final String destinationAccountId;
    private final String expenseId;
    private final String incomeId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Transaction(TransactionId id, String userId, TransactionType type,
                      TransactionStatus status, Amount amount, String currency,
                      Description description, TransactionDate transactionDate,
                      String sourceAccountId, String destinationAccountId,
                      String expenseId, String incomeId) {
        validateParameters(id, userId, type, status, amount, currency, description, transactionDate);
        validateAccountRequirements(type, sourceAccountId, destinationAccountId);
        validateSameAccountNotAllowed(sourceAccountId, destinationAccountId);
        validateLinkageRules(type, expenseId, incomeId);

        this.id = id;
        this.userId = userId.trim();
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.currency = currency.trim().toUpperCase();
        this.description = description;
        this.transactionDate = transactionDate;
        this.sourceAccountId = sourceAccountId != null ? sourceAccountId.trim() : null;
        this.destinationAccountId = destinationAccountId != null ? destinationAccountId.trim() : null;
        this.expenseId = expenseId != null ? expenseId.trim() : null;
        this.incomeId = incomeId != null ? incomeId.trim() : null;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private void validateParameters(TransactionId id, String userId, TransactionType type,
                                   TransactionStatus status, Amount amount, String currency,
                                   Description description, TransactionDate transactionDate) {
        if (id == null) {
            throw new IllegalArgumentException("Transaction ID cannot be null");
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Transaction status cannot be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        if (transactionDate == null) {
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
    }

    private void validateAccountRequirements(TransactionType type, String sourceAccountId, String destinationAccountId) {
        if (type.requiresSourceAccount() && (sourceAccountId == null || sourceAccountId.trim().isEmpty())) {
            throw new IllegalArgumentException(type + " requires a source account");
        }
        if (type.requiresDestinationAccount() && (destinationAccountId == null || destinationAccountId.trim().isEmpty())) {
            throw new IllegalArgumentException(type + " requires a destination account");
        }
    }

    private void validateSameAccountNotAllowed(String sourceAccountId, String destinationAccountId) {
        if (sourceAccountId != null && destinationAccountId != null &&
            sourceAccountId.trim().equals(destinationAccountId.trim())) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same");
        }
    }

    private void validateLinkageRules(TransactionType type, String expenseId, String incomeId) {
        if (expenseId != null && !type.canLinkToExpense()) {
            throw new IllegalArgumentException(type + " cannot be linked to an expense");
        }
        if (incomeId != null && !type.canLinkToIncome()) {
            throw new IllegalArgumentException(type + " cannot be linked to an income");
        }
    }

    public void updateStatus(TransactionStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Transaction status cannot be null");
        }
        if (!this.status.canTransitionTo(newStatus)) {
            throw new IllegalStateException("Cannot transition from " + this.status + " to " + newStatus);
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDescription(Description newDescription) {
        if (newDescription == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        if (this.status.isFinal()) {
            throw new IllegalStateException("Cannot update description of a completed transaction");
        }
        this.description = newDescription;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        updateStatus(TransactionStatus.CANCELLED);
    }

    public void complete() {
        updateStatus(TransactionStatus.COMPLETED);
    }

    public void markAsFailed() {
        updateStatus(TransactionStatus.FAILED);
    }

    public boolean belongsToUser(String userId) {
        if (userId == null) {
            return false;
        }
        return this.userId.equals(userId);
    }

    public boolean involvesAccount(String accountId) {
        if (accountId == null) {
            return false;
        }
        return accountId.equals(this.sourceAccountId) || accountId.equals(this.destinationAccountId);
    }

    public boolean isPending() {
        return this.status == TransactionStatus.PENDING;
    }

    public boolean isCompleted() {
        return this.status == TransactionStatus.COMPLETED;
    }

    public boolean isFailed() {
        return this.status == TransactionStatus.FAILED;
    }

    public boolean isCancelled() {
        return this.status == TransactionStatus.CANCELLED;
    }

    // Getters
    public TransactionId getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Amount getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Description getDescription() {
        return description;
    }

    public TransactionDate getTransactionDate() {
        return transactionDate;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public String getDestinationAccountId() {
        return destinationAccountId;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public String getIncomeId() {
        return incomeId;
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
        Transaction that = (Transaction) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", description=" + description +
                ", transactionDate=" + transactionDate +
                ", sourceAccountId='" + sourceAccountId + '\'' +
                ", destinationAccountId='" + destinationAccountId + '\'' +
                ", expenseId='" + expenseId + '\'' +
                ", incomeId='" + incomeId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
