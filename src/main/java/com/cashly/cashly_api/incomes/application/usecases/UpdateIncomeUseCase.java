package com.cashly.cashly_api.incomes.application.usecases;

import com.cashly.cashly_api.incomes.application.dto.IncomeResponse;
import com.cashly.cashly_api.incomes.application.dto.UpdateIncomeRequest;
import com.cashly.cashly_api.incomes.application.ports.IncomeRepository;
import com.cashly.cashly_api.incomes.domain.entities.Income;
import com.cashly.cashly_api.incomes.domain.valueobjects.Category;
import com.cashly.cashly_api.incomes.domain.valueobjects.Description;
import com.cashly.cashly_api.incomes.domain.valueobjects.IncomeId;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for updating an existing Income.
 * Orchestrates the update of an Income entity using domain objects.
 * Follows Clean Architecture principles by delegating business logic to the domain layer.
 */
@Service
public class UpdateIncomeUseCase {
    
    private final IncomeRepository incomeRepository;
    
    public UpdateIncomeUseCase(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }
    
    /**
     * Updates an existing Income based on the provided request.
     * 
     * @param incomeIdString the string representation of the income ID
     * @param request the update income request containing the fields to update
     * @return the updated income as an IncomeResponse
     * @throws IllegalArgumentException if any validation fails
     * @throws RuntimeException if the income is not found
     */
    public IncomeResponse execute(String incomeIdString, UpdateIncomeRequest request) {
        validateInput(incomeIdString, request);
        
        IncomeId incomeId = parseIncomeId(incomeIdString);
        
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found with ID: " + incomeIdString));
        
        // Update fields if they are provided (not null)
        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            Description newDescription = new Description(request.getDescription());
            income.updateDescription(newDescription);
        }
        
        if (request.getCategory() != null && !request.getCategory().trim().isEmpty()) {
            Category newCategory = new Category(request.getCategory());
            income.updateCategory(newCategory);
        }
        
        Income updatedIncome = incomeRepository.save(income);
        
        return mapToResponse(updatedIncome);
    }
    
    private void validateInput(String incomeIdString, UpdateIncomeRequest request) {
        if (incomeIdString == null) {
            throw new IllegalArgumentException("Income ID cannot be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Update income request cannot be null");
        }
    }
    
    private IncomeId parseIncomeId(String incomeIdString) {
        try {
            UUID uuid = UUID.fromString(incomeIdString);
            return new IncomeId(uuid);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for income ID: " + incomeIdString, e);
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