package com.cashly.cashly_api.incomes.application.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class IncomeResponseUnitTest {
    
    @Test
    public void should_CreateInstance_When_ValidParametersProvided() {
        // Arrange
        String id = "123e4567-e89b-12d3-a456-426614174000";
        BigDecimal amount = new BigDecimal("1000.00");
        String description = "Salary payment";
        String category = "SALARY";
        String userId = "user123";
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2023, 1, 2, 12, 0);
        
        // Act
        IncomeResponse response = new IncomeResponse(id, amount, description, category, userId, createdAt, updatedAt);
        
        // Assert
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(amount, response.getAmount());
        assertEquals(description, response.getDescription());
        assertEquals(category, response.getCategory());
        assertEquals(userId, response.getUserId());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
    }
    
    @Test
    public void should_HaveCorrectGetters_When_InstanceCreated() {
        // Arrange
        String id = "456e7890-e89b-12d3-a456-426614174000";
        BigDecimal amount = new BigDecimal("500.50");
        String description = "Freelance payment";
        String category = "BUSINESS";
        String userId = "user456";
        LocalDateTime createdAt = LocalDateTime.of(2023, 2, 1, 10, 30);
        LocalDateTime updatedAt = LocalDateTime.of(2023, 2, 1, 10, 30);
        
        // Act
        IncomeResponse response = new IncomeResponse(id, amount, description, category, userId, createdAt, updatedAt);
        
        // Assert
        assertEquals(id, response.getId());
        assertEquals(amount, response.getAmount());
        assertEquals(description, response.getDescription());
        assertEquals(category, response.getCategory());
        assertEquals(userId, response.getUserId());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
    }
    
    @Test
    public void should_ImplementEqualsAndHashCode_When_ComparingInstances() {
        // Arrange
        String id = "123e4567-e89b-12d3-a456-426614174000";
        BigDecimal amount = new BigDecimal("1000.00");
        String description = "Salary payment";
        String category = "SALARY";
        String userId = "user123";
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2023, 1, 2, 12, 0);
        
        IncomeResponse response1 = new IncomeResponse(id, amount, description, category, userId, createdAt, updatedAt);
        IncomeResponse response2 = new IncomeResponse(id, amount, description, category, userId, createdAt, updatedAt);
        IncomeResponse response3 = new IncomeResponse("different-id", amount, description, category, userId, createdAt, updatedAt);
        
        // Act & Assert
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }
    
    @Test
    public void should_ImplementToString_When_InstanceCreated() {
        // Arrange
        String id = "123e4567-e89b-12d3-a456-426614174000";
        BigDecimal amount = new BigDecimal("1000.00");
        String description = "Salary payment";
        String category = "SALARY";
        String userId = "user123";
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2023, 1, 2, 12, 0);
        
        IncomeResponse response = new IncomeResponse(id, amount, description, category, userId, createdAt, updatedAt);
        
        // Act
        String toString = response.toString();
        
        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("IncomeResponse"));
        assertTrue(toString.contains(id));
        assertTrue(toString.contains(amount.toString()));
        assertTrue(toString.contains(description));
        assertTrue(toString.contains(category));
        assertTrue(toString.contains(userId));
    }
}