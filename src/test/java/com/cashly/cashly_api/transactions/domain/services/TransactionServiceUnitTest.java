package com.cashly.cashly_api.transactions.domain.services;

import com.cashly.cashly_api.transactions.domain.entities.Transaction;
import com.cashly.cashly_api.transactions.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceUnitTest {

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService();
    }

    @Test
    void should_ValidateSuccessfully_When_ValidTransferProvided() {
        // Arrange
        Transaction transaction = createTransaction(TransactionType.TRANSFER, "source123", "dest123", null, null);

        // Act & Assert
        assertDoesNotThrow(() -> transactionService.validateTransactionIntegrity(transaction));
    }

    @Test
    void should_ValidateSuccessfully_When_ValidDepositProvided() {
        // Arrange
        Transaction transaction = createTransaction(TransactionType.DEPOSIT, null, "dest123", null, null);

        // Act & Assert
        assertDoesNotThrow(() -> transactionService.validateTransactionIntegrity(transaction));
    }

    @Test
    void should_ValidateSuccessfully_When_ValidWithdrawalProvided() {
        // Arrange
        Transaction transaction = createTransaction(TransactionType.WITHDRAWAL, "source123", null, null, null);

        // Act & Assert
        assertDoesNotThrow(() -> transactionService.validateTransactionIntegrity(transaction));
    }

    @Test
    void should_ValidateSuccessfully_When_ValidPaymentProvided() {
        // Arrange
        Transaction transaction = createTransaction(TransactionType.PAYMENT, "source123", null, "expense123", null);

        // Act & Assert
        assertDoesNotThrow(() -> transactionService.validateTransactionIntegrity(transaction));
    }

    @Test
    void should_ValidateSuccessfully_When_ValidRefundProvided() {
        // Arrange
        Transaction transaction = createTransaction(TransactionType.REFUND, null, "dest123", null, "income123");

        // Act & Assert
        assertDoesNotThrow(() -> transactionService.validateTransactionIntegrity(transaction));
    }

    @Test
    void should_ThrowException_When_NullTransactionProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> transactionService.validateTransactionIntegrity(null)
        );
        assertEquals("Transaction cannot be null", exception.getMessage());
    }

    @Test
    void should_ValidateTransactionIntegritySuccessfully_When_ValidTransferProvided() {
        // Arrange
        Transaction transaction = createTransaction(TransactionType.TRANSFER, "source123", "dest123", null, null);

        // Act & Assert
        assertDoesNotThrow(() -> transactionService.validateTransactionIntegrity(transaction));
    }

    @Test
    void should_ReturnTrue_When_PendingTransactionCanBeModified() {
        // Arrange
        Transaction transaction = createTransaction(TransactionType.DEPOSIT, null, "dest123", null, null);

        // Act
        boolean canModify = transactionService.canTransactionBeModified(transaction);

        // Assert
        assertTrue(canModify);
    }

    @Test
    void should_ReturnFalse_When_CompletedTransactionCannotBeModified() {
        // Arrange
        Transaction transaction = createTransaction(TransactionType.DEPOSIT, null, "dest123", null, null);
        transaction.complete();

        // Act
        boolean canModify = transactionService.canTransactionBeModified(transaction);

        // Assert
        assertFalse(canModify);
    }

    @Test
    void should_ReturnTrue_When_PendingTransactionCanBeCancelled() {
        // Arrange
        Transaction transaction = createTransaction(TransactionType.DEPOSIT, null, "dest123", null, null);

        // Act
        boolean canCancel = transactionService.canTransactionBeCancelled(transaction);

        // Assert
        assertTrue(canCancel);
    }

    @Test
    void should_ReturnFalse_When_CompletedTransactionCannotBeCancelled() {
        // Arrange
        Transaction transaction = createTransaction(TransactionType.DEPOSIT, null, "dest123", null, null);
        transaction.complete();

        // Act
        boolean canCancel = transactionService.canTransactionBeCancelled(transaction);

        // Assert
        assertFalse(canCancel);
    }

    @Test
    void should_ReturnFalse_When_NullTransactionProvidedToCanBeModified() {
        // Act
        boolean canModify = transactionService.canTransactionBeModified(null);

        // Assert
        assertFalse(canModify);
    }

    @Test
    void should_ReturnFalse_When_NullTransactionProvidedToCanBeCancelled() {
        // Act
        boolean canCancel = transactionService.canTransactionBeCancelled(null);

        // Assert
        assertFalse(canCancel);
    }

    private Transaction createTransaction(TransactionType type, String sourceAccountId,
                                         String destinationAccountId, String expenseId, String incomeId) {
        return new Transaction(
            TransactionId.generate(),
            "user123",
            type,
            TransactionStatus.PENDING,
            new Amount(new BigDecimal("100.00")),
            "USD",
            new Description("Test transaction"),
            TransactionDate.now(),
            sourceAccountId,
            destinationAccountId,
            expenseId,
            incomeId
        );
    }
}
