package com.cashly.cashly_api.transactions.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataTransactionRepository extends JpaRepository<TransactionEntity, String> {

    List<TransactionEntity> findByUserId(String userId);

    @Query("SELECT t FROM TransactionEntity t WHERE t.sourceAccountId = :accountId OR t.destinationAccountId = :accountId")
    List<TransactionEntity> findByAccountId(@Param("accountId") String accountId);
}
