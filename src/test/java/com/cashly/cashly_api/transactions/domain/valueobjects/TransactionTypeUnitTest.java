package com.cashly.cashly_api.transactions.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTypeUnitTest {

    @Test
    void should_RequireSourceAccount_When_TransferType() {
        // Arrange
        TransactionType type = TransactionType.TRANSFER;

        // Act & Assert
        assertTrue(type.requiresSourceAccount());
    }

    @Test
    void should_RequireDestinationAccount_When_TransferType() {
        // Arrange
        TransactionType type = TransactionType.TRANSFER;

        // Act & Assert
        assertTrue(type.requiresDestinationAccount());
    }

    @Test
    void should_RequireOnlyDestinationAccount_When_DepositType() {
        // Arrange
        TransactionType type = TransactionType.DEPOSIT;

        // Act & Assert
        assertFalse(type.requiresSourceAccount());
        assertTrue(type.requiresDestinationAccount());
    }

    @Test
    void should_RequireOnlySourceAccount_When_WithdrawalType() {
        // Arrange
        TransactionType type = TransactionType.WITHDRAWAL;

        // Act & Assert
        assertTrue(type.requiresSourceAccount());
        assertFalse(type.requiresDestinationAccount());
    }

    @Test
    void should_RequireOnlySourceAccount_When_PaymentType() {
        // Arrange
        TransactionType type = TransactionType.PAYMENT;

        // Act & Assert
        assertTrue(type.requiresSourceAccount());
        assertFalse(type.requiresDestinationAccount());
    }

    @Test
    void should_RequireOnlyDestinationAccount_When_RefundType() {
        // Arrange
        TransactionType type = TransactionType.REFUND;

        // Act & Assert
        assertFalse(type.requiresSourceAccount());
        assertTrue(type.requiresDestinationAccount());
    }

    @Test
    void should_AllowExpenseLinkage_When_PaymentType() {
        // Arrange
        TransactionType type = TransactionType.PAYMENT;

        // Act & Assert
        assertTrue(type.canLinkToExpense());
        assertFalse(type.canLinkToIncome());
    }

    @Test
    void should_AllowIncomeLinkage_When_RefundType() {
        // Arrange
        TransactionType type = TransactionType.REFUND;

        // Act & Assert
        assertFalse(type.canLinkToExpense());
        assertTrue(type.canLinkToIncome());
    }

    @Test
    void should_NotAllowLinkage_When_TransferType() {
        // Arrange
        TransactionType type = TransactionType.TRANSFER;

        // Act & Assert
        assertFalse(type.canLinkToExpense());
        assertFalse(type.canLinkToIncome());
    }

    @Test
    void should_CreateTypeFromString_When_ValidStringProvided() {
        // Act
        TransactionType transfer = TransactionType.fromString("TRANSFER");
        TransactionType deposit = TransactionType.fromString("deposit");
        TransactionType withdrawal = TransactionType.fromString("  WITHDRAWAL  ");

        // Assert
        assertEquals(TransactionType.TRANSFER, transfer);
        assertEquals(TransactionType.DEPOSIT, deposit);
        assertEquals(TransactionType.WITHDRAWAL, withdrawal);
    }

    @Test
    void should_ThrowException_When_NullStringProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TransactionType.fromString(null)
        );
        assertEquals("Transaction type cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_InvalidStringProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TransactionType.fromString("INVALID")
        );
        assertTrue(exception.getMessage().contains("Invalid transaction type"));
        assertTrue(exception.getMessage().contains("TRANSFER"));
    }
}
