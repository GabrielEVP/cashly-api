package com.cashly.cashly_api.transactions.domain.entities;

import com.cashly.cashly_api.transactions.domain.valueobjects.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionUnitTest {

    @Test
    void should_CreateTransaction_When_ValidTransferParametersProvided() {
        // Arrange
        TransactionId id = TransactionId.generate();
        String userId = "user123";
        TransactionType type = TransactionType.TRANSFER;
        TransactionStatus status = TransactionStatus.PENDING;
        Amount amount = new Amount(new BigDecimal("100.00"));
        String currency = "USD";
        Description description = new Description("Transfer to savings");
        TransactionDate date = TransactionDate.now();
        String sourceAccountId = "source123";
        String destinationAccountId = "dest123";

        // Act
        Transaction transaction = new Transaction(id, userId, type, status, amount, currency,
            description, date, sourceAccountId, destinationAccountId, null, null);

        // Assert
        assertNotNull(transaction);
        assertEquals(id, transaction.getId());
        assertEquals(userId, transaction.getUserId());
        assertEquals(type, transaction.getType());
        assertEquals(status, transaction.getStatus());
        assertEquals(amount, transaction.getAmount());
        assertEquals("USD", transaction.getCurrency());
    }

    @Test
    void should_CreateTransaction_When_ValidDepositParametersProvided() {
        // Arrange
        TransactionId id = TransactionId.generate();
        Amount amount = new Amount(new BigDecimal("500.00"));

        // Act
        Transaction transaction = new Transaction(id, "user123", TransactionType.DEPOSIT,
            TransactionStatus.PENDING, amount, "USD", new Description("Salary deposit"),
            TransactionDate.now(), null, "dest123", null, null);

        // Assert
        assertNotNull(transaction);
        assertNull(transaction.getSourceAccountId());
        assertNotNull(transaction.getDestinationAccountId());
    }

    @Test
    void should_CreateTransaction_When_ValidWithdrawalParametersProvided() {
        // Arrange
        TransactionId id = TransactionId.generate();
        Amount amount = new Amount(new BigDecimal("200.00"));

        // Act
        Transaction transaction = new Transaction(id, "user123", TransactionType.WITHDRAWAL,
            TransactionStatus.PENDING, amount, "USD", new Description("ATM withdrawal"),
            TransactionDate.now(), "source123", null, null, null);

        // Assert
        assertNotNull(transaction);
        assertNotNull(transaction.getSourceAccountId());
        assertNull(transaction.getDestinationAccountId());
    }

    @Test
    void should_CreateTransaction_When_ValidPaymentParametersProvided() {
        // Arrange
        TransactionId id = TransactionId.generate();
        Amount amount = new Amount(new BigDecimal("75.50"));

        // Act
        Transaction transaction = new Transaction(id, "user123", TransactionType.PAYMENT,
            TransactionStatus.PENDING, amount, "USD", new Description("Online purchase"),
            TransactionDate.now(), "source123", null, "expense123", null);

        // Assert
        assertNotNull(transaction);
        assertNotNull(transaction.getExpenseId());
        assertNull(transaction.getIncomeId());
    }

    @Test
    void should_CreateTransaction_When_ValidRefundParametersProvided() {
        // Arrange
        TransactionId id = TransactionId.generate();
        Amount amount = new Amount(new BigDecimal("50.00"));

        // Act
        Transaction transaction = new Transaction(id, "user123", TransactionType.REFUND,
            TransactionStatus.PENDING, amount, "USD", new Description("Product refund"),
            TransactionDate.now(), null, "dest123", null, "income123");

        // Assert
        assertNotNull(transaction);
        assertNotNull(transaction.getIncomeId());
        assertNull(transaction.getExpenseId());
    }

    @Test
    void should_ThrowException_When_NullIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Transaction(null, "user123", TransactionType.DEPOSIT,
                TransactionStatus.PENDING, new Amount(new BigDecimal("100")), "USD",
                new Description("Test"), TransactionDate.now(), null, "dest123", null, null)
        );
        assertEquals("Transaction ID cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullUserIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Transaction(TransactionId.generate(), null, TransactionType.DEPOSIT,
                TransactionStatus.PENDING, new Amount(new BigDecimal("100")), "USD",
                new Description("Test"), TransactionDate.now(), null, "dest123", null, null)
        );
        assertEquals("User ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_EmptyUserIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Transaction(TransactionId.generate(), "  ", TransactionType.DEPOSIT,
                TransactionStatus.PENDING, new Amount(new BigDecimal("100")), "USD",
                new Description("Test"), TransactionDate.now(), null, "dest123", null, null)
        );
        assertEquals("User ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_TransferMissingSourceAccount() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Transaction(TransactionId.generate(), "user123", TransactionType.TRANSFER,
                TransactionStatus.PENDING, new Amount(new BigDecimal("100")), "USD",
                new Description("Test"), TransactionDate.now(), null, "dest123", null, null)
        );
        assertTrue(exception.getMessage().contains("TRANSFER requires a source account"));
    }

    @Test
    void should_ThrowException_When_TransferMissingDestinationAccount() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Transaction(TransactionId.generate(), "user123", TransactionType.TRANSFER,
                TransactionStatus.PENDING, new Amount(new BigDecimal("100")), "USD",
                new Description("Test"), TransactionDate.now(), "source123", null, null, null)
        );
        assertTrue(exception.getMessage().contains("TRANSFER requires a destination account"));
    }

    @Test
    void should_ThrowException_When_DepositMissingDestinationAccount() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Transaction(TransactionId.generate(), "user123", TransactionType.DEPOSIT,
                TransactionStatus.PENDING, new Amount(new BigDecimal("100")), "USD",
                new Description("Test"), TransactionDate.now(), null, null, null, null)
        );
        assertTrue(exception.getMessage().contains("DEPOSIT requires a destination account"));
    }

    @Test
    void should_ThrowException_When_WithdrawalMissingSourceAccount() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Transaction(TransactionId.generate(), "user123", TransactionType.WITHDRAWAL,
                TransactionStatus.PENDING, new Amount(new BigDecimal("100")), "USD",
                new Description("Test"), TransactionDate.now(), null, null, null, null)
        );
        assertTrue(exception.getMessage().contains("WITHDRAWAL requires a source account"));
    }

    @Test
    void should_ThrowException_When_SameAccountAsSourceAndDestination() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Transaction(TransactionId.generate(), "user123", TransactionType.TRANSFER,
                TransactionStatus.PENDING, new Amount(new BigDecimal("100")), "USD",
                new Description("Test"), TransactionDate.now(), "account123", "account123", null, null)
        );
        assertEquals("Source and destination accounts cannot be the same", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_LinkingExpenseToNonPaymentType() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Transaction(TransactionId.generate(), "user123", TransactionType.DEPOSIT,
                TransactionStatus.PENDING, new Amount(new BigDecimal("100")), "USD",
                new Description("Test"), TransactionDate.now(), null, "dest123", "expense123", null)
        );
        assertTrue(exception.getMessage().contains("cannot be linked to an expense"));
    }

    @Test
    void should_ThrowException_When_LinkingIncomeToNonRefundType() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Transaction(TransactionId.generate(), "user123", TransactionType.WITHDRAWAL,
                TransactionStatus.PENDING, new Amount(new BigDecimal("100")), "USD",
                new Description("Test"), TransactionDate.now(), "source123", null, null, "income123")
        );
        assertTrue(exception.getMessage().contains("cannot be linked to an income"));
    }

    @Test
    void should_UpdateStatus_When_ValidTransitionProvided() {
        // Arrange
        Transaction transaction = createValidTransaction(TransactionStatus.PENDING);

        // Act
        transaction.updateStatus(TransactionStatus.COMPLETED);

        // Assert
        assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
    }

    @Test
    void should_ThrowException_When_InvalidStatusTransition() {
        // Arrange
        Transaction transaction = createValidTransaction(TransactionStatus.COMPLETED);

        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> transaction.updateStatus(TransactionStatus.PENDING)
        );
        assertTrue(exception.getMessage().contains("Cannot transition"));
    }

    @Test
    void should_CompleteTransaction_When_CompleteCalled() {
        // Arrange
        Transaction transaction = createValidTransaction(TransactionStatus.PENDING);

        // Act
        transaction.complete();

        // Assert
        assertTrue(transaction.isCompleted());
    }

    @Test
    void should_CancelTransaction_When_CancelCalled() {
        // Arrange
        Transaction transaction = createValidTransaction(TransactionStatus.PENDING);

        // Act
        transaction.cancel();

        // Assert
        assertTrue(transaction.isCancelled());
    }

    @Test
    void should_MarkAsFailed_When_MarkAsFailedCalled() {
        // Arrange
        Transaction transaction = createValidTransaction(TransactionStatus.PENDING);

        // Act
        transaction.markAsFailed();

        // Assert
        assertTrue(transaction.isFailed());
    }

    @Test
    void should_UpdateDescription_When_TransactionIsPending() {
        // Arrange
        Transaction transaction = createValidTransaction(TransactionStatus.PENDING);
        Description newDescription = new Description("Updated description");

        // Act
        transaction.updateDescription(newDescription);

        // Assert
        assertEquals(newDescription, transaction.getDescription());
    }

    @Test
    void should_ThrowException_When_UpdatingDescriptionOnCompletedTransaction() {
        // Arrange
        Transaction transaction = createValidTransaction(TransactionStatus.COMPLETED);
        Description newDescription = new Description("Updated description");

        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> transaction.updateDescription(newDescription)
        );
        assertTrue(exception.getMessage().contains("Cannot update description of a completed transaction"));
    }

    @Test
    void should_ReturnTrue_When_TransactionBelongsToUser() {
        // Arrange
        Transaction transaction = createValidTransaction(TransactionStatus.PENDING);

        // Act & Assert
        assertTrue(transaction.belongsToUser("user123"));
        assertFalse(transaction.belongsToUser("otherUser"));
    }

    @Test
    void should_ReturnTrue_When_TransactionInvolvesAccount() {
        // Arrange
        Transaction transaction = new Transaction(TransactionId.generate(), "user123",
            TransactionType.TRANSFER, TransactionStatus.PENDING,
            new Amount(new BigDecimal("100")), "USD", new Description("Test"),
            TransactionDate.now(), "source123", "dest123", null, null);

        // Act & Assert
        assertTrue(transaction.involvesAccount("source123"));
        assertTrue(transaction.involvesAccount("dest123"));
        assertFalse(transaction.involvesAccount("other123"));
    }

    @Test
    void should_ReturnTrue_When_TransactionIsPending() {
        // Arrange
        Transaction transaction = createValidTransaction(TransactionStatus.PENDING);

        // Act & Assert
        assertTrue(transaction.isPending());
        assertFalse(transaction.isCompleted());
        assertFalse(transaction.isFailed());
        assertFalse(transaction.isCancelled());
    }

    @Test
    void should_NormalizeCurrency_When_LowercaseProvided() {
        // Arrange & Act
        Transaction transaction = new Transaction(TransactionId.generate(), "user123",
            TransactionType.DEPOSIT, TransactionStatus.PENDING,
            new Amount(new BigDecimal("100")), "usd", new Description("Test"),
            TransactionDate.now(), null, "dest123", null, null);

        // Assert
        assertEquals("USD", transaction.getCurrency());
    }

    @Test
    void should_TrimUserId_When_WhitespaceProvided() {
        // Arrange & Act
        Transaction transaction = new Transaction(TransactionId.generate(), "  user123  ",
            TransactionType.DEPOSIT, TransactionStatus.PENDING,
            new Amount(new BigDecimal("100")), "USD", new Description("Test"),
            TransactionDate.now(), null, "dest123", null, null);

        // Assert
        assertEquals("user123", transaction.getUserId());
    }

    @Test
    void should_BeEqual_When_SameIdUsed() {
        // Arrange
        TransactionId id = TransactionId.generate();
        Transaction transaction1 = new Transaction(id, "user123", TransactionType.DEPOSIT,
            TransactionStatus.PENDING, new Amount(new BigDecimal("100")), "USD",
            new Description("Test"), TransactionDate.now(), null, "dest123", null, null);
        Transaction transaction2 = new Transaction(id, "user123", TransactionType.DEPOSIT,
            TransactionStatus.PENDING, new Amount(new BigDecimal("100")), "USD",
            new Description("Test"), TransactionDate.now(), null, "dest123", null, null);

        // Act & Assert
        assertEquals(transaction1, transaction2);
        assertEquals(transaction1.hashCode(), transaction2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentIdUsed() {
        // Arrange
        Transaction transaction1 = createValidTransaction(TransactionStatus.PENDING);
        Transaction transaction2 = createValidTransaction(TransactionStatus.PENDING);

        // Act & Assert
        assertNotEquals(transaction1, transaction2);
    }

    @Test
    void should_ReturnStringRepresentation_When_ToStringCalled() {
        // Arrange
        Transaction transaction = createValidTransaction(TransactionStatus.PENDING);

        // Act
        String result = transaction.toString();

        // Assert
        assertTrue(result.contains("Transaction"));
        assertTrue(result.contains("user123"));
        assertTrue(result.contains("DEPOSIT"));
    }

    private Transaction createValidTransaction(TransactionStatus status) {
        return new Transaction(
            TransactionId.generate(),
            "user123",
            TransactionType.DEPOSIT,
            status,
            new Amount(new BigDecimal("100.00")),
            "USD",
            new Description("Test transaction"),
            TransactionDate.now(),
            null,
            "dest123",
            null,
            null
        );
    }
}
