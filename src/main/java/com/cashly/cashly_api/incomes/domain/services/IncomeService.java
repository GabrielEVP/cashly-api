package com.cashly.cashly_api.incomes.domain.services;

import com.cashly.cashly_api.incomes.application.ports.IncomeRepository;
import com.cashly.cashly_api.incomes.domain.valueobjects.Amount;
import com.cashly.cashly_api.incomes.domain.valueobjects.Category;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;

public class IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }


    public Amount calculateMonthlyAverageIncome(String userId, int months, LocalDate referenceDate) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (months <= 0) {
            throw new IllegalArgumentException("Months must be positive");
        }
        if (referenceDate == null) {
            throw new IllegalArgumentException("Reference date cannot be null");
        }

        LocalDate startDate = referenceDate.minusMonths(months - 1).withDayOfMonth(1);
        LocalDate endDate = referenceDate.withDayOfMonth(referenceDate.lengthOfMonth());

        Amount totalIncome = incomeRepository.calculateTotalIncomeForPeriod(userId, startDate, endDate);
        BigDecimal average = totalIncome.getValue().divide(
            BigDecimal.valueOf(months), 
            2, 
            RoundingMode.HALF_UP
        );

        return new Amount(average);
    }

    public IncomeGrowthAnalysis analyzeIncomeGrowth(String userId, YearMonth currentMonth) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (currentMonth == null) {
            throw new IllegalArgumentException("Current month cannot be null");
        }

        YearMonth previousMonth = currentMonth.minusMonths(1);

        LocalDate currentStart = currentMonth.atDay(1);
        LocalDate currentEnd = currentMonth.atEndOfMonth();
        LocalDate previousStart = previousMonth.atDay(1);
        LocalDate previousEnd = previousMonth.atEndOfMonth();

        Amount currentIncome = incomeRepository.calculateTotalIncomeForPeriod(userId, currentStart, currentEnd);
        Amount previousIncome = incomeRepository.calculateTotalIncomeForPeriod(userId, previousStart, previousEnd);

        BigDecimal growthPercentage = calculateGrowthPercentage(
            previousIncome.getValue(), 
            currentIncome.getValue()
        );

        return new IncomeGrowthAnalysis(
            previousIncome,
            currentIncome,
            growthPercentage,
            currentMonth
        );
    }

    public boolean isSignificantIncome(Amount amount, Amount userAverageIncome, 
                                     BigDecimal significanceThreshold) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (userAverageIncome == null) {
            throw new IllegalArgumentException("User average income cannot be null");
        }
        if (significanceThreshold == null || significanceThreshold.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Significance threshold must be positive");
        }

        BigDecimal threshold = userAverageIncome.getValue().multiply(significanceThreshold);
        return amount.getValue().compareTo(threshold) > 0;
    }



    private void validatePeriodCalculationParams(String userId, LocalDate startDate, LocalDate endDate) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    private BigDecimal calculateGrowthPercentage(BigDecimal previousAmount, BigDecimal currentAmount) {
        if (previousAmount.compareTo(BigDecimal.ZERO) == 0) {
            return currentAmount.compareTo(BigDecimal.ZERO) == 0 ?
                BigDecimal.ZERO :
                new BigDecimal("100.00");
        }

        return currentAmount.subtract(previousAmount)
            .divide(previousAmount, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(2, RoundingMode.HALF_UP);
    }

    public static class IncomeGrowthAnalysis {
        private final Amount previousMonthIncome;
        private final Amount currentMonthIncome;
        private final BigDecimal growthPercentage;
        private final YearMonth analysisMonth;

        public IncomeGrowthAnalysis(Amount previousMonthIncome, Amount currentMonthIncome,
                                  BigDecimal growthPercentage, YearMonth analysisMonth) {
            this.previousMonthIncome = previousMonthIncome;
            this.currentMonthIncome = currentMonthIncome;
            this.growthPercentage = growthPercentage;
            this.analysisMonth = analysisMonth;
        }

        public Amount getPreviousMonthIncome() {
            return previousMonthIncome;
        }

        public Amount getCurrentMonthIncome() {
            return currentMonthIncome;
        }

        public BigDecimal getGrowthPercentage() {
            return growthPercentage;
        }

        public YearMonth getAnalysisMonth() {
            return analysisMonth;
        }

        public boolean hasPositiveGrowth() {
            return growthPercentage.compareTo(BigDecimal.ZERO) > 0;
        }

        public boolean hasNegativeGrowth() {
            return growthPercentage.compareTo(BigDecimal.ZERO) < 0;
        }

        public Amount getGrowthAmount() {
            BigDecimal currentValue = currentMonthIncome.getValue();
            BigDecimal previousValue = previousMonthIncome.getValue();
            BigDecimal difference = currentValue.subtract(previousValue);
            return new Amount(difference.abs());
        }

        @Override
        public String toString() {
            return "IncomeGrowthAnalysis{" +
                    "previousMonthIncome=" + previousMonthIncome +
                    ", currentMonthIncome=" + currentMonthIncome +
                    ", growthPercentage=" + growthPercentage +
                    ", analysisMonth=" + analysisMonth +
                    '}';
        }
    }
}