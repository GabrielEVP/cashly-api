package com.cashly.cashly_api.incomes.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IncomeIdUnitTest {

    @Test
    void should_CreateIncomeId_When_ValidUUIDProvided() {
        // Arrange
        UUID validUuid = UUID.randomUUID();
        
        // Act
        IncomeId incomeId = new IncomeId(validUuid);
        
        // Assert
        assertNotNull(incomeId);
        assertEquals(validUuid, incomeId.getValue());
    }

    @Test
    void should_ThrowException_When_NullUUIDProvided() {
        // Arrange
        UUID nullUuid = null;
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new IncomeId(nullUuid)
        );
        
        assertEquals("Income ID cannot be null", exception.getMessage());
    }

    @Test
    void should_BeEqual_When_SameUUIDValue() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        IncomeId incomeId1 = new IncomeId(uuid);
        IncomeId incomeId2 = new IncomeId(uuid);
        
        // Act & Assert
        assertEquals(incomeId1, incomeId2);
        assertEquals(incomeId1.hashCode(), incomeId2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentUUIDValues() {
        // Arrange
        IncomeId incomeId1 = new IncomeId(UUID.randomUUID());
        IncomeId incomeId2 = new IncomeId(UUID.randomUUID());
        
        // Act & Assert
        assertNotEquals(incomeId1, incomeId2);
        assertNotEquals(incomeId1.hashCode(), incomeId2.hashCode());
    }

    @Test
    void should_GenerateRandomIncomeId_When_UsingFactoryMethod() {
        // Act
        IncomeId incomeId1 = IncomeId.generate();
        IncomeId incomeId2 = IncomeId.generate();
        
        // Assert
        assertNotNull(incomeId1);
        assertNotNull(incomeId2);
        assertNotEquals(incomeId1, incomeId2);
    }
}