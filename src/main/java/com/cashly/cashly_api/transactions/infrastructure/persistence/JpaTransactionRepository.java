package com.cashly.cashly_api.transactions.infrastructure.persistence;

import com.cashly.cashly_api.transactions.application.ports.TransactionRepository;
import com.cashly.cashly_api.transactions.domain.entities.Transaction;
import com.cashly.cashly_api.transactions.domain.valueobjects.TransactionId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JpaTransactionRepository implements TransactionRepository {

    private final SpringDataTransactionRepository springDataRepository;

    public JpaTransactionRepository(SpringDataTransactionRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }

        String idString = transaction.getId().getValue().toString();
        Optional<TransactionEntity> existingEntity = springDataRepository.findById(idString);

        TransactionEntity entity;
        if (existingEntity.isPresent()) {
            entity = existingEntity.get();
            entity.updateFromDomain(transaction);
        } else {
            entity = TransactionEntity.fromDomain(transaction);
        }

        TransactionEntity savedEntity = springDataRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Transaction> findById(TransactionId id) {
        if (id == null) {
            throw new IllegalArgumentException("Transaction ID cannot be null");
        }

        return springDataRepository.findById(id.getValue().toString())
            .map(TransactionEntity::toDomain);
    }

    @Override
    public List<Transaction> findByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        return springDataRepository.findByUserId(userId).stream()
            .map(TransactionEntity::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByAccountId(String accountId) {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }

        return springDataRepository.findByAccountId(accountId).stream()
            .map(TransactionEntity::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(TransactionId id) {
        if (id == null) {
            throw new IllegalArgumentException("Transaction ID cannot be null");
        }

        springDataRepository.deleteById(id.getValue().toString());
    }

    @Override
    public boolean existsById(TransactionId id) {
        if (id == null) {
            throw new IllegalArgumentException("Transaction ID cannot be null");
        }

        return springDataRepository.existsById(id.getValue().toString());
    }
}
