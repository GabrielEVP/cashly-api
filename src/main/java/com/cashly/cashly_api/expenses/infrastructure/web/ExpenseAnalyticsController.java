package com.cashly.cashly_api.expenses.infrastructure.web;

import com.cashly.cashly_api.expenses.domain.services.ExpenseService;
import com.cashly.cashly_api.expenses.domain.services.ExpenseService.SpendingAnalysis;
import com.cashly.cashly_api.expenses.domain.services.ExpenseService.BudgetUtilization;
import com.cashly.cashly_api.expenses.domain.services.ExpenseService.CategoryAnalysis;
import com.cashly.cashly_api.expenses.domain.valueobjects.Amount;
import com.cashly.cashly_api.shared.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/expenses/analytics")
public class ExpenseAnalyticsController {

    private final ExpenseService expenseService;

    public ExpenseAnalyticsController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/spending-trend")
    public ResponseEntity<SpendingAnalysis> getSpendingTrend(
            @RequestParam String userId,
            @RequestParam String month) {
        
        if (!ControllerUtils.isValidUserId(userId)) {
            return ResponseEntity.badRequest().build();
        }
        
        YearMonth yearMonth = ControllerUtils.parseYearMonth(month);
        if (yearMonth == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return ControllerUtils.executeServiceCall(() -> 
            expenseService.analyzeSpendingTrend(userId, yearMonth)
        );
    }

    @GetMapping("/budget-utilization")
    public ResponseEntity<BudgetUtilization> getBudgetUtilization(
            @RequestParam String userId,
            @RequestParam String budgetLimit,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        if (!ControllerUtils.isValidUserId(userId) || 
            !ControllerUtils.isValidParameter(budgetLimit) ||
            !ControllerUtils.isValidParameter(startDate) ||
            !ControllerUtils.isValidParameter(endDate)) {
            return ResponseEntity.badRequest().build();
        }
        
        LocalDate start = ControllerUtils.parseLocalDate(startDate);
        LocalDate end = ControllerUtils.parseLocalDate(endDate);
        
        if (start == null || end == null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            Amount budget = new Amount(new BigDecimal(budgetLimit));
            return ControllerUtils.executeServiceCall(() -> 
                expenseService.calculateBudgetUtilization(userId, budget, start, end)
            );
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/category-analysis")
    public ResponseEntity<List<CategoryAnalysis>> getCategoryAnalysis(
            @RequestParam String userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        if (!ControllerUtils.isValidUserId(userId) || 
            !ControllerUtils.isValidParameter(startDate) ||
            !ControllerUtils.isValidParameter(endDate)) {
            return ResponseEntity.badRequest().build();
        }
        
        LocalDate start = ControllerUtils.parseLocalDate(startDate);
        LocalDate end = ControllerUtils.parseLocalDate(endDate);
        
        if (start == null || end == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return ControllerUtils.executeServiceCall(() -> 
            expenseService.analyzeCategoryBreakdown(userId, start, end)
        );
    }

    @GetMapping("/monthly-average")
    public ResponseEntity<Amount> getMonthlyAverage(
            @RequestParam String userId,
            @RequestParam String months,
            @RequestParam String referenceDate) {
        
        if (!ControllerUtils.isValidUserId(userId) || 
            !ControllerUtils.isValidParameter(months) ||
            !ControllerUtils.isValidParameter(referenceDate)) {
            return ResponseEntity.badRequest().build();
        }
        
        Integer monthCount = ControllerUtils.parseInteger(months);
        LocalDate reference = ControllerUtils.parseLocalDate(referenceDate);
        
        if (monthCount == null || reference == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return ControllerUtils.executeServiceCall(() -> 
            expenseService.calculateMonthlyAverageExpense(userId, monthCount, reference)
        );
    }
}