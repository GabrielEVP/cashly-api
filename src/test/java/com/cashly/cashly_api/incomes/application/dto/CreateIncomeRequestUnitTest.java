package com.cashly.cashly_api.incomes.application.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CreateIncomeRequestUnitTest {
    
    @Test
    public void should_CreateInstance_When_ValidParametersProvided() {
        // Arrange
        BigDecimal amount = new BigDecimal("1000.00");
        String description = "Salary payment";
        String category = "SALARY";
        String userId = "user123";
        
        // Act
        CreateIncomeRequest request = new CreateIncomeRequest(amount, description, category, userId);
        
        // Assert
        assertNotNull(request);
        assertEquals(amount, request.getAmount());
        assertEquals(description, request.getDescription());
        assertEquals(category, request.getCategory());
        assertEquals(userId, request.getUserId());
    }
    
    @Test
    public void should_AllowNullParameters_When_ValidationNotApplied() {
        // Note: Jakarta validation is only applied when validator is used
        // Constructor itself doesn't validate - validation happens at service layer
        
        // Arrange & Act
        CreateIncomeRequest request1 = new CreateIncomeRequest(null, "description", "SALARY", "user123");
        CreateIncomeRequest request2 = new CreateIncomeRequest(new BigDecimal("100"), null, "SALARY", "user123");
        CreateIncomeRequest request3 = new CreateIncomeRequest(new BigDecimal("100"), "description", null, "user123");
        CreateIncomeRequest request4 = new CreateIncomeRequest(new BigDecimal("100"), "description", "SALARY", null);
        
        // Assert
        assertNotNull(request1);
        assertNotNull(request2);
        assertNotNull(request3);
        assertNotNull(request4);
    }
    
    @Test
    public void should_HaveCorrectGetters_When_InstanceCreated() {
        // Arrange
        BigDecimal amount = new BigDecimal("500.50");
        String description = "Freelance payment";
        String category = "BUSINESS";
        String userId = "user456";
        
        // Act
        CreateIncomeRequest request = new CreateIncomeRequest(amount, description, category, userId);
        
        // Assert
        assertEquals(amount, request.getAmount());
        assertEquals(description, request.getDescription());
        assertEquals(category, request.getCategory());
        assertEquals(userId, request.getUserId());
    }
    
    @Test
    public void should_ImplementEqualsAndHashCode_When_ComparingInstances() {
        // Arrange
        BigDecimal amount = new BigDecimal("1000.00");
        String description = "Salary payment";
        String category = "SALARY";
        String userId = "user123";
        
        CreateIncomeRequest request1 = new CreateIncomeRequest(amount, description, category, userId);
        CreateIncomeRequest request2 = new CreateIncomeRequest(amount, description, category, userId);
        CreateIncomeRequest request3 = new CreateIncomeRequest(new BigDecimal("2000.00"), description, category, userId);
        
        // Act & Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
    
    @Test
    public void should_ImplementToString_When_InstanceCreated() {
        // Arrange
        BigDecimal amount = new BigDecimal("1000.00");
        String description = "Salary payment";
        String category = "SALARY";
        String userId = "user123";
        
        CreateIncomeRequest request = new CreateIncomeRequest(amount, description, category, userId);
        
        // Act
        String toString = request.toString();
        
        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("CreateIncomeRequest"));
        assertTrue(toString.contains(amount.toString()));
        assertTrue(toString.contains(description));
        assertTrue(toString.contains(category));
        assertTrue(toString.contains(userId));
    }
}