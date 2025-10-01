package com.cashly.cashly_api.transactions.application.ports;

import com.cashly.cashly_api.transactions.domain.entities.Transaction;
import com.cashly.cashly_api.transactions.domain.valueobjects.TransactionId;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(TransactionId id);
    List<Transaction> findByUserId(String userId);
    List<Transaction> findByAccountId(String accountId);
    void deleteById(TransactionId id);
    boolean existsById(TransactionId id);
}
