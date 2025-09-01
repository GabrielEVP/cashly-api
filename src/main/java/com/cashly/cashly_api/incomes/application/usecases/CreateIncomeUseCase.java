package com.cashly.cashly_api.incomes.application.usecases;

import com.cashly.cashly_api.incomes.application.dto.CreateIncomeRequest;
import com.cashly.cashly_api.incomes.application.dto.IncomeResponse;
import com.cashly.cashly_api.incomes.application.ports.IncomeRepository;
import com.cashly.cashly_api.incomes.domain.entities.Income;
import com.cashly.cashly_api.incomes.domain.valueobjects.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Use case for creating a new Income.
 * Orchestrates the creation of an Income entity using domain objects.
 * Follows Clean Architecture principles by delegating business logic to the domain layer.
 */
@Service
public class CreateIncomeUseCase {
    
    private final IncomeRepository incomeRepository;
    
    public CreateIncomeUseCase(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }
    
    /**
     * Creates a new Income based on the provided request.
     * 
     * @param request the create income request containing the necessary data
     * @return the created income as an IncomeResponse
     * @throws IllegalArgumentException if any validation fails
     */
    public IncomeResponse execute(CreateIncomeRequest request) {
        validateRequest(request);
        
        // Create domain value objects (this will validate business rules)
        IncomeId id = IncomeId.generate();
        Amount amount = new Amount(request.getAmount());
        Description description = new Description(request.getDescription());
        Category category = new Category(request.getCategory());
        LocalDate date = LocalDate.now();
        
        // Create domain entity (this will validate business rules)
        Income income = new Income(id, amount, description, category, date, request.getUserId());
        
        // Save through repository port
        Income savedIncome = incomeRepository.save(income);
        
        // Convert to response DTO
        return mapToResponse(savedIncome);
    }
    
    private void validateRequest(CreateIncomeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Create income request cannot be null");
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
    
    private IncomeResponse mapToResponse(Income income) {
        return new IncomeResponse(
            income.getId().getValue().toString(),
            income.getAmount().getValue(),
            income.getDescription().getValue(),
            income.getCategory().getValue(),
            income.getUserId(),
            income.getCreatedAt(),
            income.getUpdatedAt()
        );
    }
}