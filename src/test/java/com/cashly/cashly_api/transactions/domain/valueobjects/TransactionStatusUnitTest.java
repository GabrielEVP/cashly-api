package com.cashly.cashly_api.transactions.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionStatusUnitTest {

    @Test
    void should_AllowTransitionToCompleted_When_StatusIsPending() {
        // Arrange
        TransactionStatus status = TransactionStatus.PENDING;

        // Act & Assert
        assertTrue(status.canTransitionTo(TransactionStatus.COMPLETED));
    }

    @Test
    void should_AllowTransitionToFailed_When_StatusIsPending() {
        // Arrange
        TransactionStatus status = TransactionStatus.PENDING;

        // Act & Assert
        assertTrue(status.canTransitionTo(TransactionStatus.FAILED));
    }

    @Test
    void should_AllowTransitionToCancelled_When_StatusIsPending() {
        // Arrange
        TransactionStatus status = TransactionStatus.PENDING;

        // Act & Assert
        assertTrue(status.canTransitionTo(TransactionStatus.CANCELLED));
    }

    @Test
    void should_NotAllowTransition_When_StatusIsCompleted() {
        // Arrange
        TransactionStatus status = TransactionStatus.COMPLETED;

        // Act & Assert
        assertFalse(status.canTransitionTo(TransactionStatus.PENDING));
        assertFalse(status.canTransitionTo(TransactionStatus.FAILED));
        assertFalse(status.canTransitionTo(TransactionStatus.CANCELLED));
    }

    @Test
    void should_NotAllowTransition_When_StatusIsFailed() {
        // Arrange
        TransactionStatus status = TransactionStatus.FAILED;

        // Act & Assert
        assertFalse(status.canTransitionTo(TransactionStatus.PENDING));
        assertFalse(status.canTransitionTo(TransactionStatus.COMPLETED));
        assertFalse(status.canTransitionTo(TransactionStatus.CANCELLED));
    }

    @Test
    void should_NotAllowTransition_When_StatusIsCancelled() {
        // Arrange
        TransactionStatus status = TransactionStatus.CANCELLED;

        // Act & Assert
        assertFalse(status.canTransitionTo(TransactionStatus.PENDING));
        assertFalse(status.canTransitionTo(TransactionStatus.COMPLETED));
        assertFalse(status.canTransitionTo(TransactionStatus.FAILED));
    }

    @Test
    void should_ReturnFalse_When_PendingStatusIsFinal() {
        // Arrange
        TransactionStatus status = TransactionStatus.PENDING;

        // Act & Assert
        assertFalse(status.isFinal());
    }

    @Test
    void should_ReturnTrue_When_CompletedStatusIsFinal() {
        // Arrange
        TransactionStatus status = TransactionStatus.COMPLETED;

        // Act & Assert
        assertTrue(status.isFinal());
    }

    @Test
    void should_ReturnTrue_When_FailedStatusIsFinal() {
        // Arrange
        TransactionStatus status = TransactionStatus.FAILED;

        // Act & Assert
        assertTrue(status.isFinal());
    }

    @Test
    void should_ReturnTrue_When_CancelledStatusIsFinal() {
        // Arrange
        TransactionStatus status = TransactionStatus.CANCELLED;

        // Act & Assert
        assertTrue(status.isFinal());
    }

    @Test
    void should_CreateStatusFromString_When_ValidStringProvided() {
        // Act
        TransactionStatus pending = TransactionStatus.fromString("PENDING");
        TransactionStatus completed = TransactionStatus.fromString("completed");
        TransactionStatus failed = TransactionStatus.fromString("  FAILED  ");

        // Assert
        assertEquals(TransactionStatus.PENDING, pending);
        assertEquals(TransactionStatus.COMPLETED, completed);
        assertEquals(TransactionStatus.FAILED, failed);
    }

    @Test
    void should_ThrowException_When_NullStringProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TransactionStatus.fromString(null)
        );
        assertEquals("Transaction status cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_InvalidStringProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TransactionStatus.fromString("INVALID")
        );
        assertTrue(exception.getMessage().contains("Invalid transaction status"));
        assertTrue(exception.getMessage().contains("PENDING"));
    }
}
