package com.cashly.cashly_api.expenses.domain.services;

import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.services.ExpenseService.BudgetUtilization;
import com.cashly.cashly_api.expenses.domain.services.ExpenseService.CategoryAnalysis;
import com.cashly.cashly_api.expenses.domain.services.ExpenseService.SpendingAnalysis;
import com.cashly.cashly_api.expenses.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseServiceUnitTest {

    private ExpenseService expenseService;
    private List<Expense> testExpenses;
    private String validUserId;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        expenseService = new ExpenseService();
        validUserId = "user123";
        startDate = LocalDate.of(2024, 1, 1);
        endDate = LocalDate.of(2024, 1, 31);
        
        testExpenses = Arrays.asList(
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("800.00")),
                new Description("Groceries"),
                new Category("FOOD_DINING"),
                LocalDate.of(2024, 1, 15),
                validUserId
            ),
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("1200.00")),
                new Description("Rent payment"),
                new Category("HOUSING"),
                LocalDate.of(2024, 1, 1),
                validUserId
            ),
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("300.00")),
                new Description("Gas bill"),
                new Category("OTHER"),
                LocalDate.of(2024, 1, 20),
                validUserId
            ),
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("500.00")),
                new Description("Other user expense"),
                new Category("FOOD_DINING"),
                LocalDate.of(2024, 1, 10),
                "otherUser"
            )
        );
    }

    @Test
    void should_CalculateTotalExpense_When_ValidParametersProvided() {
        Amount result = expenseService.calculateTotalExpenseForPeriod(testExpenses, validUserId, startDate, endDate);
        
        assertEquals(new BigDecimal("2300.00"), result.getValue());
    }

    @Test
    void should_ReturnZeroAmount_When_NoExpensesInPeriod() {
        LocalDate futureStart = LocalDate.of(2024, 6, 1);
        LocalDate futureEnd = LocalDate.of(2024, 6, 30);
        
        Amount result = expenseService.calculateTotalExpenseForPeriod(testExpenses, validUserId, futureStart, futureEnd);
        
        assertEquals(BigDecimal.ZERO, result.getValue());
    }

    @Test
    void should_ReturnZeroAmount_When_NoExpensesForUser() {
        Amount result = expenseService.calculateTotalExpenseForPeriod(testExpenses, "nonexistentUser", startDate, endDate);
        
        assertEquals(BigDecimal.ZERO, result.getValue());
    }

    @Test
    void should_ThrowException_When_ExpensesListIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateTotalExpenseForPeriod(null, validUserId, startDate, endDate));
    }

    @Test
    void should_ThrowException_When_UserIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateTotalExpenseForPeriod(testExpenses, null, startDate, endDate));
    }

    @Test
    void should_ThrowException_When_UserIdIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateTotalExpenseForPeriod(testExpenses, "", startDate, endDate));
    }

    @Test
    void should_ThrowException_When_StartDateIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateTotalExpenseForPeriod(testExpenses, validUserId, null, endDate));
    }

    @Test
    void should_ThrowException_When_EndDateIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateTotalExpenseForPeriod(testExpenses, validUserId, startDate, null));
    }

    @Test
    void should_ThrowException_When_StartDateIsAfterEndDate() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateTotalExpenseForPeriod(testExpenses, validUserId, endDate, startDate));
    }

    @Test
    void should_CalculateExpensesByCategory_When_ValidParametersProvided() {
        Map<Category, Amount> result = expenseService.calculateExpensesByCategory(testExpenses, validUserId, startDate, endDate);
        
        assertEquals(3, result.size());
        assertEquals(new BigDecimal("800.00"), result.get(new Category("FOOD_DINING")).getValue());
        assertEquals(new BigDecimal("1200.00"), result.get(new Category("HOUSING")).getValue());
        assertEquals(new BigDecimal("300.00"), result.get(new Category("OTHER")).getValue());
    }

    @Test
    void should_ReturnEmptyMap_When_NoExpensesInPeriodForCategory() {
        LocalDate futureStart = LocalDate.of(2024, 6, 1);
        LocalDate futureEnd = LocalDate.of(2024, 6, 30);
        
        Map<Category, Amount> result = expenseService.calculateExpensesByCategory(testExpenses, validUserId, futureStart, futureEnd);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void should_CalculateMonthlyAverage_When_ValidParametersProvided() {
        Amount result = expenseService.calculateMonthlyAverageExpense(testExpenses, validUserId, 3, LocalDate.of(2024, 1, 31));
        
        BigDecimal expected = new BigDecimal("2300.00").divide(BigDecimal.valueOf(3), 2, BigDecimal.ROUND_HALF_UP);
        assertEquals(expected, result.getValue());
    }

    @Test
    void should_ThrowException_When_MonthsIsZero() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateMonthlyAverageExpense(testExpenses, validUserId, 0, LocalDate.now()));
    }

    @Test
    void should_ThrowException_When_MonthsIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateMonthlyAverageExpense(testExpenses, validUserId, -1, LocalDate.now()));
    }

    @Test
    void should_ThrowException_When_ReferenceDateIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateMonthlyAverageExpense(testExpenses, validUserId, 3, null));
    }

    @Test
    void should_AnalyzeSpendingTrend_When_ValidParametersProvided() {
        YearMonth currentMonth = YearMonth.of(2024, 1);
        
        SpendingAnalysis result = expenseService.analyzeSpendingTrend(testExpenses, validUserId, currentMonth);
        
        assertNotNull(result);
        assertEquals(currentMonth, result.getAnalysisMonth());
        assertEquals(new BigDecimal("2300.00"), result.getCurrentMonthExpense().getValue());
        assertEquals(BigDecimal.ZERO, result.getPreviousMonthExpense().getValue());
        assertEquals(new BigDecimal("100.00"), result.getChangePercentage());
    }

    @Test
    void should_ReturnZeroChange_When_BothMonthsHaveZeroExpenses() {
        YearMonth futureMonth = YearMonth.of(2024, 6);
        
        SpendingAnalysis result = expenseService.analyzeSpendingTrend(testExpenses, validUserId, futureMonth);
        
        assertEquals(BigDecimal.ZERO, result.getChangePercentage());
        assertEquals(BigDecimal.ZERO, result.getCurrentMonthExpense().getValue());
        assertEquals(BigDecimal.ZERO, result.getPreviousMonthExpense().getValue());
    }

    @Test
    void should_ThrowException_When_CurrentMonthIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.analyzeSpendingTrend(testExpenses, validUserId, null));
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
    void should_CalculateCategoryPercentages_When_ValidParametersProvided() {
        Map<Category, BigDecimal> result = expenseService.calculateCategoryPercentages(testExpenses, validUserId, startDate, endDate);
        
        assertEquals(3, result.size());
        assertEquals(new BigDecimal("34.78"), result.get(new Category("FOOD_DINING")));
        assertEquals(new BigDecimal("52.17"), result.get(new Category("HOUSING")));
        assertEquals(new BigDecimal("13.04"), result.get(new Category("OTHER")));
    }

    @Test
    void should_ReturnZeroPercentages_When_NoExpenseInPeriod() {
        LocalDate futureStart = LocalDate.of(2024, 6, 1);
        LocalDate futureEnd = LocalDate.of(2024, 6, 30);
        
        Map<Category, BigDecimal> result = expenseService.calculateCategoryPercentages(testExpenses, validUserId, futureStart, futureEnd);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void should_AnalyzeHighestSpendingCategory_When_ValidParametersProvided() {
        CategoryAnalysis result = expenseService.analyzeHighestSpendingCategory(testExpenses, validUserId, startDate, endDate);
        
        assertNotNull(result);
        assertEquals(new Category("HOUSING"), result.getCategory());
        assertEquals(new BigDecimal("1200.00"), result.getAmount().getValue());
        assertEquals(new BigDecimal("52.17"), result.getPercentageOfTotal());
    }

    @Test
    void should_ReturnEmptyAnalysis_When_NoExpensesForHighestCategory() {
        LocalDate futureStart = LocalDate.of(2024, 6, 1);
        LocalDate futureEnd = LocalDate.of(2024, 6, 30);
        
        CategoryAnalysis result = expenseService.analyzeHighestSpendingCategory(testExpenses, validUserId, futureStart, futureEnd);
        
        assertNotNull(result);
        assertNull(result.getCategory());
        assertEquals(BigDecimal.ZERO, result.getAmount().getValue());
        assertEquals(BigDecimal.ZERO, result.getPercentageOfTotal());
    }

    @Test
    void should_CalculateBudgetUtilization_When_ValidParametersProvided() {
        Amount budgetLimit = new Amount(new BigDecimal("3000.00"));
        
        BudgetUtilization result = expenseService.calculateBudgetUtilization(testExpenses, validUserId, budgetLimit, startDate, endDate);
        
        assertNotNull(result);
        assertEquals(budgetLimit, result.getBudgetLimit());
        assertEquals(new BigDecimal("2300.00"), result.getActualExpense().getValue());
        assertEquals(new BigDecimal("700.00"), result.getRemaining().getValue());
        assertEquals(new BigDecimal("76.67"), result.getUtilizationPercentage());
        assertFalse(result.isOverBudget());
        assertEquals(BigDecimal.ZERO, result.getOverspendAmount().getValue());
    }

    @Test
    void should_CalculateBudgetUtilization_When_OverBudget() {
        Amount budgetLimit = new Amount(new BigDecimal("2000.00"));
        
        BudgetUtilization result = expenseService.calculateBudgetUtilization(testExpenses, validUserId, budgetLimit, startDate, endDate);
        
        assertNotNull(result);
        assertEquals(budgetLimit, result.getBudgetLimit());
        assertEquals(new BigDecimal("2300.00"), result.getActualExpense().getValue());
        assertEquals(BigDecimal.ZERO, result.getRemaining().getValue());
        assertEquals(new BigDecimal("115.00"), result.getUtilizationPercentage());
        assertTrue(result.isOverBudget());
        assertEquals(new BigDecimal("300.00"), result.getOverspendAmount().getValue());
    }

    @Test
    void should_HandleZeroBudgetLimit_When_CalculatingUtilization() {
        Amount zeroBudgetLimit = new Amount(BigDecimal.ZERO);
        
        BudgetUtilization result = expenseService.calculateBudgetUtilization(testExpenses, validUserId, zeroBudgetLimit, startDate, endDate);
        
        assertNotNull(result);
        assertEquals(zeroBudgetLimit, result.getBudgetLimit());
        assertEquals(new BigDecimal("100.00"), result.getUtilizationPercentage());
        assertTrue(result.isOverBudget());
    }

    @Test
    void should_ThrowException_When_BudgetLimitIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            expenseService.calculateBudgetUtilization(testExpenses, validUserId, null, startDate, endDate));
    }

    @Test
    void should_HandleBoundaryDates_When_ExpensesOnBoundaryDates() {
        Expense boundaryExpense1 = new Expense(
            ExpenseId.generate(),
            new Amount(new BigDecimal("100.00")),
            new Description("Start date expense"),
            new Category("FOOD_DINING"),
            startDate,
            validUserId
        );
        
        Expense boundaryExpense2 = new Expense(
            ExpenseId.generate(),
            new Amount(new BigDecimal("200.00")),
            new Description("End date expense"),
            new Category("FOOD_DINING"),
            endDate,
            validUserId
        );
        
        List<Expense> boundaryExpenses = Arrays.asList(boundaryExpense1, boundaryExpense2);
        
        Amount result = expenseService.calculateTotalExpenseForPeriod(boundaryExpenses, validUserId, startDate, endDate);
        
        assertEquals(new BigDecimal("300.00"), result.getValue());
    }

    @Test
    void should_HandleSingleCategoryExpenses_When_CalculatingPercentages() {
        List<Expense> singleCategoryExpenses = Arrays.asList(
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("1000.00")),
                new Description("Food expense 1"),
                new Category("FOOD_DINING"),
                LocalDate.of(2024, 1, 5),
                validUserId
            ),
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("500.00")),
                new Description("Food expense 2"),
                new Category("FOOD_DINING"),
                LocalDate.of(2024, 1, 15),
                validUserId
            )
        );
        
        Map<Category, BigDecimal> result = expenseService.calculateCategoryPercentages(singleCategoryExpenses, validUserId, startDate, endDate);
        
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("100.00"), result.get(new Category("FOOD_DINING")));
    }
}