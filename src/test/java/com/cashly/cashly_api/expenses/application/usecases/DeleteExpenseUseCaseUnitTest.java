package com.cashly.cashly_api.expenses.application.usecases;

import com.cashly.cashly_api.expenses.application.ports.ExpenseRepository;
import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeleteExpenseUseCaseUnitTest {
    
    @Mock
    private ExpenseRepository expenseRepository;
    
    private DeleteExpenseUseCase deleteExpenseUseCase;
    private String validExpenseId;
    private Expense existingExpense;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteExpenseUseCase = new DeleteExpenseUseCase(expenseRepository);
        
        validExpenseId = UUID.randomUUID().toString();
        existingExpense = new Expense(
            new ExpenseId(UUID.fromString(validExpenseId)),
            new Amount(new BigDecimal("500.00")),
            new Description("Test expense"),
            new Category("FOOD_DINING"),
            LocalDate.now(),
            "user123"
        );
    }
    
    @Test
    void should_DeleteExpense_When_ValidExpenseIdProvided() {
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        doNothing().when(expenseRepository).deleteById(any(ExpenseId.class));
        
        assertDoesNotThrow(() -> deleteExpenseUseCase.execute(validExpenseId));
        
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).deleteById(any(ExpenseId.class));
    }
    
    @Test
    void should_ThrowException_When_ExpenseIdIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> deleteExpenseUseCase.execute(null)
        );
        
        assertEquals("Expense ID cannot be null", exception.getMessage());
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
        verify(expenseRepository, never()).deleteById(any(ExpenseId.class));
    }
    
    @Test
    void should_ThrowException_When_ExpenseIdIsInvalidUUID() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> deleteExpenseUseCase.execute("invalid-uuid")
        );
        
        assertTrue(exception.getMessage().contains("Invalid UUID format for expense ID"));
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
        verify(expenseRepository, never()).deleteById(any(ExpenseId.class));
    }
    
    @Test
    void should_ThrowException_When_ExpenseNotFound() {
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.empty());
        
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> deleteExpenseUseCase.execute(validExpenseId)
        );
        
        assertTrue(exception.getMessage().contains("Expense not found with ID"));
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, never()).deleteById(any(ExpenseId.class));
    }
    
    @Test
    void should_HandleRepositoryException_When_FindByIdFails() {
        when(expenseRepository.findById(any(ExpenseId.class)))
            .thenThrow(new RuntimeException("Database connection error"));
        
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> deleteExpenseUseCase.execute(validExpenseId)
        );
        
        assertEquals("Database connection error", exception.getMessage());
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, never()).deleteById(any(ExpenseId.class));
    }
    
    @Test
    void should_HandleRepositoryException_When_DeleteFails() {
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        doThrow(new RuntimeException("Delete operation failed"))
            .when(expenseRepository).deleteById(any(ExpenseId.class));
        
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> deleteExpenseUseCase.execute(validExpenseId)
        );
        
        assertEquals("Delete operation failed", exception.getMessage());
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).deleteById(any(ExpenseId.class));
    }
    
    @Test
    void should_ParseValidUUID_When_ValidUUIDStringProvided() {
        String uuidString = "12345678-1234-1234-1234-123456789012";
        Expense expenseWithSpecificId = new Expense(
            new ExpenseId(UUID.fromString(uuidString)),
            new Amount(new BigDecimal("300.00")),
            new Description("Specific expense"),
            new Category("OTHER"),
            LocalDate.now(),
            "user456"
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(expenseWithSpecificId));
        doNothing().when(expenseRepository).deleteById(any(ExpenseId.class));
        
        assertDoesNotThrow(() -> deleteExpenseUseCase.execute(uuidString));
        
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).deleteById(any(ExpenseId.class));
    }
    
    @Test
    void should_ThrowException_When_UUIDFormatIsIncorrect() {
        String incorrectUUID = "not-a-valid-uuid-format";
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> deleteExpenseUseCase.execute(incorrectUUID)
        );
        
        assertTrue(exception.getMessage().contains("Invalid UUID format for expense ID"));
        assertTrue(exception.getMessage().contains(incorrectUUID));
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
        verify(expenseRepository, never()).deleteById(any(ExpenseId.class));
    }
    
    @Test
    void should_ThrowException_When_UUIDHasExtraCharacters() {
        String malformedUUID = "12345678-1234-1234-1234-123456789012-extra";
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> deleteExpenseUseCase.execute(malformedUUID)
        );
        
        assertTrue(exception.getMessage().contains("Invalid UUID format for expense ID"));
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
        verify(expenseRepository, never()).deleteById(any(ExpenseId.class));
    }
    
    @Test
    void should_ThrowException_When_UUIDIsTooShort() {
        String shortUUID = "12345678-1234-1234";
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> deleteExpenseUseCase.execute(shortUUID)
        );
        
        assertTrue(exception.getMessage().contains("Invalid UUID format for expense ID"));
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
        verify(expenseRepository, never()).deleteById(any(ExpenseId.class));
    }
    
    @Test
    void should_ThrowException_When_EmptyStringProvided() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> deleteExpenseUseCase.execute("")
        );
        
        assertTrue(exception.getMessage().contains("Invalid UUID format for expense ID"));
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
        verify(expenseRepository, never()).deleteById(any(ExpenseId.class));
    }
    
    @Test
    void should_DeleteExpenseSuccessfully_When_ExpenseExistsAndRepositoryWorksCorrectly() {
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        doNothing().when(expenseRepository).deleteById(any(ExpenseId.class));
        
        deleteExpenseUseCase.execute(validExpenseId);
        
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).deleteById(any(ExpenseId.class));
    }
    
    @Test
    void should_CallRepositoryMethodsInCorrectOrder_When_DeletingExpense() {
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        doNothing().when(expenseRepository).deleteById(any(ExpenseId.class));
        
        deleteExpenseUseCase.execute(validExpenseId);
        
        var inOrder = inOrder(expenseRepository);
        inOrder.verify(expenseRepository).findById(any(ExpenseId.class));
        inOrder.verify(expenseRepository).deleteById(any(ExpenseId.class));
    }
    
    @Test
    void should_PassCorrectExpenseId_When_CallingRepositoryMethods() {
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        doNothing().when(expenseRepository).deleteById(any(ExpenseId.class));
        
        deleteExpenseUseCase.execute(validExpenseId);
        
        ExpenseId expectedExpenseId = new ExpenseId(UUID.fromString(validExpenseId));
        verify(expenseRepository, times(1)).findById(eq(expectedExpenseId));
        verify(expenseRepository, times(1)).deleteById(eq(expectedExpenseId));
    }
}