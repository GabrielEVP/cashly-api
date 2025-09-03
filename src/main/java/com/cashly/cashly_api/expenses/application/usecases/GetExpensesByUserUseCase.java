package com.cashly.cashly_api.expenses.application.usecases;

import com.cashly.cashly_api.expenses.application.dto.ExpenseResponse;
import com.cashly.cashly_api.expenses.application.ports.ExpenseRepository;
import com.cashly.cashly_api.expenses.domain.entities.Expense;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetExpensesByUserUseCase {
    
    private final ExpenseRepository expenseRepository;
    
    public GetExpensesByUserUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
    
    public List<ExpenseResponse> execute(String userId) {
        validateUserId(userId);
        
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        
        return expenses.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private void validateUserId(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty or blank");
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