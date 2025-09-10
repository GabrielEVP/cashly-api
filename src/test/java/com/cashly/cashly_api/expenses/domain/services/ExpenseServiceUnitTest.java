package com.cashly.cashly_api.expenses.domain.services;

import com.cashly.cashly_api.expenses.application.ports.ExpenseRepository;
import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.services.ExpenseService.BudgetUtilization;
import com.cashly.cashly_api.expenses.domain.services.ExpenseService.CategoryAnalysis;
import com.cashly.cashly_api.expenses.domain.services.ExpenseService.SpendingAnalysis;
import com.cashly.cashly_api.expenses.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceUnitTest {

    @Mock
    private ExpenseRepository expenseRepository;
    
    private ExpenseService expenseService;
    private String validUserId;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        expenseService = new ExpenseService(expenseRepository);
        validUserId = "user123";
        startDate = LocalDate.of(2024, 1, 1);
        endDate = LocalDate.of(2024, 1, 31);
    }

    @Test
    void should_CalculateMonthlyAverage_When_ValidParametersProvided() {
        // Arrange
        LocalDate referenceDate = LocalDate.of(2024, 1, 31);
        Amount expectedTotal = new Amount(new BigDecimal("2300.00"));
        when(expenseRepository.calculateTotalExpenseForPeriod(eq(validUserId), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(expectedTotal);
        
        // Act
        Amount result = expenseService.calculateMonthlyAverageExpense(validUserId, 3, referenceDate);
        
        // Assert
        BigDecimal expected = new BigDecimal("2300.00").divide(BigDecimal.valueOf(3), 2, BigDecimal.ROUND_HALF_UP);
        assertEquals(expected, result.getValue());
        verify(expenseRepository).calculateTotalExpenseForPeriod(eq(validUserId), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void should_ThrowException_When_MonthsIsZero() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateMonthlyAverageExpense(validUserId, 0, LocalDate.now()));
    }

    @Test
    void should_ThrowException_When_MonthsIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateMonthlyAverageExpense(validUserId, -1, LocalDate.now()));
    }

    @Test
    void should_ThrowException_When_ReferenceDateIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateMonthlyAverageExpense(validUserId, 3, null));
    }

    @Test
    void should_ThrowException_When_UserIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateMonthlyAverageExpense(null, 3, LocalDate.now()));
    }

    @Test
    void should_ThrowException_When_UserIdIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateMonthlyAverageExpense("", 3, LocalDate.now()));
    }

    @Test
    void should_AnalyzeSpendingTrend_When_ValidParametersProvided() {
        // Arrange
        YearMonth currentMonth = YearMonth.of(2024, 1);
        Amount currentExpense = new Amount(new BigDecimal("2300.00"));
        Amount previousExpense = new Amount(BigDecimal.ZERO);
        
        when(expenseRepository.calculateTotalExpenseForPeriod(eq(validUserId), eq(currentMonth.atDay(1)), eq(currentMonth.atEndOfMonth())))
            .thenReturn(currentExpense);
        when(expenseRepository.calculateTotalExpenseForPeriod(eq(validUserId), eq(currentMonth.minusMonths(1).atDay(1)), eq(currentMonth.minusMonths(1).atEndOfMonth())))
            .thenReturn(previousExpense);
        
        // Act
        SpendingAnalysis result = expenseService.analyzeSpendingTrend(validUserId, currentMonth);
        
        // Assert
        assertNotNull(result);
        assertEquals(currentMonth, result.getAnalysisMonth());
        assertEquals(new BigDecimal("2300.00"), result.getCurrentMonthExpense().getValue());
        assertEquals(BigDecimal.ZERO, result.getPreviousMonthExpense().getValue());
        assertEquals(new BigDecimal("100.00"), result.getChangePercentage());
        
        verify(expenseRepository, times(2)).calculateTotalExpenseForPeriod(eq(validUserId), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void should_ReturnZeroChange_When_BothMonthsHaveZeroExpenses() {
        // Arrange
        YearMonth futureMonth = YearMonth.of(2024, 6);
        Amount zeroExpense = new Amount(BigDecimal.ZERO);
        
        when(expenseRepository.calculateTotalExpenseForPeriod(eq(validUserId), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(zeroExpense);
        
        // Act
        SpendingAnalysis result = expenseService.analyzeSpendingTrend(validUserId, futureMonth);
        
        // Assert
        assertEquals(BigDecimal.ZERO, result.getChangePercentage());
        assertEquals(BigDecimal.ZERO, result.getCurrentMonthExpense().getValue());
        assertEquals(BigDecimal.ZERO, result.getPreviousMonthExpense().getValue());
        
        verify(expenseRepository, times(2)).calculateTotalExpenseForPeriod(eq(validUserId), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void should_ThrowException_When_CurrentMonthIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.analyzeSpendingTrend(validUserId, null));
    }

    @Test
    void should_ThrowException_When_UserIdIsNullForSpendingTrend() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.analyzeSpendingTrend(null, YearMonth.of(2024, 1)));
    }

    @Test
    void should_ThrowException_When_UserIdIsEmptyForSpendingTrend() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.analyzeSpendingTrend("", YearMonth.of(2024, 1)));
    }

    @Test
    void should_ReturnTrue_When_ExpenseIsExcessive() {
        Amount testAmount = new Amount(new BigDecimal("5000.00"));
        Amount averageExpense = new Amount(new BigDecimal("2000.00"));
        BigDecimal threshold = new BigDecimal("2.0");
        
        boolean result = expenseService.isExcessiveExpense(testAmount, averageExpense, threshold);
        
        assertTrue(result);
    }

    @Test
    void should_ReturnFalse_When_ExpenseIsNotExcessive() {
        Amount testAmount = new Amount(new BigDecimal("3000.00"));
        Amount averageExpense = new Amount(new BigDecimal("2000.00"));
        BigDecimal threshold = new BigDecimal("2.0");
        
        boolean result = expenseService.isExcessiveExpense(testAmount, averageExpense, threshold);
        
        assertFalse(result);
    }

    @Test
    void should_ThrowException_When_AmountIsNullForExcessiveCheck() {
        Amount averageExpense = new Amount(new BigDecimal("2000.00"));
        BigDecimal threshold = new BigDecimal("2.0");
        
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.isExcessiveExpense(null, averageExpense, threshold));
    }

    @Test
    void should_ThrowException_When_AverageExpenseIsNullForExcessiveCheck() {
        Amount testAmount = new Amount(new BigDecimal("5000.00"));
        BigDecimal threshold = new BigDecimal("2.0");
        
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.isExcessiveExpense(testAmount, null, threshold));
    }

    @Test
    void should_ThrowException_When_ThresholdIsNullForExcessiveCheck() {
        Amount testAmount = new Amount(new BigDecimal("5000.00"));
        Amount averageExpense = new Amount(new BigDecimal("2000.00"));
        
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.isExcessiveExpense(testAmount, averageExpense, null));
    }

    @Test
    void should_ThrowException_When_ThresholdIsZeroForExcessiveCheck() {
        Amount testAmount = new Amount(new BigDecimal("5000.00"));
        Amount averageExpense = new Amount(new BigDecimal("2000.00"));
        BigDecimal threshold = BigDecimal.ZERO;
        
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.isExcessiveExpense(testAmount, averageExpense, threshold));
    }

    @Test
    void should_ThrowException_When_ThresholdIsNegativeForExcessiveCheck() {
        Amount testAmount = new Amount(new BigDecimal("5000.00"));
        Amount averageExpense = new Amount(new BigDecimal("2000.00"));
        BigDecimal threshold = new BigDecimal("-1.0");
        
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.isExcessiveExpense(testAmount, averageExpense, threshold));
    }

    @Test
    void should_CalculateBudgetUtilization_When_ValidParametersProvided() {
        // Arrange
        Amount budgetLimit = new Amount(new BigDecimal("3000.00"));
        Amount actualExpense = new Amount(new BigDecimal("2300.00"));
        
        when(expenseRepository.calculateTotalExpenseForPeriod(validUserId, startDate, endDate))
            .thenReturn(actualExpense);
        
        // Act
        BudgetUtilization result = expenseService.calculateBudgetUtilization(validUserId, budgetLimit, startDate, endDate);
        
        // Assert
        assertNotNull(result);
        assertEquals(budgetLimit, result.getBudgetLimit());
        assertEquals(new BigDecimal("2300.00"), result.getActualExpense().getValue());
        assertEquals(new BigDecimal("700.00"), result.getRemaining().getValue());
        assertEquals(new BigDecimal("76.67"), result.getUtilizationPercentage());
        assertFalse(result.isOverBudget());
        assertEquals(BigDecimal.ZERO, result.getOverspendAmount().getValue());
        
        verify(expenseRepository).calculateTotalExpenseForPeriod(validUserId, startDate, endDate);
    }

    @Test
    void should_CalculateBudgetUtilization_When_OverBudget() {
        // Arrange
        Amount budgetLimit = new Amount(new BigDecimal("2000.00"));
        Amount actualExpense = new Amount(new BigDecimal("2300.00"));
        
        when(expenseRepository.calculateTotalExpenseForPeriod(validUserId, startDate, endDate))
            .thenReturn(actualExpense);
        
        // Act
        BudgetUtilization result = expenseService.calculateBudgetUtilization(validUserId, budgetLimit, startDate, endDate);
        
        // Assert
        assertNotNull(result);
        assertEquals(budgetLimit, result.getBudgetLimit());
        assertEquals(new BigDecimal("2300.00"), result.getActualExpense().getValue());
        assertEquals(BigDecimal.ZERO, result.getRemaining().getValue());
        assertEquals(new BigDecimal("115.00"), result.getUtilizationPercentage());
        assertTrue(result.isOverBudget());
        assertEquals(new BigDecimal("300.00"), result.getOverspendAmount().getValue());
        
        verify(expenseRepository).calculateTotalExpenseForPeriod(validUserId, startDate, endDate);
    }

    @Test
    void should_HandleZeroBudgetLimit_When_CalculatingUtilization() {
        // Arrange
        Amount zeroBudgetLimit = new Amount(BigDecimal.ZERO);
        Amount actualExpense = new Amount(new BigDecimal("2300.00"));
        
        when(expenseRepository.calculateTotalExpenseForPeriod(validUserId, startDate, endDate))
            .thenReturn(actualExpense);
        
        // Act
        BudgetUtilization result = expenseService.calculateBudgetUtilization(validUserId, zeroBudgetLimit, startDate, endDate);
        
        // Assert
        assertNotNull(result);
        assertEquals(zeroBudgetLimit, result.getBudgetLimit());
        assertEquals(new BigDecimal("100.00"), result.getUtilizationPercentage());
        assertTrue(result.isOverBudget());
        
        verify(expenseRepository).calculateTotalExpenseForPeriod(validUserId, startDate, endDate);
    }

    @Test
    void should_ThrowException_When_BudgetLimitIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateBudgetUtilization(validUserId, null, startDate, endDate));
    }

    @Test
    void should_ThrowException_When_UserIdIsNullForBudgetUtilization() {
        Amount budgetLimit = new Amount(new BigDecimal("3000.00"));
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateBudgetUtilization(null, budgetLimit, startDate, endDate));
    }

    @Test
    void should_ThrowException_When_UserIdIsEmptyForBudgetUtilization() {
        Amount budgetLimit = new Amount(new BigDecimal("3000.00"));
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateBudgetUtilization("", budgetLimit, startDate, endDate));
    }

    @Test
    void should_ThrowException_When_StartDateIsNullForBudgetUtilization() {
        Amount budgetLimit = new Amount(new BigDecimal("3000.00"));
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateBudgetUtilization(validUserId, budgetLimit, null, endDate));
    }

    @Test
    void should_ThrowException_When_EndDateIsNullForBudgetUtilization() {
        Amount budgetLimit = new Amount(new BigDecimal("3000.00"));
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateBudgetUtilization(validUserId, budgetLimit, startDate, null));
    }

    @Test
    void should_ThrowException_When_StartDateIsAfterEndDateForBudgetUtilization() {
        Amount budgetLimit = new Amount(new BigDecimal("3000.00"));
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateBudgetUtilization(validUserId, budgetLimit, endDate, startDate));
    }

    @Test
    void should_AnalyzeCategoryBreakdown_When_ValidParametersProvided() {
        // Arrange
        Map<Category, Amount> categoryAmounts = new HashMap<>();
        categoryAmounts.put(new Category("HOUSING"), new Amount(new BigDecimal("1200.00")));
        categoryAmounts.put(new Category("FOOD_DINING"), new Amount(new BigDecimal("800.00")));
        categoryAmounts.put(new Category("OTHER"), new Amount(new BigDecimal("300.00")));
        
        Amount totalExpense = new Amount(new BigDecimal("2300.00"));
        
        when(expenseRepository.calculateExpensesByCategory(validUserId, startDate, endDate))
            .thenReturn(categoryAmounts);
        when(expenseRepository.calculateTotalExpenseForPeriod(validUserId, startDate, endDate))
            .thenReturn(totalExpense);
        
        // Act
        List<CategoryAnalysis> result = expenseService.analyzeCategoryBreakdown(validUserId, startDate, endDate);
        
        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        
        // Should be sorted by amount (descending)
        CategoryAnalysis highest = result.get(0);
        assertEquals(new Category("HOUSING"), highest.getCategory());
        assertEquals(new BigDecimal("1200.00"), highest.getAmount().getValue());
        assertEquals(new BigDecimal("52.17"), highest.getPercentageOfTotal());
        
        CategoryAnalysis second = result.get(1);
        assertEquals(new Category("FOOD_DINING"), second.getCategory());
        assertEquals(new BigDecimal("800.00"), second.getAmount().getValue());
        assertEquals(new BigDecimal("34.78"), second.getPercentageOfTotal());
        
        CategoryAnalysis lowest = result.get(2);
        assertEquals(new Category("OTHER"), lowest.getCategory());
        assertEquals(new BigDecimal("300.00"), lowest.getAmount().getValue());
        assertEquals(new BigDecimal("13.04"), lowest.getPercentageOfTotal());
        
        verify(expenseRepository).calculateExpensesByCategory(validUserId, startDate, endDate);
        verify(expenseRepository).calculateTotalExpenseForPeriod(validUserId, startDate, endDate);
    }

    @Test
    void should_ReturnEmptyList_When_NoExpensesForCategoryBreakdown() {
        // Arrange
        Map<Category, Amount> emptyMap = new HashMap<>();
        Amount zeroTotal = new Amount(BigDecimal.ZERO);
        
        when(expenseRepository.calculateExpensesByCategory(validUserId, startDate, endDate))
            .thenReturn(emptyMap);
        when(expenseRepository.calculateTotalExpenseForPeriod(validUserId, startDate, endDate))
            .thenReturn(zeroTotal);
        
        // Act
        List<CategoryAnalysis> result = expenseService.analyzeCategoryBreakdown(validUserId, startDate, endDate);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(expenseRepository).calculateExpensesByCategory(validUserId, startDate, endDate);
        verify(expenseRepository).calculateTotalExpenseForPeriod(validUserId, startDate, endDate);
    }

    @Test
    void should_ThrowException_When_UserIdIsNullForCategoryBreakdown() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.analyzeCategoryBreakdown(null, startDate, endDate));
    }

    @Test
    void should_ThrowException_When_UserIdIsEmptyForCategoryBreakdown() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.analyzeCategoryBreakdown("", startDate, endDate));
    }

    @Test
    void should_ThrowException_When_StartDateIsNullForCategoryBreakdown() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.analyzeCategoryBreakdown(validUserId, null, endDate));
    }

    @Test
    void should_ThrowException_When_EndDateIsNullForCategoryBreakdown() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.analyzeCategoryBreakdown(validUserId, startDate, null));
    }

    @Test
    void should_ThrowException_When_StartDateIsAfterEndDateForCategoryBreakdown() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.analyzeCategoryBreakdown(validUserId, endDate, startDate));
    }
}