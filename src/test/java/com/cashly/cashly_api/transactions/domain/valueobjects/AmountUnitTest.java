package com.cashly.cashly_api.transactions.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AmountUnitTest {

    @Test
    void should_CreateAmount_When_PositiveValueProvided() {
        // Arrange
        BigDecimal value = new BigDecimal("100.50");

        // Act
        Amount amount = new Amount(value);

        // Assert
        assertNotNull(amount);
        assertEquals(value, amount.getValue());
    }

    @Test
    void should_ThrowException_When_NullValueProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Amount(null)
        );
        assertEquals("Amount cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_ZeroValueProvided() {
        // Arrange
        BigDecimal zero = BigDecimal.ZERO;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Amount(zero)
        );
        assertEquals("Amount must be positive", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NegativeValueProvided() {
        // Arrange
        BigDecimal negative = new BigDecimal("-10.00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Amount(negative)
        );
        assertEquals("Amount must be positive", exception.getMessage());
    }

    @Test
    void should_AddAmounts_When_TwoAmountsProvided() {
        // Arrange
        Amount amount1 = new Amount(new BigDecimal("100.00"));
        Amount amount2 = new Amount(new BigDecimal("50.00"));

        // Act
        Amount result = amount1.add(amount2);

        // Assert
        assertEquals(new BigDecimal("150.00"), result.getValue());
    }

    @Test
    void should_ThrowException_When_AddingNullAmount() {
        // Arrange
        Amount amount = new Amount(new BigDecimal("100.00"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> amount.add(null)
        );
        assertEquals("Other amount cannot be null", exception.getMessage());
    }

    @Test
    void should_SubtractAmounts_When_ResultIsPositive() {
        // Arrange
        Amount amount1 = new Amount(new BigDecimal("100.00"));
        Amount amount2 = new Amount(new BigDecimal("50.00"));

        // Act
        Amount result = amount1.subtract(amount2);

        // Assert
        assertEquals(new BigDecimal("50.00"), result.getValue());
    }

    @Test
    void should_ThrowException_When_SubtractionResultIsZero() {
        // Arrange
        Amount amount1 = new Amount(new BigDecimal("100.00"));
        Amount amount2 = new Amount(new BigDecimal("100.00"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> amount1.subtract(amount2)
        );
        assertEquals("Subtraction result must be positive", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_SubtractionResultIsNegative() {
        // Arrange
        Amount amount1 = new Amount(new BigDecimal("50.00"));
        Amount amount2 = new Amount(new BigDecimal("100.00"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> amount1.subtract(amount2)
        );
        assertEquals("Subtraction result must be positive", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_SubtractingNullAmount() {
        // Arrange
        Amount amount = new Amount(new BigDecimal("100.00"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> amount.subtract(null)
        );
        assertEquals("Other amount cannot be null", exception.getMessage());
    }

    @Test
    void should_ReturnTrue_When_AmountIsGreaterThan() {
        // Arrange
        Amount greater = new Amount(new BigDecimal("100.00"));
        Amount lesser = new Amount(new BigDecimal("50.00"));

        // Act & Assert
        assertTrue(greater.isGreaterThan(lesser));
        assertFalse(lesser.isGreaterThan(greater));
    }

    @Test
    void should_ReturnFalse_When_AmountsAreEqual() {
        // Arrange
        Amount amount1 = new Amount(new BigDecimal("100.00"));
        Amount amount2 = new Amount(new BigDecimal("100.00"));

        // Act & Assert
        assertFalse(amount1.isGreaterThan(amount2));
    }

    @Test
    void should_ThrowException_When_ComparingWithNullInIsGreaterThan() {
        // Arrange
        Amount amount = new Amount(new BigDecimal("100.00"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> amount.isGreaterThan(null)
        );
        assertEquals("Other amount cannot be null", exception.getMessage());
    }

    @Test
    void should_BeEqual_When_SameValueUsed() {
        // Arrange
        BigDecimal value = new BigDecimal("100.00");
        Amount amount1 = new Amount(value);
        Amount amount2 = new Amount(value);

        // Act & Assert
        assertEquals(amount1, amount2);
        assertEquals(amount1.hashCode(), amount2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentValueUsed() {
        // Arrange
        Amount amount1 = new Amount(new BigDecimal("100.00"));
        Amount amount2 = new Amount(new BigDecimal("50.00"));

        // Act & Assert
        assertNotEquals(amount1, amount2);
    }

    @Test
    void should_ReturnImmutableCopy_When_GetValueCalled() {
        // Arrange
        BigDecimal original = new BigDecimal("100.00");
        Amount amount = new Amount(original);

        // Act
        BigDecimal retrieved = amount.getValue();
        retrieved = retrieved.add(new BigDecimal("50.00"));

        // Assert
        assertEquals(new BigDecimal("100.00"), amount.getValue());
    }

    @Test
    void should_ReturnStringRepresentation_When_ToStringCalled() {
        // Arrange
        BigDecimal value = new BigDecimal("100.00");
        Amount amount = new Amount(value);

        // Act
        String result = amount.toString();

        // Assert
        assertTrue(result.contains("Amount"));
        assertTrue(result.contains("100.00"));
    }
}
