package com.cashly.cashly_api.expenses.application.usecases;

import com.cashly.cashly_api.expenses.application.ports.ExpenseRepository;
import com.cashly.cashly_api.expenses.domain.valueobjects.ExpenseId;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteExpenseUseCase {
    
    private final ExpenseRepository expenseRepository;
    
    public DeleteExpenseUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
    
    public void execute(String expenseIdString) {
        validateExpenseId(expenseIdString);
        
        ExpenseId expenseId = parseExpenseId(expenseIdString);
        
        expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found with ID: " + expenseIdString));
        
        expenseRepository.deleteById(expenseId);
    }
    
    private void validateExpenseId(String expenseIdString) {
        if (expenseIdString == null) {
            throw new IllegalArgumentException("Expense ID cannot be null");
        }
    }
    
    private ExpenseId parseExpenseId(String expenseIdString) {
        try {
            UUID uuid = UUID.fromString(expenseIdString);
            return new ExpenseId(uuid);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for expense ID: " + expenseIdString, e);
        }
    }
}