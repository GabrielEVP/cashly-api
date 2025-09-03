package com.cashly.cashly_api.expenses.application.usecases;

import com.cashly.cashly_api.expenses.application.dto.ExpenseResponse;
import com.cashly.cashly_api.expenses.application.ports.ExpenseRepository;
import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.valueobjects.ExpenseId;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetExpenseByIdUseCase {
    
    private final ExpenseRepository expenseRepository;
    
    public GetExpenseByIdUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
    
    public ExpenseResponse execute(String expenseIdString) {
        validateExpenseId(expenseIdString);
        
        ExpenseId expenseId = parseExpenseId(expenseIdString);
        
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found with ID: " + expenseIdString));
        
        return mapToResponse(expense);
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
    
    private ExpenseResponse mapToResponse(Expense expense) {
        return new ExpenseResponse(
            expense.getId().getValue().toString(),
            expense.getAmount().getValue(),
            expense.getDescription().getValue(),
            expense.getCategory().getValue(),
            expense.getUserId(),
            expense.getCreatedAt(),
            expense.getUpdatedAt()
        );
    }
}