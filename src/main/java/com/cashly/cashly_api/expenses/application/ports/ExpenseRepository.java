package com.cashly.cashly_api.expenses.application.ports;

import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.valueobjects.Amount;
import com.cashly.cashly_api.expenses.domain.valueobjects.Category;
import com.cashly.cashly_api.expenses.domain.valueobjects.ExpenseId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExpenseRepository {
    
    Expense save(Expense expense);
    
    Optional<Expense> findById(ExpenseId id);
    
    List<Expense> findByUserId(String userId);
    
    List<Expense> findByUserIdAndDateRange(String userId, LocalDateTime start, LocalDateTime end);
    
    List<Expense> findByUserIdAndCategory(String userId, Category category);
    
    void deleteById(ExpenseId id);
    
    boolean existsById(ExpenseId id);
    
    // Heavy computation methods - optimized for database processing
    
    /**
     * Calculates the total expense amount for a user within a date range.
     * Uses database aggregation for optimal performance.
     * @param userId the user ID
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return the total expense amount
     */
    Amount calculateTotalExpenseForPeriod(String userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Calculates expense amounts grouped by category for a user within a date range.
     * Uses database aggregation for optimal performance.
     * @param userId the user ID
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return a map of categories to their total expense amounts
     */
    Map<Category, Amount> calculateExpensesByCategory(String userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Calculates the percentage distribution of expenses by category for a user within a date range.
     * Uses database aggregation for optimal performance.
     * @param userId the user ID
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return a map of categories to their percentage of total expenses
     */
    Map<Category, Double> calculateCategoryPercentages(String userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Analyzes and returns the category with the highest spending for a user within a date range.
     * Uses database aggregation for optimal performance.
     * @param userId the user ID
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return the category with the highest spending, or null if no expenses found
     */
    Category analyzeHighestSpendingCategory(String userId, LocalDate startDate, LocalDate endDate);
}