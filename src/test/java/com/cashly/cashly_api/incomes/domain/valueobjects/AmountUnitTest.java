package com.cashly.cashly_api.incomes.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AmountUnitTest {

    @Test
    void should_CreateAmount_When_ValidPositiveValueProvided() {
        // Arrange
        BigDecimal validAmount = new BigDecimal("100.50");
        
        // Act
        Amount amount = new Amount(validAmount);
        
        // Assert
        assertNotNull(amount);
        assertEquals(validAmount, amount.getValue());
    }

    @Test
    void should_CreateAmountWithZero_When_ZeroValueProvided() {
        // Arrange
        BigDecimal zeroAmount = BigDecimal.ZERO;
        
        // Act
        Amount amount = new Amount(zeroAmount);
        
        // Assert
        assertNotNull(amount);
        assertEquals(zeroAmount, amount.getValue());
    }

    @Test
    void should_ThrowException_When_NullValueProvided() {
        // Arrange
        BigDecimal nullAmount = null;
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Amount(nullAmount)
        );
        
        assertEquals("Amount cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NegativeValueProvided() {
        // Arrange
        BigDecimal negativeAmount = new BigDecimal("-10.00");
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Amount(negativeAmount)
        );
        
        assertEquals("Amount cannot be negative", exception.getMessage());
    }

    @Test
    void should_BeEqual_When_SameAmountValue() {
        // Arrange
        BigDecimal value = new BigDecimal("50.25");
        Amount amount1 = new Amount(value);
        Amount amount2 = new Amount(value);
        
        // Act & Assert
        assertEquals(amount1, amount2);
        assertEquals(amount1.hashCode(), amount2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentAmountValues() {
        // Arrange
        Amount amount1 = new Amount(new BigDecimal("50.25"));
        Amount amount2 = new Amount(new BigDecimal("75.00"));
        
        // Act & Assert
        assertNotEquals(amount1, amount2);
    }

    @Test
    void should_AddAmounts_When_ValidAmountsProvided() {
        // Arrange
        Amount amount1 = new Amount(new BigDecimal("50.25"));
        Amount amount2 = new Amount(new BigDecimal("25.75"));
        
        // Act
        Amount result = amount1.add(amount2);
        
        // Assert
        assertEquals(new BigDecimal("76.00"), result.getValue());
    }

    @Test
    void should_SubtractAmounts_When_ValidAmountsProvided() {
        // Arrange
        Amount amount1 = new Amount(new BigDecimal("100.00"));
        Amount amount2 = new Amount(new BigDecimal("30.50"));
        
        // Act
        Amount result = amount1.subtract(amount2);
        
        // Assert
        assertEquals(new BigDecimal("69.50"), result.getValue());
    }

    @Test
    void should_ThrowException_When_SubtractionResultsInNegative() {
        // Arrange
        Amount amount1 = new Amount(new BigDecimal("30.00"));
        Amount amount2 = new Amount(new BigDecimal("50.00"));
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> amount1.subtract(amount2)
        );
        
        assertEquals("Subtraction result cannot be negative", exception.getMessage());
    }

    @Test
    void should_ReturnTrue_When_AmountIsGreaterThanOther() {
        // Arrange
        Amount amount1 = new Amount(new BigDecimal("100.00"));
        Amount amount2 = new Amount(new BigDecimal("50.00"));
        
        // Act & Assert
        assertTrue(amount1.isGreaterThan(amount2));
        assertFalse(amount2.isGreaterThan(amount1));
    }

    @Test
    void should_ReturnFalse_When_AmountsAreEqual() {
        // Arrange
        Amount amount1 = new Amount(new BigDecimal("50.00"));
        Amount amount2 = new Amount(new BigDecimal("50.00"));
        
        // Act & Assert
        assertFalse(amount1.isGreaterThan(amount2));
        assertFalse(amount2.isGreaterThan(amount1));
    }
}