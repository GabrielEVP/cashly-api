package com.cashly.cashly_api.accounts.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BalanceUnitTest {

    @Test
    void should_CreateBalance_When_ValidPositiveAmountProvided() {
        // Arrange
        BigDecimal amount = new BigDecimal("100.50");

        // Act
        Balance balance = new Balance(amount);

        // Assert
        assertNotNull(balance);
        assertEquals(0, amount.compareTo(balance.getValue()));
    }

    @Test
    void should_CreateBalance_When_NegativeAmountProvided() {
        // Arrange
        BigDecimal amount = new BigDecimal("-50.00");

        // Act
        Balance balance = new Balance(amount);

        // Assert
        assertNotNull(balance);
        assertEquals(0, amount.compareTo(balance.getValue()));
    }

    @Test
    void should_CreateBalance_When_ZeroAmountProvided() {
        // Arrange
        BigDecimal amount = BigDecimal.ZERO;

        // Act
        Balance balance = new Balance(amount);

        // Assert
        assertNotNull(balance);
        assertEquals(0, amount.compareTo(balance.getValue()));
    }

    @Test
    void should_ThrowException_When_NullAmountProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Balance(null)
        );
        assertEquals("Balance cannot be null", exception.getMessage());
    }

    @Test
    void should_ReturnNewBalanceInstance_When_GetValueCalled() {
        // Arrange
        Balance balance = new Balance(new BigDecimal("100.00"));

        // Act
        BigDecimal value1 = balance.getValue();
        BigDecimal value2 = balance.getValue();

        // Assert
        assertEquals(value1, value2);
        assertNotSame(value1, value2); // Defensive copy
    }

    @Test
    void should_AddBalances_When_ValidBalanceProvided() {
        // Arrange
        Balance balance1 = new Balance(new BigDecimal("100.00"));
        Balance balance2 = new Balance(new BigDecimal("50.00"));

        // Act
        Balance result = balance1.add(balance2);

        // Assert
        assertEquals(0, new BigDecimal("150.00").compareTo(result.getValue()));
    }

    @Test
    void should_AddNegativeBalance_When_AddingNegativeAmount() {
        // Arrange
        Balance balance1 = new Balance(new BigDecimal("100.00"));
        Balance balance2 = new Balance(new BigDecimal("-30.00"));

        // Act
        Balance result = balance1.add(balance2);

        // Assert
        assertEquals(0, new BigDecimal("70.00").compareTo(result.getValue()));
    }

    @Test
    void should_ThrowException_When_AddingNullBalance() {
        // Arrange
        Balance balance = new Balance(new BigDecimal("100.00"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> balance.add(null)
        );
        assertEquals("Other balance cannot be null", exception.getMessage());
    }

    @Test
    void should_SubtractBalances_When_ValidBalanceProvided() {
        // Arrange
        Balance balance1 = new Balance(new BigDecimal("100.00"));
        Balance balance2 = new Balance(new BigDecimal("30.00"));

        // Act
        Balance result = balance1.subtract(balance2);

        // Assert
        assertEquals(0, new BigDecimal("70.00").compareTo(result.getValue()));
    }

    @Test
    void should_AllowNegativeResult_When_SubtractingLargerAmount() {
        // Arrange
        Balance balance1 = new Balance(new BigDecimal("50.00"));
        Balance balance2 = new Balance(new BigDecimal("100.00"));

        // Act
        Balance result = balance1.subtract(balance2);

        // Assert
        assertEquals(0, new BigDecimal("-50.00").compareTo(result.getValue()));
    }

    @Test
    void should_ThrowException_When_SubtractingNullBalance() {
        // Arrange
        Balance balance = new Balance(new BigDecimal("100.00"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> balance.subtract(null)
        );
        assertEquals("Other balance cannot be null", exception.getMessage());
    }

    @Test
    void should_ReturnTrue_When_BalanceIsNegative() {
        // Arrange
        Balance balance = new Balance(new BigDecimal("-10.00"));

        // Act & Assert
        assertTrue(balance.isNegative());
        assertFalse(balance.isPositive());
        assertFalse(balance.isZero());
    }

    @Test
    void should_ReturnTrue_When_BalanceIsPositive() {
        // Arrange
        Balance balance = new Balance(new BigDecimal("10.00"));

        // Act & Assert
        assertTrue(balance.isPositive());
        assertFalse(balance.isNegative());
        assertFalse(balance.isZero());
    }

    @Test
    void should_ReturnTrue_When_BalanceIsZero() {
        // Arrange
        Balance balance = new Balance(BigDecimal.ZERO);

        // Act & Assert
        assertTrue(balance.isZero());
        assertFalse(balance.isPositive());
        assertFalse(balance.isNegative());
    }

    @Test
    void should_ReturnTrue_When_BalanceIsGreaterThanOther() {
        // Arrange
        Balance balance1 = new Balance(new BigDecimal("100.00"));
        Balance balance2 = new Balance(new BigDecimal("50.00"));

        // Act & Assert
        assertTrue(balance1.isGreaterThan(balance2));
        assertFalse(balance2.isGreaterThan(balance1));
    }

    @Test
    void should_ThrowException_When_ComparingWithNullBalance() {
        // Arrange
        Balance balance = new Balance(new BigDecimal("100.00"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> balance.isGreaterThan(null)
        );
        assertEquals("Other balance cannot be null", exception.getMessage());
    }

    @Test
    void should_BeEqual_When_SameBalanceValue() {
        // Arrange
        Balance balance1 = new Balance(new BigDecimal("100.00"));
        Balance balance2 = new Balance(new BigDecimal("100.00"));

        // Act & Assert
        assertEquals(balance1, balance2);
        assertEquals(balance1.hashCode(), balance2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentBalanceValue() {
        // Arrange
        Balance balance1 = new Balance(new BigDecimal("100.00"));
        Balance balance2 = new Balance(new BigDecimal("50.00"));

        // Act & Assert
        assertNotEquals(balance1, balance2);
    }

    @Test
    void should_ReturnStringRepresentation_When_ToStringCalled() {
        // Arrange
        Balance balance = new Balance(new BigDecimal("100.50"));

        // Act
        String result = balance.toString();

        // Assert
        assertTrue(result.contains("Balance"));
        assertTrue(result.contains("100.5"));
    }
}
