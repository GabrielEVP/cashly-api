package com.cashly.cashly_api.expenses.application.ports;

import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.valueobjects.Category;
import com.cashly.cashly_api.expenses.domain.valueobjects.ExpenseId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {
    
    Expense save(Expense expense);
    
    Optional<Expense> findById(ExpenseId id);
    
    List<Expense> findByUserId(String userId);
    
    List<Expense> findByUserIdAndDateRange(String userId, LocalDateTime start, LocalDateTime end);
    
    List<Expense> findByUserIdAndCategory(String userId, Category category);
    
    void deleteById(ExpenseId id);
    
    boolean existsById(ExpenseId id);
}