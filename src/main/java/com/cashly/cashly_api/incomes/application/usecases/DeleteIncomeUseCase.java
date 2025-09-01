package com.cashly.cashly_api.incomes.application.usecases;

import com.cashly.cashly_api.incomes.application.ports.IncomeRepository;
import com.cashly.cashly_api.incomes.domain.valueobjects.IncomeId;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for deleting an existing Income.
 * Orchestrates the deletion of an Income entity.
 * Follows Clean Architecture principles by using the repository port interface.
 */
@Service
public class DeleteIncomeUseCase {
    
    private final IncomeRepository incomeRepository;
    
    public DeleteIncomeUseCase(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }
    
    /**
     * Deletes an existing Income by its ID.
     * 
     * @param incomeIdString the string representation of the income ID
     * @throws IllegalArgumentException if the incomeIdString is null or invalid UUID
     * @throws RuntimeException if the income is not found
     */
    public void execute(String incomeIdString) {
        validateIncomeId(incomeIdString);
        
        IncomeId incomeId = parseIncomeId(incomeIdString);
        
        // Verify income exists before deleting
        incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found with ID: " + incomeIdString));
        
        incomeRepository.deleteById(incomeId);
    }
    
    private void validateIncomeId(String incomeIdString) {
        if (incomeIdString == null) {
            throw new IllegalArgumentException("Income ID cannot be null");
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
}