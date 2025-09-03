package com.cashly.cashly_api.expenses.application.usecases;

import com.cashly.cashly_api.expenses.application.dto.ExpenseResponse;
import com.cashly.cashly_api.expenses.application.dto.UpdateExpenseRequest;
import com.cashly.cashly_api.expenses.application.ports.ExpenseRepository;
import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.valueobjects.Amount;
import com.cashly.cashly_api.expenses.domain.valueobjects.Category;
import com.cashly.cashly_api.expenses.domain.valueobjects.Description;
import com.cashly.cashly_api.expenses.domain.valueobjects.ExpenseId;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateExpenseUseCase {
    
    private final ExpenseRepository expenseRepository;
    
    public UpdateExpenseUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
    
    public ExpenseResponse execute(String expenseIdString, UpdateExpenseRequest request) {
        validateInput(expenseIdString, request);
        
        ExpenseId expenseId = parseExpenseId(expenseIdString);
        
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found with ID: " + expenseIdString));
        
        if (request.getAmount() != null) {
            Amount newAmount = new Amount(request.getAmount());
            expense.updateAmount(newAmount);
        }
        
        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            Description newDescription = new Description(request.getDescription());
            expense.updateDescription(newDescription);
        }
        
        if (request.getCategory() != null && !request.getCategory().trim().isEmpty()) {
            Category newCategory = new Category(request.getCategory());
            expense.updateCategory(newCategory);
        }
        
        Expense updatedExpense = expenseRepository.save(expense);
        
        return mapToResponse(updatedExpense);
    }
    
    private void validateInput(String expenseIdString, UpdateExpenseRequest request) {
        if (expenseIdString == null) {
            throw new IllegalArgumentException("Expense ID cannot be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Update expense request cannot be null");
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