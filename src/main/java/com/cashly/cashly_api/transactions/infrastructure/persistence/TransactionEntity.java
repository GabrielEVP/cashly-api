package com.cashly.cashly_api.transactions.infrastructure.persistence;

import com.cashly.cashly_api.transactions.domain.entities.Transaction;
import com.cashly.cashly_api.transactions.domain.valueobjects.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_source_account_id", columnList = "source_account_id"),
    @Index(name = "idx_destination_account_id", columnList = "destination_account_id"),
    @Index(name = "idx_transaction_status", columnList = "transaction_status"),
    @Index(name = "idx_transaction_date", columnList = "transaction_date")
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class TransactionEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @Column(name = "transaction_type", length = 20, nullable = false)
    private String transactionType;

    @Column(name = "transaction_status", length = 20, nullable = false)
    private String transactionStatus;

    @Column(name = "amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "source_account_id", length = 36)
    private String sourceAccountId;

    @Column(name = "destination_account_id", length = 36)
    private String destinationAccountId;

    @Column(name = "expense_id", length = 36)
    private String expenseId;

    @Column(name = "income_id", length = 36)
    private String incomeId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static TransactionEntity fromDomain(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }

        TransactionEntity entity = new TransactionEntity();
        entity.id = transaction.getId().getValue().toString();
        entity.userId = transaction.getUserId();
        entity.transactionType = transaction.getType().name();
        entity.transactionStatus = transaction.getStatus().name();
        entity.amount = transaction.getAmount().getValue();
        entity.currency = transaction.getCurrency();
        entity.description = transaction.getDescription().getValue();
        entity.transactionDate = transaction.getTransactionDate().getValue();
        entity.sourceAccountId = transaction.getSourceAccountId();
        entity.destinationAccountId = transaction.getDestinationAccountId();
        entity.expenseId = transaction.getExpenseId();
        entity.incomeId = transaction.getIncomeId();
        entity.createdAt = transaction.getCreatedAt();
        entity.updatedAt = transaction.getUpdatedAt();

        return entity;
    }

    public void updateFromDomain(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }

        this.transactionStatus = transaction.getStatus().name();
        this.description = transaction.getDescription().getValue();
        this.updatedAt = transaction.getUpdatedAt();
    }

    public Transaction toDomain() {
        TransactionId transactionId = new TransactionId(UUID.fromString(this.id));
        TransactionType type = TransactionType.fromString(this.transactionType);
        TransactionStatus status = TransactionStatus.fromString(this.transactionStatus);
        Amount domainAmount = new Amount(this.amount);
        Description domainDescription = new Description(this.description);
        TransactionDate domainDate = new TransactionDate(this.transactionDate);

        Transaction transaction = new Transaction(
            transactionId,
            this.userId,
            type,
            status,
            domainAmount,
            this.currency,
            domainDescription,
            domainDate,
            this.sourceAccountId,
            this.destinationAccountId,
            this.expenseId,
            this.incomeId
        );

        try {
            java.lang.reflect.Field createdAtField = Transaction.class.getDeclaredField("createdAt");
            java.lang.reflect.Field updatedAtField = Transaction.class.getDeclaredField("updatedAt");

            createdAtField.setAccessible(true);
            updatedAtField.setAccessible(true);

            createdAtField.set(transaction, this.createdAt);
            updatedAtField.set(transaction, this.updatedAt);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set timestamps on domain entity", e);
        }

        return transaction;
    }
}
