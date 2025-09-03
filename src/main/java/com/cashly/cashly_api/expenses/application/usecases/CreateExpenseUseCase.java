package com.cashly.cashly_api.expenses.application.usecases;

import com.cashly.cashly_api.expenses.application.dto.CreateExpenseRequest;
import com.cashly.cashly_api.expenses.application.dto.ExpenseResponse;
import com.cashly.cashly_api.expenses.application.ports.ExpenseRepository;
import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.valueobjects.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CreateExpenseUseCase {
    
    private final ExpenseRepository expenseRepository;
    
    public CreateExpenseUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
    
    public ExpenseResponse execute(CreateExpenseRequest request) {
        validateRequest(request);
        
        ExpenseId id = ExpenseId.generate();
        Amount amount = new Amount(request.getAmount());
        Description description = new Description(request.getDescription());
        Category category = new Category(request.getCategory());
        LocalDate date = LocalDate.now();
        
        Expense expense = new Expense(id, amount, description, category, date, request.getUserId());
        
        Expense savedExpense = expenseRepository.save(expense);
        
        return mapToResponse(savedExpense);
    }
    
    private void validateRequest(CreateExpenseRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Create expense request cannot be null");
        }
        if (request.getAmount() == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (request.getDescription() == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        if (request.getCategory() == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
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