package com.cashly.cashly_api.transactions.domain.services;

import com.cashly.cashly_api.transactions.domain.entities.Transaction;
import com.cashly.cashly_api.transactions.domain.valueobjects.TransactionType;

public class TransactionService {

    public void validateTransactionIntegrity(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }

        validateAccountRequirementsForType(transaction);
        validateCurrencyConsistency(transaction);
    }

    private void validateAccountRequirementsForType(Transaction transaction) {
        TransactionType type = transaction.getType();

        switch (type) {
            case TRANSFER:
                if (transaction.getSourceAccountId() == null || transaction.getDestinationAccountId() == null) {
                    throw new IllegalArgumentException("TRANSFER requires both source and destination accounts");
                }
                break;
            case DEPOSIT:
                if (transaction.getSourceAccountId() != null) {
                    throw new IllegalArgumentException("DEPOSIT must not have a source account");
                }
                if (transaction.getDestinationAccountId() == null) {
                    throw new IllegalArgumentException("DEPOSIT requires a destination account");
                }
                break;
            case WITHDRAWAL:
                if (transaction.getDestinationAccountId() != null) {
                    throw new IllegalArgumentException("WITHDRAWAL must not have a destination account");
                }
                if (transaction.getSourceAccountId() == null) {
                    throw new IllegalArgumentException("WITHDRAWAL requires a source account");
                }
                break;
            case PAYMENT:
                if (transaction.getDestinationAccountId() != null) {
                    throw new IllegalArgumentException("PAYMENT must not have a destination account");
                }
                if (transaction.getSourceAccountId() == null) {
                    throw new IllegalArgumentException("PAYMENT requires a source account");
                }
                break;
            case REFUND:
                if (transaction.getSourceAccountId() != null) {
                    throw new IllegalArgumentException("REFUND must not have a source account");
                }
                if (transaction.getDestinationAccountId() == null) {
                    throw new IllegalArgumentException("REFUND requires a destination account");
                }
                break;
        }
    }

    private void validateCurrencyConsistency(Transaction transaction) {
        String currency = transaction.getCurrency();
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
    }

    public boolean canTransactionBeModified(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        return transaction.isPending();
    }

    public boolean canTransactionBeCancelled(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        return transaction.isPending();
    }
}
