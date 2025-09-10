package com.cashly.cashly_api.incomes.infrastructure.web;

import com.cashly.cashly_api.incomes.domain.services.IncomeService;
import com.cashly.cashly_api.incomes.domain.services.IncomeService.IncomeGrowthAnalysis;
import com.cashly.cashly_api.incomes.domain.valueobjects.Amount;
import com.cashly.cashly_api.shared.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/incomes/analytics")
public class IncomeAnalyticsController {

    private final IncomeService incomeService;

    public IncomeAnalyticsController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping("/growth-analysis")
    public ResponseEntity<IncomeGrowthAnalysis> getIncomeGrowthAnalysis(
            @RequestParam String userId,
            @RequestParam String month) {
        
        if (!ControllerUtils.isValidUserId(userId) || 
            !ControllerUtils.isValidParameter(month)) {
            return ResponseEntity.badRequest().build();
        }
        
        YearMonth yearMonth = ControllerUtils.parseYearMonth(month);
        if (yearMonth == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return ControllerUtils.executeServiceCall(() -> 
            incomeService.analyzeIncomeGrowth(userId, yearMonth)
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
            incomeService.calculateMonthlyAverageIncome(userId, monthCount, reference)
        );
    }
}