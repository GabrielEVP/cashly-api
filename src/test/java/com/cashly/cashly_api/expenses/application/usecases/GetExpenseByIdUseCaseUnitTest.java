package com.cashly.cashly_api.expenses.application.usecases;

import com.cashly.cashly_api.expenses.application.dto.ExpenseResponse;
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

class GetExpenseByIdUseCaseUnitTest {
    
    @Mock
    private ExpenseRepository expenseRepository;
    
    private GetExpenseByIdUseCase getExpenseByIdUseCase;
    private String validExpenseId;
    private Expense existingExpense;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        getExpenseByIdUseCase = new GetExpenseByIdUseCase(expenseRepository);
        
        validExpenseId = UUID.randomUUID().toString();
        existingExpense = new Expense(
            new ExpenseId(UUID.fromString(validExpenseId)),
            new Amount(new BigDecimal("750.00")),
            new Description("Monthly rent"),
            new Category("HOUSING"),
            LocalDate.of(2024, 1, 15),
            "user123"
        );
    }
    
    @Test
    void should_ReturnExpenseResponse_When_ValidExpenseIdProvided() {
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        
        ExpenseResponse response = getExpenseByIdUseCase.execute(validExpenseId);
        
        assertNotNull(response);
        assertEquals(existingExpense.getId().getValue().toString(), response.getId());
        assertEquals(existingExpense.getAmount().getValue(), response.getAmount());
        assertEquals(existingExpense.getDescription().getValue(), response.getDescription());
        assertEquals(existingExpense.getCategory().getValue(), response.getCategory());
        assertEquals(existingExpense.getUserId(), response.getUserId());
        assertEquals(existingExpense.getCreatedAt(), response.getCreatedAt());
        assertEquals(existingExpense.getUpdatedAt(), response.getUpdatedAt());
        
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_ThrowException_When_ExpenseIdIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> getExpenseByIdUseCase.execute(null)
        );
        
        assertEquals("Expense ID cannot be null", exception.getMessage());
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_ThrowException_When_ExpenseIdIsInvalidUUID() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> getExpenseByIdUseCase.execute("invalid-uuid")
        );
        
        assertTrue(exception.getMessage().contains("Invalid UUID format for expense ID"));
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_ThrowException_When_ExpenseNotFound() {
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.empty());
        
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> getExpenseByIdUseCase.execute(validExpenseId)
        );
        
        assertTrue(exception.getMessage().contains("Expense not found with ID"));
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_HandleRepositoryException_When_FindByIdFails() {
        when(expenseRepository.findById(any(ExpenseId.class)))
            .thenThrow(new RuntimeException("Database connection error"));
        
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> getExpenseByIdUseCase.execute(validExpenseId)
        );
        
        assertEquals("Database connection error", exception.getMessage());
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_ReturnCorrectResponse_When_ExpenseHasZeroAmount() {
        Expense zeroAmountExpense = new Expense(
            new ExpenseId(UUID.fromString(validExpenseId)),
            new Amount(BigDecimal.ZERO),
            new Description("Zero amount test"),
            new Category("OTHER"),
            LocalDate.now(),
            "user456"
        );

        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(zeroAmountExpense));

        ExpenseResponse response = getExpenseByIdUseCase.execute(validExpenseId);

        assertNotNull(response);
        assertEquals(BigDecimal.ZERO, response.getAmount());
        assertEquals("Zero amount test", response.getDescription());
        assertEquals("OTHER", response.getCategory());
        assertEquals("user456", response.getUserId());

        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_ReturnCorrectResponse_When_ExpenseHasLargeAmount() {
        BigDecimal largeAmount = new BigDecimal("999999999.99");
        Expense largeAmountExpense = new Expense(
            new ExpenseId(UUID.fromString(validExpenseId)),
            new Amount(largeAmount),
            new Description("Large amount expense"),
            new Category("SHOPPING"),
            LocalDate.now(),
            "user789"
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(largeAmountExpense));
        
        ExpenseResponse response = getExpenseByIdUseCase.execute(validExpenseId);
        
        assertNotNull(response);
        assertEquals(largeAmount, response.getAmount());
        assertEquals("Large amount expense", response.getDescription());
        assertEquals("SHOPPING", response.getCategory());
        assertEquals("user789", response.getUserId());
        
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_ReturnCorrectResponse_When_ExpenseHasMinimalDescription() {
        Expense minimalDescriptionExpense = new Expense(
            new ExpenseId(UUID.fromString(validExpenseId)),
            new Amount(new BigDecimal("100.00")),
            new Description("Expense"),
            new Category("OTHER"),
            LocalDate.now(),
            "user123"
        );

        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(minimalDescriptionExpense));

        ExpenseResponse response = getExpenseByIdUseCase.execute(validExpenseId);

        assertNotNull(response);
        assertEquals("Expense", response.getDescription());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
        assertEquals("OTHER", response.getCategory());

        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_ReturnCorrectResponse_When_ExpenseHasOtherCategory() {
        Expense otherCategoryExpense = new Expense(
            new ExpenseId(UUID.fromString(validExpenseId)),
            new Amount(new BigDecimal("200.00")),
            new Description("Test expense"),
            new Category("OTHER"),
            LocalDate.now(),
            "user123"
        );

        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(otherCategoryExpense));

        ExpenseResponse response = getExpenseByIdUseCase.execute(validExpenseId);

        assertNotNull(response);
        assertEquals("OTHER", response.getCategory());
        assertEquals(new BigDecimal("200.00"), response.getAmount());
        assertEquals("Test expense", response.getDescription());

        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_ReturnCorrectResponse_When_ExpenseHasValidUserId() {
        Expense validUserIdExpense = new Expense(
            new ExpenseId(UUID.fromString(validExpenseId)),
            new Amount(new BigDecimal("300.00")),
            new Description("User expense"),
            new Category("FOOD_DINING"),
            LocalDate.now(),
            "user999"
        );

        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(validUserIdExpense));

        ExpenseResponse response = getExpenseByIdUseCase.execute(validExpenseId);

        assertNotNull(response);
        assertEquals("user999", response.getUserId());
        assertEquals(new BigDecimal("300.00"), response.getAmount());
        assertEquals("User expense", response.getDescription());
        assertEquals("FOOD_DINING", response.getCategory());

        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_ParseValidUUID_When_ValidUUIDStringProvided() {
        String uuidString = "12345678-1234-1234-1234-123456789012";
        Expense expenseWithSpecificId = new Expense(
            new ExpenseId(UUID.fromString(uuidString)),
            new Amount(new BigDecimal("400.00")),
            new Description("Specific expense"),
            new Category("OTHER"),
            LocalDate.now(),
            "user456"
        );
        
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(expenseWithSpecificId));
        
        ExpenseResponse response = getExpenseByIdUseCase.execute(uuidString);
        
        assertNotNull(response);
        assertEquals(uuidString, response.getId());
        
        verify(expenseRepository, times(1)).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_ThrowException_When_UUIDFormatIsIncorrect() {
        String incorrectUUID = "not-a-valid-uuid-format";
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> getExpenseByIdUseCase.execute(incorrectUUID)
        );
        
        assertTrue(exception.getMessage().contains("Invalid UUID format for expense ID"));
        assertTrue(exception.getMessage().contains(incorrectUUID));
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_ThrowException_When_UUIDHasExtraCharacters() {
        String malformedUUID = "12345678-1234-1234-1234-123456789012-extra";
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> getExpenseByIdUseCase.execute(malformedUUID)
        );
        
        assertTrue(exception.getMessage().contains("Invalid UUID format for expense ID"));
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_ThrowException_When_UUIDIsTooShort() {
        String shortUUID = "12345678-1234-1234";
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> getExpenseByIdUseCase.execute(shortUUID)
        );
        
        assertTrue(exception.getMessage().contains("Invalid UUID format for expense ID"));
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_ThrowException_When_EmptyStringProvided() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> getExpenseByIdUseCase.execute("")
        );
        
        assertTrue(exception.getMessage().contains("Invalid UUID format for expense ID"));
        verify(expenseRepository, never()).findById(any(ExpenseId.class));
    }
    
    @Test
    void should_PassCorrectExpenseId_When_CallingRepository() {
        when(expenseRepository.findById(any(ExpenseId.class))).thenReturn(Optional.of(existingExpense));
        
        getExpenseByIdUseCase.execute(validExpenseId);
        
        ExpenseId expectedExpenseId = new ExpenseId(UUID.fromString(validExpenseId));
        verify(expenseRepository, times(1)).findById(eq(expectedExpenseId));
    }
}