package com.cashly.cashly_api.incomes.application.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateIncomeRequestUnitTest {
    
    @Test
    public void should_CreateInstance_When_ValidParametersProvided() {
        // Arrange
        String description = "Updated salary payment";
        String category = "SALARY";
        
        // Act
        UpdateIncomeRequest request = new UpdateIncomeRequest(description, category);
        
        // Assert
        assertNotNull(request);
        assertEquals(description, request.getDescription());
        assertEquals(category, request.getCategory());
    }
    
    @Test
    public void should_AllowNullParameters_When_PartialUpdateNeeded() {
        // Arrange & Act
        UpdateIncomeRequest request1 = new UpdateIncomeRequest(null, "SALARY");
        UpdateIncomeRequest request2 = new UpdateIncomeRequest("description", null);
        UpdateIncomeRequest request3 = new UpdateIncomeRequest(null, null);
        
        // Assert
        assertNotNull(request1);
        assertNull(request1.getDescription());
        assertEquals("SALARY", request1.getCategory());
        
        assertNotNull(request2);
        assertEquals("description", request2.getDescription());
        assertNull(request2.getCategory());
        
        assertNotNull(request3);
        assertNull(request3.getDescription());
        assertNull(request3.getCategory());
    }
    
    @Test
    public void should_ImplementEqualsAndHashCode_When_ComparingInstances() {
        // Arrange
        String description = "Updated salary payment";
        String category = "SALARY";
        
        UpdateIncomeRequest request1 = new UpdateIncomeRequest(description, category);
        UpdateIncomeRequest request2 = new UpdateIncomeRequest(description, category);
        UpdateIncomeRequest request3 = new UpdateIncomeRequest("Different description", category);
        
        // Act & Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
    
    @Test
    public void should_ImplementToString_When_InstanceCreated() {
        // Arrange
        String description = "Updated salary payment";
        String category = "SALARY";
        
        UpdateIncomeRequest request = new UpdateIncomeRequest(description, category);
        
        // Act
        String toString = request.toString();
        
        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("UpdateIncomeRequest"));
        assertTrue(toString.contains(description));
        assertTrue(toString.contains(category));
    }
}