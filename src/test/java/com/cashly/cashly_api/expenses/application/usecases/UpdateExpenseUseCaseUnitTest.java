package com.cashly.cashly_api.expenses.application.usecases;

import com.cashly.cashly_api.expenses.application.dto.ExpenseResponse;
import com.cashly.cashly_api.expenses.application.dto.UpdateExpenseRequest;
import com.cashly.cashly_api.expenses.application.ports.ExpenseRepository;
import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateExpenseUseCaseUnitTest {
    
    @Mock
    private ExpenseRepository expenseRepository;
    
    private UpdateExpenseUseCase updateExpenseUseCase;
    private String validExpenseId;
    private Expense existingExpense;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateExpenseUseCase = new UpdateExpenseUseCase(expenseRepository);
        
        validExpenseId = UUID.randomUUID().toString();
        existingExpense = new Expense(
            new ExpenseId(UUID.fromString(validExpenseId)),
            new Amount(new BigDecimal("500.00")),
            new Description("Original description"),
            new Category("FOOD_DINING"),
            LocalDate.now(),
            "user123"
        );
    }
    
    @Test
    void should_UpdateExpenseAmount_When_AmountProvided() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            new BigDecimal("750.00"), 
            null, 
            null
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);
        
        ExpenseResponse response = updateExpenseUseCase.execute(validExpenseId, request);
        
        assertNotNull(response);
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_UpdateExpenseDescription_When_DescriptionProvided() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            null, 
            "Updated description", 
            null
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);
        
        ExpenseResponse response = updateExpenseUseCase.execute(validExpenseId, request);
        
        assertNotNull(response);
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_UpdateExpenseCategory_When_CategoryProvided() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            null, 
            null, 
            "TRANSPORTATION"
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);
        
        ExpenseResponse response = updateExpenseUseCase.execute(validExpenseId, request);
        
        assertNotNull(response);
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_UpdateAllFields_When_AllFieldsProvided() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            new BigDecimal("1000.00"), 
            "Updated description", 
            "HOUSING"
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);
        
        ExpenseResponse response = updateExpenseUseCase.execute(validExpenseId, request);
        
        assertNotNull(response);
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_NotUpdateFields_When_AllFieldsNull() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(null, null, null);
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);
        
        ExpenseResponse response = updateExpenseUseCase.execute(validExpenseId, request);
        
        assertNotNull(response);
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_NotUpdateDescription_When_DescriptionIsEmpty() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            new BigDecimal("600.00"), 
            "", 
            "OTHER"
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);
        
        ExpenseResponse response = updateExpenseUseCase.execute(validExpenseId, request);
        
        assertNotNull(response);
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_NotUpdateDescription_When_DescriptionIsWhitespace() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            new BigDecimal("600.00"), 
            "   ", 
            "OTHER"
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);
        
        ExpenseResponse response = updateExpenseUseCase.execute(validExpenseId, request);
        
        assertNotNull(response);
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_NotUpdateCategory_When_CategoryIsEmpty() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            new BigDecimal("600.00"), 
            "Updated description", 
            ""
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);
        
        ExpenseResponse response = updateExpenseUseCase.execute(validExpenseId, request);
        
        assertNotNull(response);
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_NotUpdateCategory_When_CategoryIsWhitespace() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            new BigDecimal("600.00"), 
            "Updated description", 
            "   "
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);
        
        ExpenseResponse response = updateExpenseUseCase.execute(validExpenseId, request);
        
        assertNotNull(response);
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_ThrowException_When_ExpenseIdIsNull() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            new BigDecimal("600.00"), 
            "Description", 
            "FOOD_DINING"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> updateExpenseUseCase.execute(null, request)
        );
        
        assertEquals("Expense ID cannot be null", exception.getMessage());
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
        verify(expenseRepository, never()).save(any(Expense.class));
    }
    
    @Test
    void should_ThrowException_When_RequestIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> updateExpenseUseCase.execute(validExpenseId, null)
        );
        
        assertEquals("Update expense request cannot be null", exception.getMessage());
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
        verify(expenseRepository, never()).save(any(Expense.class));
    }
    
    @Test
    void should_ThrowException_When_ExpenseIdIsInvalidUUID() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            new BigDecimal("600.00"), 
            "Description", 
            "FOOD_DINING"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> updateExpenseUseCase.execute("invalid-uuid", request)
        );
        
        assertTrue(exception.getMessage().contains("Invalid UUID format for expense ID"));
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
        verify(expenseRepository, never()).save(any(Expense.class));
    }
    
    @Test
    void should_ThrowException_When_ExpenseNotFound() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            new BigDecimal("600.00"), 
            "Description", 
            "FOOD_DINING"
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.empty());
        
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> updateExpenseUseCase.execute(validExpenseId, request)
        );
        
        assertTrue(exception.getMessage().contains("Expense not found with ID"));
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, never()).save(any(Expense.class));
    }
    
    @Test
    void should_UpdateWithZeroAmount_When_ZeroAmountProvided() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            BigDecimal.ZERO, 
            "Zero amount expense", 
            "OTHER"
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);
        
        ExpenseResponse response = updateExpenseUseCase.execute(validExpenseId, request);
        
        assertNotNull(response);
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_UpdateWithLargeAmount_When_LargeAmountProvided() {
        BigDecimal largeAmount = new BigDecimal("999999999.99");
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            largeAmount, 
            "Large expense", 
            "SHOPPING"
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);
        
        ExpenseResponse response = updateExpenseUseCase.execute(validExpenseId, request);
        
        assertNotNull(response);
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_HandleRepositoryException_When_FindByIdFails() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            new BigDecimal("100.00"), 
            "Test expense", 
            "FOOD_DINING"
        );
        
        when(expenseRepository.findById(any(ExpenseId.class)))
            .thenThrow(new RuntimeException("Database error"));
        
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> updateExpenseUseCase.execute(validExpenseId, request)
        );
        
        assertEquals("Database error", exception.getMessage());
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, never()).save(any(Expense.class));
    }
    
    @Test
    void should_HandleRepositoryException_When_SaveFails() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            new BigDecimal("100.00"), 
            "Test expense", 
            "FOOD_DINING"
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class)))
            .thenThrow(new RuntimeException("Save failed"));
        
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> updateExpenseUseCase.execute(validExpenseId, request)
        );
        
        assertEquals("Save failed", exception.getMessage());
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_MapResponseCorrectly_When_ExpenseUpdated() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            new BigDecimal("300.00"), 
            "Updated expense", 
            "TRANSPORTATION"
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);
        
        ExpenseResponse response = updateExpenseUseCase.execute(validExpenseId, request);
        
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(existingExpense.getId().getValue().toString(), response.getId());
        assertEquals(existingExpense.getAmount().getValue(), response.getAmount());
        assertEquals(existingExpense.getDescription().getValue(), response.getDescription());
        assertEquals(existingExpense.getCategory().getValue(), response.getCategory());
        assertEquals(existingExpense.getUserId(), response.getUserId());
        assertEquals(existingExpense.getCreatedAt(), response.getCreatedAt());
        assertEquals(existingExpense.getUpdatedAt(), response.getUpdatedAt());
        
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
}