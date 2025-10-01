package com.cashly.cashly_api.expenses.application.usecases;

import com.cashly.cashly_api.expenses.application.dto.CreateExpenseRequest;
import com.cashly.cashly_api.expenses.application.dto.ExpenseResponse;
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
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateExpenseUseCaseUnitTest {
    
    @Mock
    private ExpenseRepository expenseRepository;
    
    private CreateExpenseUseCase createExpenseUseCase;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createExpenseUseCase = new CreateExpenseUseCase(expenseRepository);
    }
    
    @Test
    void should_CreateExpense_When_ValidRequestProvided() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            new BigDecimal("500.00"), 
            "Grocery shopping", 
            "FOOD_DINING", 
            "user123"
        );
        
        Expense savedExpense = new Expense(
            ExpenseId.generate(),
            new Amount(new BigDecimal("500.00")),
            new Description("Grocery shopping"),
            new Category("FOOD_DINING"),
            LocalDate.now(),
            "user123"
        );
        
        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);
        
        ExpenseResponse response = createExpenseUseCase.execute(request);
        
        assertNotNull(response);
        assertEquals(new BigDecimal("500.00"), response.getAmount());
        assertEquals("Grocery shopping", response.getDescription());
        assertEquals("FOOD_DINING", response.getCategory());
        assertEquals("user123", response.getUserId());
        
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_SaveExpenseWithCorrectValues_When_ValidRequestProvided() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            new BigDecimal("750.00"), 
            "Gas bill", 
            "OTHER", 
            "user456"
        );
        
        ArgumentCaptor<Expense> expenseCaptor = ArgumentCaptor.forClass(Expense.class);
        
        Expense savedExpense = new Expense(
            ExpenseId.generate(),
            new Amount(new BigDecimal("750.00")),
            new Description("Gas bill"),
            new Category("OTHER"),
            LocalDate.now(),
            "user456"
        );
        
        when(expenseRepository.save(expenseCaptor.capture())).thenReturn(savedExpense);
        
        createExpenseUseCase.execute(request);
        
        Expense capturedExpense = expenseCaptor.getValue();
        assertNotNull(capturedExpense.getId());
        assertEquals(new BigDecimal("750.00"), capturedExpense.getAmount().getValue());
        assertEquals("Gas bill", capturedExpense.getDescription().getValue());
        assertEquals("OTHER", capturedExpense.getCategory().getValue());
        assertEquals("user456", capturedExpense.getUserId());
        assertEquals(LocalDate.now(), capturedExpense.getDate());
    }
    
    @Test
    void should_ThrowException_When_RequestIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> createExpenseUseCase.execute(null)
        );
        
        assertEquals("Create expense request cannot be null", exception.getMessage());
        verify(expenseRepository, never()).save(any(Expense.class));
    }
    
    @Test
    void should_ThrowException_When_AmountIsNull() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            null, 
            "Description", 
            "FOOD_DINING", 
            "user123"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> createExpenseUseCase.execute(request)
        );
        
        assertEquals("Amount cannot be null", exception.getMessage());
        verify(expenseRepository, never()).save(any(Expense.class));
    }
    
    @Test
    void should_ThrowException_When_DescriptionIsNull() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            new BigDecimal("100.00"), 
            null, 
            "FOOD_DINING", 
            "user123"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> createExpenseUseCase.execute(request)
        );
        
        assertEquals("Description cannot be null", exception.getMessage());
        verify(expenseRepository, never()).save(any(Expense.class));
    }
    
    @Test
    void should_ThrowException_When_CategoryIsNull() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            new BigDecimal("100.00"), 
            "Description", 
            null, 
            "user123"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> createExpenseUseCase.execute(request)
        );
        
        assertEquals("Category cannot be null", exception.getMessage());
        verify(expenseRepository, never()).save(any(Expense.class));
    }
    
    @Test
    void should_ThrowException_When_UserIdIsNull() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            new BigDecimal("100.00"), 
            "Description", 
            "FOOD_DINING", 
            null
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> createExpenseUseCase.execute(request)
        );
        
        assertEquals("User ID cannot be null", exception.getMessage());
        verify(expenseRepository, never()).save(any(Expense.class));
    }
    
    @Test
    void should_CreateExpense_When_AmountIsZero() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            BigDecimal.ZERO, 
            "Zero amount expense", 
            "OTHER", 
            "user123"
        );
        
        Expense savedExpense = new Expense(
            ExpenseId.generate(),
            new Amount(BigDecimal.ZERO),
            new Description("Zero amount expense"),
            new Category("OTHER"),
            LocalDate.now(),
            "user123"
        );
        
        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);
        
        ExpenseResponse response = createExpenseUseCase.execute(request);
        
        assertNotNull(response);
        assertEquals(BigDecimal.ZERO, response.getAmount());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_CreateExpense_When_AmountIsVeryLarge() {
        BigDecimal largeAmount = new BigDecimal("999999999.99");
        CreateExpenseRequest request = new CreateExpenseRequest(
            largeAmount, 
            "Large expense", 
            "SHOPPING", 
            "user123"
        );
        
        Expense savedExpense = new Expense(
            ExpenseId.generate(),
            new Amount(largeAmount),
            new Description("Large expense"),
            new Category("SHOPPING"),
            LocalDate.now(),
            "user123"
        );
        
        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);
        
        ExpenseResponse response = createExpenseUseCase.execute(request);
        
        assertNotNull(response);
        assertEquals(largeAmount, response.getAmount());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_ThrowException_When_DescriptionIsEmpty() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            new BigDecimal("50.00"),
            "",
            "FOOD_DINING",
            "user123"
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> createExpenseUseCase.execute(request)
        );

        assertEquals("Description cannot be empty", exception.getMessage());
        verify(expenseRepository, never()).save(any(Expense.class));
    }
    
    @Test
    void should_ThrowException_When_CategoryIsEmpty() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            new BigDecimal("50.00"),
            "Some expense",
            "",
            "user123"
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> createExpenseUseCase.execute(request)
        );

        assertEquals("Category cannot be empty", exception.getMessage());
        verify(expenseRepository, never()).save(any(Expense.class));
    }
    
    @Test
    void should_ThrowException_When_UserIdIsEmpty() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            new BigDecimal("50.00"),
            "Some expense",
            "FOOD_DINING",
            ""
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> createExpenseUseCase.execute(request)
        );

        assertEquals("User ID cannot be empty", exception.getMessage());
        verify(expenseRepository, never()).save(any(Expense.class));
    }
    
    @Test
    void should_HandleRepositoryException_When_SaveFails() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            new BigDecimal("100.00"), 
            "Test expense", 
            "FOOD_DINING", 
            "user123"
        );
        
        when(expenseRepository.save(any(Expense.class)))
            .thenThrow(new RuntimeException("Database error"));
        
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> createExpenseUseCase.execute(request)
        );
        
        assertEquals("Database error", exception.getMessage());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
    
    @Test
    void should_MapResponseCorrectly_When_ExpenseHasTimestamps() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            new BigDecimal("300.00"), 
            "Test expense", 
            "TRANSPORTATION", 
            "user789"
        );
        
        LocalDateTime now = LocalDateTime.now();
        Expense savedExpense = new Expense(
            ExpenseId.generate(),
            new Amount(new BigDecimal("300.00")),
            new Description("Test expense"),
            new Category("TRANSPORTATION"),
            LocalDate.now(),
            "user789"
        );
        
        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);
        
        ExpenseResponse response = createExpenseUseCase.execute(request);
        
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(savedExpense.getId().getValue().toString(), response.getId());
        assertEquals(savedExpense.getCreatedAt(), response.getCreatedAt());
        assertEquals(savedExpense.getUpdatedAt(), response.getUpdatedAt());
        
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
}