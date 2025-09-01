package com.cashly.cashly_api.expenses.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseIdUnitTest {

    @Test
    void should_CreateExpenseId_When_ValidUUIDProvided() {
        // Arrange
        UUID validUuid = UUID.randomUUID();
        
        // Act
        ExpenseId expenseId = new ExpenseId(validUuid);
        
        // Assert
        assertNotNull(expenseId);
        assertEquals(validUuid, expenseId.getValue());
    }

    @Test
    void should_ThrowException_When_NullUUIDProvided() {
        // Arrange
        UUID nullUuid = null;
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ExpenseId(nullUuid)
        );
        
        assertEquals("Expense ID cannot be null", exception.getMessage());
    }

    @Test
    void should_BeEqual_When_SameUUIDValue() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        ExpenseId expenseId1 = new ExpenseId(uuid);
        ExpenseId expenseId2 = new ExpenseId(uuid);
        
        // Act & Assert
        assertEquals(expenseId1, expenseId2);
        assertEquals(expenseId1.hashCode(), expenseId2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentUUIDValues() {
        // Arrange
        ExpenseId expenseId1 = new ExpenseId(UUID.randomUUID());
        ExpenseId expenseId2 = new ExpenseId(UUID.randomUUID());
        
        // Act & Assert
        assertNotEquals(expenseId1, expenseId2);
        assertNotEquals(expenseId1.hashCode(), expenseId2.hashCode());
    }

    @Test
    void should_GenerateRandomExpenseId_When_UsingFactoryMethod() {
        // Act
        ExpenseId expenseId1 = ExpenseId.generate();
        ExpenseId expenseId2 = ExpenseId.generate();
        
        // Assert
        assertNotNull(expenseId1);
        assertNotNull(expenseId2);
        assertNotEquals(expenseId1, expenseId2);
    }
}