package com.cashly.cashly_api.expenses.domain.services;

import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.valueobjects.Amount;
import com.cashly.cashly_api.expenses.domain.valueobjects.Category;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseService {

    public Amount calculateTotalExpenseForPeriod(List<Expense> expenses, String userId, 
                                                LocalDate startDate, LocalDate endDate) {
        validatePeriodCalculationParams(expenses, userId, startDate, endDate);

        BigDecimal total = expenses.stream()
            .filter(expense -> expense.belongsToUser(userId))
            .filter(expense -> isDateInPeriod(expense.getDate(), startDate, endDate))
            .map(expense -> expense.getAmount().getValue())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Amount(total);
    }

    public Map<Category, Amount> calculateExpensesByCategory(List<Expense> expenses, String userId,
                                                           LocalDate startDate, LocalDate endDate) {
        validatePeriodCalculationParams(expenses, userId, startDate, endDate);

        return expenses.stream()
            .filter(expense -> expense.belongsToUser(userId))
            .filter(expense -> isDateInPeriod(expense.getDate(), startDate, endDate))
            .collect(Collectors.groupingBy(
                Expense::getCategory,
                Collectors.reducing(
                    new Amount(BigDecimal.ZERO),
                    Expense::getAmount,
                    (amount1, amount2) -> amount1.add(amount2)
                )
            ));
    }

    public Amount calculateMonthlyAverageExpense(List<Expense> expenses, String userId, 
                                               int months, LocalDate referenceDate) {
        if (expenses == null) {
            throw new IllegalArgumentException("Expenses list cannot be null");
        }
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

        Amount totalExpense = calculateTotalExpenseForPeriod(expenses, userId, startDate, endDate);
        BigDecimal average = totalExpense.getValue().divide(
            BigDecimal.valueOf(months), 
            2, 
            RoundingMode.HALF_UP
        );

        return new Amount(average);
    }

    public SpendingAnalysis analyzeSpendingTrend(List<Expense> expenses, String userId, 
                                               YearMonth currentMonth) {
        if (expenses == null) {
            throw new IllegalArgumentException("Expenses list cannot be null");
        }
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

        Amount currentExpense = calculateTotalExpenseForPeriod(expenses, userId, currentStart, currentEnd);
        Amount previousExpense = calculateTotalExpenseForPeriod(expenses, userId, previousStart, previousEnd);

        BigDecimal changePercentage = calculateChangePercentage(
            previousExpense.getValue(), 
            currentExpense.getValue()
        );

        return new SpendingAnalysis(
            previousExpense,
            currentExpense,
            changePercentage,
            currentMonth
        );
    }

    public boolean isExcessiveExpense(Amount amount, Amount userAverageExpense, 
                                    BigDecimal excessThreshold) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (userAverageExpense == null) {
            throw new IllegalArgumentException("User average expense cannot be null");
        }
        if (excessThreshold == null || excessThreshold.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Excess threshold must be positive");
        }

        BigDecimal threshold = userAverageExpense.getValue().multiply(excessThreshold);
        return amount.getValue().compareTo(threshold) > 0;
    }

    public Map<Category, BigDecimal> calculateCategoryPercentages(List<Expense> expenses, String userId,
                                                                LocalDate startDate, LocalDate endDate) {
        validatePeriodCalculationParams(expenses, userId, startDate, endDate);

        Map<Category, Amount> categoryTotals = calculateExpensesByCategory(expenses, userId, startDate, endDate);
        Amount totalExpense = calculateTotalExpenseForPeriod(expenses, userId, startDate, endDate);

        if (totalExpense.getValue().compareTo(BigDecimal.ZERO) == 0) {
            return categoryTotals.keySet().stream()
                .collect(Collectors.toMap(
                    category -> category,
                    category -> BigDecimal.ZERO
                ));
        }

        return categoryTotals.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().getValue()
                    .divide(totalExpense.getValue(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
            ));
    }

    public CategoryAnalysis analyzeHighestSpendingCategory(List<Expense> expenses, String userId,
                                                         LocalDate startDate, LocalDate endDate) {
        validatePeriodCalculationParams(expenses, userId, startDate, endDate);

        Map<Category, Amount> categoryTotals = calculateExpensesByCategory(expenses, userId, startDate, endDate);

        if (categoryTotals.isEmpty()) {
            return new CategoryAnalysis(null, new Amount(BigDecimal.ZERO), BigDecimal.ZERO);
        }

        Map.Entry<Category, Amount> highestEntry = categoryTotals.entrySet().stream()
            .max(Map.Entry.<Category, Amount>comparingByValue(
                (amount1, amount2) -> amount1.getValue().compareTo(amount2.getValue())
            ))
            .orElseThrow(() -> new IllegalStateException("Should not happen with non-empty map"));

        Amount totalExpense = calculateTotalExpenseForPeriod(expenses, userId, startDate, endDate);
        BigDecimal percentage = totalExpense.getValue().compareTo(BigDecimal.ZERO) == 0 ?
            BigDecimal.ZERO :
            highestEntry.getValue().getValue()
                .divide(totalExpense.getValue(), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);

        return new CategoryAnalysis(
            highestEntry.getKey(),
            highestEntry.getValue(),
            percentage
        );
    }

    public BudgetUtilization calculateBudgetUtilization(List<Expense> expenses, String userId,
                                                      Amount budgetLimit, LocalDate startDate,
                                                      LocalDate endDate) {
        validatePeriodCalculationParams(expenses, userId, startDate, endDate);
        if (budgetLimit == null) {
            throw new IllegalArgumentException("Budget limit cannot be null");
        }

        Amount actualExpense = calculateTotalExpenseForPeriod(expenses, userId, startDate, endDate);
        
        BigDecimal utilizationPercentage = budgetLimit.getValue().compareTo(BigDecimal.ZERO) == 0 ?
            BigDecimal.valueOf(100) :
            actualExpense.getValue()
                .divide(budgetLimit.getValue(), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);

        boolean isOverBudget = actualExpense.getValue().compareTo(budgetLimit.getValue()) > 0;
        Amount remaining = isOverBudget ? 
            new Amount(BigDecimal.ZERO) : 
            budgetLimit.subtract(actualExpense);

        return new BudgetUtilization(
            budgetLimit,
            actualExpense,
            remaining,
            utilizationPercentage,
            isOverBudget
        );
    }


    private void validatePeriodCalculationParams(List<Expense> expenses, String userId,
                                               LocalDate startDate, LocalDate endDate) {
        if (expenses == null) {
            throw new IllegalArgumentException("Expenses list cannot be null");
        }
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

    private boolean isDateInPeriod(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return (date.isEqual(startDate) || date.isAfter(startDate)) &&
               (date.isEqual(endDate) || date.isBefore(endDate));
    }

    private BigDecimal calculateChangePercentage(BigDecimal previousAmount, BigDecimal currentAmount) {
        if (previousAmount.compareTo(BigDecimal.ZERO) == 0) {
            return currentAmount.compareTo(BigDecimal.ZERO) == 0 ? 
                BigDecimal.ZERO : 
                BigDecimal.valueOf(100);
        }

        return currentAmount.subtract(previousAmount)
            .divide(previousAmount, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(2, RoundingMode.HALF_UP);
    }

    public static class SpendingAnalysis {
        private final Amount previousMonthExpense;
        private final Amount currentMonthExpense;
        private final BigDecimal changePercentage;
        private final YearMonth analysisMonth;

        public SpendingAnalysis(Amount previousMonthExpense, Amount currentMonthExpense,
                              BigDecimal changePercentage, YearMonth analysisMonth) {
            this.previousMonthExpense = previousMonthExpense;
            this.currentMonthExpense = currentMonthExpense;
            this.changePercentage = changePercentage;
            this.analysisMonth = analysisMonth;
        }

        public Amount getPreviousMonthExpense() {
            return previousMonthExpense;
        }

        public Amount getCurrentMonthExpense() {
            return currentMonthExpense;
        }

        public BigDecimal getChangePercentage() {
            return changePercentage;
        }

        public YearMonth getAnalysisMonth() {
            return analysisMonth;
        }

        public boolean hasIncreasedSpending() {
            return changePercentage.compareTo(BigDecimal.ZERO) > 0;
        }

        public boolean hasDecreasedSpending() {
            return changePercentage.compareTo(BigDecimal.ZERO) < 0;
        }

        public Amount getChangeAmount() {
            BigDecimal currentValue = currentMonthExpense.getValue();
            BigDecimal previousValue = previousMonthExpense.getValue();
            BigDecimal difference = currentValue.subtract(previousValue);
            return new Amount(difference.abs());
        }

        @Override
        public String toString() {
            return "SpendingAnalysis{" +
                    "previousMonthExpense=" + previousMonthExpense +
                    ", currentMonthExpense=" + currentMonthExpense +
                    ", changePercentage=" + changePercentage +
                    ", analysisMonth=" + analysisMonth +
                    '}';
        }
    }

    public static class CategoryAnalysis {
        private final Category category;
        private final Amount amount;
        private final BigDecimal percentageOfTotal;

        public CategoryAnalysis(Category category, Amount amount, BigDecimal percentageOfTotal) {
            this.category = category;
            this.amount = amount;
            this.percentageOfTotal = percentageOfTotal;
        }

        public Category getCategory() {
            return category;
        }

        public Amount getAmount() {
            return amount;
        }

        public BigDecimal getPercentageOfTotal() {
            return percentageOfTotal;
        }

        @Override
        public String toString() {
            return "CategoryAnalysis{" +
                    "category=" + category +
                    ", amount=" + amount +
                    ", percentageOfTotal=" + percentageOfTotal +
                    '}';
        }
    }

    public static class BudgetUtilization {
        private final Amount budgetLimit;
        private final Amount actualExpense;
        private final Amount remaining;
        private final BigDecimal utilizationPercentage;
        private final boolean isOverBudget;

        public BudgetUtilization(Amount budgetLimit, Amount actualExpense, Amount remaining,
                               BigDecimal utilizationPercentage, boolean isOverBudget) {
            this.budgetLimit = budgetLimit;
            this.actualExpense = actualExpense;
            this.remaining = remaining;
            this.utilizationPercentage = utilizationPercentage;
            this.isOverBudget = isOverBudget;
        }

        public Amount getBudgetLimit() {
            return budgetLimit;
        }

        public Amount getActualExpense() {
            return actualExpense;
        }

        public Amount getRemaining() {
            return remaining;
        }

        public BigDecimal getUtilizationPercentage() {
            return utilizationPercentage;
        }

        public boolean isOverBudget() {
            return isOverBudget;
        }

        public Amount getOverspendAmount() {
            return isOverBudget ? actualExpense.subtract(budgetLimit) : new Amount(BigDecimal.ZERO);
        }

        @Override
        public String toString() {
            return "BudgetUtilization{" +
                    "budgetLimit=" + budgetLimit +
                    ", actualExpense=" + actualExpense +
                    ", remaining=" + remaining +
                    ", utilizationPercentage=" + utilizationPercentage +
                    ", isOverBudget=" + isOverBudget +
                    '}';
        }
    }
}