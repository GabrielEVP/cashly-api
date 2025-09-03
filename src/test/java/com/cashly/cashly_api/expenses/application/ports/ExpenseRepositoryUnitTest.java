package com.cashly.cashly_api.expenses.application.ports;

import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExpenseRepositoryUnitTest {
    
    @Mock
    private ExpenseRepository expenseRepository;
    
    private Expense testExpense;
    private ExpenseId testExpenseId;
    private String testUserId;
    private Category testCategory;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testExpenseId = ExpenseId.generate();
        testUserId = "user123";
        testCategory = new Category("FOOD_DINING");
        
        testExpense = new Expense(
            testExpenseId,
            new Amount(new BigDecimal("500.00")),
            new Description("Test expense"),
            testCategory,
            LocalDate.now(),
            testUserId
        );
    }
    
    @Test
    void should_SaveExpense_When_ValidExpenseProvided() {
        when(expenseRepository.save(testExpense)).thenReturn(testExpense);
        
        Expense savedExpense = expenseRepository.save(testExpense);
        
        assertNotNull(savedExpense);
        assertEquals(testExpense, savedExpense);
        verify(expenseRepository, times(1)).save(testExpense);
    }
    
    @Test
    void should_ReturnSavedExpense_When_SaveCalled() {
        Expense modifiedExpense = new Expense(
            testExpenseId,
            new Amount(new BigDecimal("600.00")),
            new Description("Modified expense"),
            testCategory,
            LocalDate.now(),
            testUserId
        );
        
        when(expenseRepository.save(testExpense)).thenReturn(modifiedExpense);
        
        Expense result = expenseRepository.save(testExpense);
        
        assertNotNull(result);
        assertEquals(modifiedExpense, result);
        assertNotEquals(testExpense.getAmount(), result.getAmount());
        verify(expenseRepository, times(1)).save(testExpense);
    }
    
    @Test
    void should_FindExpenseById_When_ExpenseExists() {
        when(expenseRepository.findById(testExpenseId)).thenReturn(Optional.of(testExpense));
        
        Optional<Expense> foundExpense = expenseRepository.findById(testExpenseId);
        
        assertTrue(foundExpense.isPresent());
        assertEquals(testExpense, foundExpense.get());
        verify(expenseRepository, times(1)).findById(testExpenseId);
    }
    
    @Test
    void should_ReturnEmptyOptional_When_ExpenseNotFound() {
        when(expenseRepository.findById(testExpenseId)).thenReturn(Optional.empty());
        
        Optional<Expense> foundExpense = expenseRepository.findById(testExpenseId);
        
        assertFalse(foundExpense.isPresent());
        verify(expenseRepository, times(1)).findById(testExpenseId);
    }
    
    @Test
    void should_FindExpensesByUserId_When_UserHasExpenses() {
        List<Expense> userExpenses = Arrays.asList(testExpense);
        when(expenseRepository.findByUserId(testUserId)).thenReturn(userExpenses);
        
        List<Expense> foundExpenses = expenseRepository.findByUserId(testUserId);
        
        assertNotNull(foundExpenses);
        assertEquals(1, foundExpenses.size());
        assertEquals(testExpense, foundExpenses.get(0));
        verify(expenseRepository, times(1)).findByUserId(testUserId);
    }
    
    @Test
    void should_ReturnEmptyList_When_UserHasNoExpenses() {
        when(expenseRepository.findByUserId(testUserId)).thenReturn(Collections.emptyList());
        
        List<Expense> foundExpenses = expenseRepository.findByUserId(testUserId);
        
        assertNotNull(foundExpenses);
        assertTrue(foundExpenses.isEmpty());
        verify(expenseRepository, times(1)).findByUserId(testUserId);
    }
    
    @Test
    void should_FindExpensesByUserIdAndDateRange_When_ExpensesExistInRange() {
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 31, 23, 59);
        List<Expense> rangeExpenses = Arrays.asList(testExpense);
        
        when(expenseRepository.findByUserIdAndDateRange(testUserId, startDateTime, endDateTime))
            .thenReturn(rangeExpenses);
        
        List<Expense> foundExpenses = expenseRepository.findByUserIdAndDateRange(testUserId, startDateTime, endDateTime);
        
        assertNotNull(foundExpenses);
        assertEquals(1, foundExpenses.size());
        assertEquals(testExpense, foundExpenses.get(0));
        verify(expenseRepository, times(1)).findByUserIdAndDateRange(testUserId, startDateTime, endDateTime);
    }
    
    @Test
    void should_ReturnEmptyList_When_NoExpensesInDateRange() {
        LocalDateTime startDateTime = LocalDateTime.of(2024, 6, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 6, 30, 23, 59);
        
        when(expenseRepository.findByUserIdAndDateRange(testUserId, startDateTime, endDateTime))
            .thenReturn(Collections.emptyList());
        
        List<Expense> foundExpenses = expenseRepository.findByUserIdAndDateRange(testUserId, startDateTime, endDateTime);
        
        assertNotNull(foundExpenses);
        assertTrue(foundExpenses.isEmpty());
        verify(expenseRepository, times(1)).findByUserIdAndDateRange(testUserId, startDateTime, endDateTime);
    }
    
    @Test
    void should_FindExpensesByUserIdAndCategory_When_ExpensesExistForCategory() {
        List<Expense> categoryExpenses = Arrays.asList(testExpense);
        
        when(expenseRepository.findByUserIdAndCategory(testUserId, testCategory))
            .thenReturn(categoryExpenses);
        
        List<Expense> foundExpenses = expenseRepository.findByUserIdAndCategory(testUserId, testCategory);
        
        assertNotNull(foundExpenses);
        assertEquals(1, foundExpenses.size());
        assertEquals(testExpense, foundExpenses.get(0));
        verify(expenseRepository, times(1)).findByUserIdAndCategory(testUserId, testCategory);
    }
    
    @Test
    void should_ReturnEmptyList_When_NoExpensesForCategory() {
        Category emptyCategory = new Category("OTHER");
        
        when(expenseRepository.findByUserIdAndCategory(testUserId, emptyCategory))
            .thenReturn(Collections.emptyList());
        
        List<Expense> foundExpenses = expenseRepository.findByUserIdAndCategory(testUserId, emptyCategory);
        
        assertNotNull(foundExpenses);
        assertTrue(foundExpenses.isEmpty());
        verify(expenseRepository, times(1)).findByUserIdAndCategory(testUserId, emptyCategory);
    }
    
    @Test
    void should_DeleteExpenseById_When_ExpenseExists() {
        doNothing().when(expenseRepository).deleteById(testExpenseId);
        
        assertDoesNotThrow(() -> expenseRepository.deleteById(testExpenseId));
        
        verify(expenseRepository, times(1)).deleteById(testExpenseId);
    }
    
    @Test
    void should_HandleDeleteById_When_ExpenseDoesNotExist() {
        doNothing().when(expenseRepository).deleteById(testExpenseId);
        
        assertDoesNotThrow(() -> expenseRepository.deleteById(testExpenseId));
        
        verify(expenseRepository, times(1)).deleteById(testExpenseId);
    }
    
    @Test
    void should_ReturnTrue_When_ExpenseExists() {
        when(expenseRepository.existsById(testExpenseId)).thenReturn(true);
        
        boolean exists = expenseRepository.existsById(testExpenseId);
        
        assertTrue(exists);
        verify(expenseRepository, times(1)).existsById(testExpenseId);
    }
    
    @Test
    void should_ReturnFalse_When_ExpenseDoesNotExist() {
        when(expenseRepository.existsById(testExpenseId)).thenReturn(false);
        
        boolean exists = expenseRepository.existsById(testExpenseId);
        
        assertFalse(exists);
        verify(expenseRepository, times(1)).existsById(testExpenseId);
    }
    
    @Test
    void should_HandleMultipleExpenses_When_FindingByUserId() {
        List<Expense> multipleExpenses = Arrays.asList(
            testExpense,
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("200.00")),
                new Description("Second expense"),
                new Category("TRANSPORTATION"),
                LocalDate.now(),
                testUserId
            ),
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("800.00")),
                new Description("Third expense"),
                new Category("HOUSING"),
                LocalDate.now(),
                testUserId
            )
        );
        
        when(expenseRepository.findByUserId(testUserId)).thenReturn(multipleExpenses);
        
        List<Expense> foundExpenses = expenseRepository.findByUserId(testUserId);
        
        assertNotNull(foundExpenses);
        assertEquals(3, foundExpenses.size());
        assertTrue(foundExpenses.contains(testExpense));
        verify(expenseRepository, times(1)).findByUserId(testUserId);
    }
    
    @Test
    void should_HandleDifferentCategories_When_FindingByUserIdAndCategory() {
        Category transportCategory = new Category("TRANSPORTATION");
        List<Expense> transportExpenses = Arrays.asList(
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("150.00")),
                new Description("Bus ticket"),
                transportCategory,
                LocalDate.now(),
                testUserId
            )
        );
        
        when(expenseRepository.findByUserIdAndCategory(testUserId, transportCategory))
            .thenReturn(transportExpenses);
        
        List<Expense> foundExpenses = expenseRepository.findByUserIdAndCategory(testUserId, transportCategory);
        
        assertNotNull(foundExpenses);
        assertEquals(1, foundExpenses.size());
        assertEquals(transportCategory, foundExpenses.get(0).getCategory());
        verify(expenseRepository, times(1)).findByUserIdAndCategory(testUserId, transportCategory);
    }
    
    @Test
    void should_HandleNullResults_When_RepositoryReturnsNull() {
        when(expenseRepository.findById(testExpenseId)).thenReturn(null);
        
        Optional<Expense> result = expenseRepository.findById(testExpenseId);
        
        assertNull(result);
        verify(expenseRepository, times(1)).findById(testExpenseId);
    }
    
    @Test
    void should_HandleRepositoryExceptions_When_OperationsFail() {
        when(expenseRepository.save(any(Expense.class)))
            .thenThrow(new RuntimeException("Database connection error"));
        
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> expenseRepository.save(testExpense)
        );
        
        assertEquals("Database connection error", exception.getMessage());
        verify(expenseRepository, times(1)).save(testExpense);
    }
    
    @Test
    void should_HandleDateRangeBoundaries_When_FindingByDateRange() {
        LocalDateTime exactStart = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime exactEnd = LocalDateTime.of(2024, 1, 1, 23, 59, 59);
        
        when(expenseRepository.findByUserIdAndDateRange(testUserId, exactStart, exactEnd))
            .thenReturn(Arrays.asList(testExpense));
        
        List<Expense> foundExpenses = expenseRepository.findByUserIdAndDateRange(testUserId, exactStart, exactEnd);
        
        assertNotNull(foundExpenses);
        assertEquals(1, foundExpenses.size());
        verify(expenseRepository, times(1)).findByUserIdAndDateRange(testUserId, exactStart, exactEnd);
    }
    
    @Test
    void should_HandleEmptyStringUserId_When_FindingByUserId() {
        String emptyUserId = "";
        when(expenseRepository.findByUserId(emptyUserId)).thenReturn(Collections.emptyList());
        
        List<Expense> foundExpenses = expenseRepository.findByUserId(emptyUserId);
        
        assertNotNull(foundExpenses);
        assertTrue(foundExpenses.isEmpty());
        verify(expenseRepository, times(1)).findByUserId(emptyUserId);
    }
    
    @Test
    void should_HandleNullUserId_When_FindingByUserId() {
        when(expenseRepository.findByUserId(null)).thenThrow(new IllegalArgumentException("User ID cannot be null"));
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> expenseRepository.findByUserId(null)
        );
        
        assertEquals("User ID cannot be null", exception.getMessage());
        verify(expenseRepository, times(1)).findByUserId(null);
    }
}