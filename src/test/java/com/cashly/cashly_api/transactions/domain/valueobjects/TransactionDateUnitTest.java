package com.cashly.cashly_api.transactions.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDateUnitTest {

    @Test
    void should_CreateTransactionDate_When_ValidDateProvided() {
        // Arrange
        LocalDate date = LocalDate.now();

        // Act
        TransactionDate transactionDate = new TransactionDate(date);

        // Assert
        assertNotNull(transactionDate);
        assertEquals(date, transactionDate.getValue());
    }

    @Test
    void should_CreateTransactionDate_When_PastDateProvided() {
        // Arrange
        LocalDate pastDate = LocalDate.now().minusDays(10);

        // Act
        TransactionDate transactionDate = new TransactionDate(pastDate);

        // Assert
        assertNotNull(transactionDate);
        assertEquals(pastDate, transactionDate.getValue());
    }

    @Test
    void should_ThrowException_When_NullDateProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new TransactionDate(null)
        );
        assertEquals("Transaction date cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_FutureDateProvided() {
        // Arrange
        LocalDate futureDate = LocalDate.now().plusDays(1);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new TransactionDate(futureDate)
        );
        assertEquals("Transaction date cannot be in the future", exception.getMessage());
    }

    @Test
    void should_CreateCurrentDate_When_NowCalled() {
        // Act
        TransactionDate transactionDate = TransactionDate.now();

        // Assert
        assertNotNull(transactionDate);
        assertEquals(LocalDate.now(), transactionDate.getValue());
    }

    @Test
    void should_ReturnTrue_When_DateIsBefore() {
        // Arrange
        TransactionDate earlier = new TransactionDate(LocalDate.now().minusDays(5));
        TransactionDate later = new TransactionDate(LocalDate.now());

        // Act & Assert
        assertTrue(earlier.isBefore(later));
        assertFalse(later.isBefore(earlier));
    }

    @Test
    void should_ReturnTrue_When_DateIsAfter() {
        // Arrange
        TransactionDate earlier = new TransactionDate(LocalDate.now().minusDays(5));
        TransactionDate later = new TransactionDate(LocalDate.now());

        // Act & Assert
        assertTrue(later.isAfter(earlier));
        assertFalse(earlier.isAfter(later));
    }

    @Test
    void should_ThrowException_When_ComparingWithNullInIsBefore() {
        // Arrange
        TransactionDate date = TransactionDate.now();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> date.isBefore(null)
        );
        assertEquals("Other transaction date cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_ComparingWithNullInIsAfter() {
        // Arrange
        TransactionDate date = TransactionDate.now();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> date.isAfter(null)
        );
        assertEquals("Other transaction date cannot be null", exception.getMessage());
    }

    @Test
    void should_BeEqual_When_SameDateUsed() {
        // Arrange
        LocalDate date = LocalDate.now();
        TransactionDate date1 = new TransactionDate(date);
        TransactionDate date2 = new TransactionDate(date);

        // Act & Assert
        assertEquals(date1, date2);
        assertEquals(date1.hashCode(), date2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentDateUsed() {
        // Arrange
        TransactionDate date1 = new TransactionDate(LocalDate.now().minusDays(1));
        TransactionDate date2 = new TransactionDate(LocalDate.now());

        // Act & Assert
        assertNotEquals(date1, date2);
    }

    @Test
    void should_ReturnTrueForEquals_When_SameInstance() {
        // Arrange
        TransactionDate date = TransactionDate.now();

        // Act & Assert
        assertEquals(date, date);
    }

    @Test
    void should_ReturnFalseForEquals_When_NullProvided() {
        // Arrange
        TransactionDate date = TransactionDate.now();

        // Act & Assert
        assertNotEquals(null, date);
    }

    @Test
    void should_ReturnFalseForEquals_When_DifferentClassProvided() {
        // Arrange
        TransactionDate date = TransactionDate.now();
        String other = "not a date";

        // Act & Assert
        assertNotEquals(date, other);
    }

    @Test
    void should_ReturnStringRepresentation_When_ToStringCalled() {
        // Arrange
        LocalDate date = LocalDate.now();
        TransactionDate transactionDate = new TransactionDate(date);

        // Act
        String result = transactionDate.toString();

        // Assert
        assertTrue(result.contains("TransactionDate"));
        assertTrue(result.contains(date.toString()));
    }
}
