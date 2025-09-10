package com.cashly.cashly_api.expenses.infrastructure.web;

import com.cashly.cashly_api.expenses.domain.services.ExpenseService;
import com.cashly.cashly_api.expenses.domain.services.ExpenseService.SpendingAnalysis;
import com.cashly.cashly_api.expenses.domain.services.ExpenseService.BudgetUtilization;
import com.cashly.cashly_api.expenses.domain.services.ExpenseService.CategoryAnalysis;
import com.cashly.cashly_api.expenses.domain.valueobjects.Amount;
import com.cashly.cashly_api.expenses.domain.valueobjects.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseAnalyticsControllerUnitTest {

    @Mock
    private ExpenseService expenseService;

    private ExpenseAnalyticsController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ExpenseAnalyticsController(expenseService);
    }

    @Test
    void should_ReturnSpendingAnalysis_When_ValidSpendingTrendRequested() {
        // Arrange
        String userId = "user123";
        String monthParam = "2024-03";
        YearMonth month = YearMonth.parse(monthParam);
        
        SpendingAnalysis expectedAnalysis = new SpendingAnalysis(
            new Amount(BigDecimal.valueOf(1000)),
            new Amount(BigDecimal.valueOf(1200)),
            BigDecimal.valueOf(20.00),
            month
        );
        
        when(expenseService.analyzeSpendingTrend(userId, month)).thenReturn(expectedAnalysis);

        // Act
        ResponseEntity<SpendingAnalysis> result = controller.getSpendingTrend(userId, monthParam);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedAnalysis, result.getBody());
        verify(expenseService).analyzeSpendingTrend(userId, month);
    }

    @Test
    void should_ReturnBadRequest_When_SpendingTrendRequestedWithNullUserId() {
        // Act
        ResponseEntity<SpendingAnalysis> result = controller.getSpendingTrend(null, "2024-03");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(expenseService);
    }

    @Test
    void should_ReturnBadRequest_When_SpendingTrendRequestedWithInvalidMonth() {
        // Act
        ResponseEntity<SpendingAnalysis> result = controller.getSpendingTrend("user123", "invalid-month");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(expenseService);
    }

    @Test
    void should_ReturnInternalServerError_When_SpendingTrendServiceThrowsException() {
        // Arrange
        String userId = "user123";
        String monthParam = "2024-03";
        
        when(expenseService.analyzeSpendingTrend(userId, YearMonth.parse(monthParam)))
            .thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<SpendingAnalysis> result = controller.getSpendingTrend(userId, monthParam);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void should_ReturnBudgetUtilization_When_ValidBudgetUtilizationRequested() {
        // Arrange
        String userId = "user123";
        String budgetLimitParam = "1500.00";
        String startDateParam = "2024-03-01";
        String endDateParam = "2024-03-31";
        
        Amount budgetLimit = new Amount(new BigDecimal(budgetLimitParam));
        LocalDate startDate = LocalDate.parse(startDateParam);
        LocalDate endDate = LocalDate.parse(endDateParam);
        
        BudgetUtilization expectedUtilization = new BudgetUtilization(
            budgetLimit,
            new Amount(BigDecimal.valueOf(1200)),
            new Amount(BigDecimal.valueOf(300)),
            BigDecimal.valueOf(80.00),
            false
        );
        
        when(expenseService.calculateBudgetUtilization(userId, budgetLimit, startDate, endDate))
            .thenReturn(expectedUtilization);

        // Act
        ResponseEntity<BudgetUtilization> result = controller.getBudgetUtilization(
            userId, budgetLimitParam, startDateParam, endDateParam);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedUtilization, result.getBody());
        verify(expenseService).calculateBudgetUtilization(userId, budgetLimit, startDate, endDate);
    }

    @Test
    void should_ReturnBadRequest_When_BudgetUtilizationRequestedWithNullParameters() {
        // Act
        ResponseEntity<BudgetUtilization> result = controller.getBudgetUtilization(
            null, "1500.00", "2024-03-01", "2024-03-31");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(expenseService);
    }

    @Test
    void should_ReturnBadRequest_When_BudgetUtilizationRequestedWithInvalidDateFormat() {
        // Act
        ResponseEntity<BudgetUtilization> result = controller.getBudgetUtilization(
            "user123", "1500.00", "invalid-date", "2024-03-31");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(expenseService);
    }

    @Test
    void should_ReturnCategoryAnalysis_When_ValidCategoryAnalysisRequested() {
        // Arrange
        String userId = "user123";
        String startDateParam = "2024-03-01";
        String endDateParam = "2024-03-31";
        
        LocalDate startDate = LocalDate.parse(startDateParam);
        LocalDate endDate = LocalDate.parse(endDateParam);
        
        List<CategoryAnalysis> expectedAnalysis = List.of(
            new CategoryAnalysis(new Category("FOOD_DINING"), new Amount(BigDecimal.valueOf(500)), BigDecimal.valueOf(41.67)),
            new CategoryAnalysis(new Category("TRANSPORTATION"), new Amount(BigDecimal.valueOf(300)), BigDecimal.valueOf(25.00))
        );
        
        when(expenseService.analyzeCategoryBreakdown(userId, startDate, endDate))
            .thenReturn(expectedAnalysis);

        // Act
        ResponseEntity<List<CategoryAnalysis>> result = controller.getCategoryAnalysis(
            userId, startDateParam, endDateParam);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedAnalysis, result.getBody());
        verify(expenseService).analyzeCategoryBreakdown(userId, startDate, endDate);
    }

    @Test
    void should_ReturnBadRequest_When_CategoryAnalysisRequestedWithNullUserId() {
        // Act
        ResponseEntity<List<CategoryAnalysis>> result = controller.getCategoryAnalysis(
            null, "2024-03-01", "2024-03-31");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(expenseService);
    }

    @Test
    void should_ReturnMonthlyAverage_When_ValidMonthlyAverageRequested() {
        // Arrange
        String userId = "user123";
        String monthsParam = "6";
        String referenceDateParam = "2024-03-31";
        
        int months = Integer.parseInt(monthsParam);
        LocalDate referenceDate = LocalDate.parse(referenceDateParam);
        
        Amount expectedAverage = new Amount(BigDecimal.valueOf(1100.50));
        
        when(expenseService.calculateMonthlyAverageExpense(userId, months, referenceDate))
            .thenReturn(expectedAverage);

        // Act
        ResponseEntity<Amount> result = controller.getMonthlyAverage(userId, monthsParam, referenceDateParam);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedAverage, result.getBody());
        verify(expenseService).calculateMonthlyAverageExpense(userId, months, referenceDate);
    }

    @Test
    void should_ReturnBadRequest_When_MonthlyAverageRequestedWithNullUserId() {
        // Act
        ResponseEntity<Amount> result = controller.getMonthlyAverage(null, "6", "2024-03-31");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(expenseService);
    }

    @Test
    void should_ReturnBadRequest_When_MonthlyAverageRequestedWithInvalidMonths() {
        // Act
        ResponseEntity<Amount> result = controller.getMonthlyAverage("user123", "invalid", "2024-03-31");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(expenseService);
    }
}