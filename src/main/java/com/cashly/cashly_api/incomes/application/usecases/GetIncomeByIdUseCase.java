package com.cashly.cashly_api.incomes.application.usecases;

import com.cashly.cashly_api.incomes.application.dto.IncomeResponse;
import com.cashly.cashly_api.incomes.application.ports.IncomeRepository;
import com.cashly.cashly_api.incomes.domain.entities.Income;
import com.cashly.cashly_api.incomes.domain.valueobjects.IncomeId;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for retrieving a specific income by its ID.
 * Orchestrates the retrieval of an Income entity and converts it to response DTO.
 * Follows Clean Architecture principles by using the repository port interface.
 */
@Service
public class GetIncomeByIdUseCase {
    
    private final IncomeRepository incomeRepository;
    
    public GetIncomeByIdUseCase(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }
    
    /**
     * Retrieves a specific income by its ID.
     * 
     * @param incomeIdString the string representation of the income ID
     * @return the IncomeResponse object representing the found income
     * @throws IllegalArgumentException if the incomeIdString is null or invalid UUID
     * @throws RuntimeException if the income is not found
     */
    public IncomeResponse execute(String incomeIdString) {
        validateIncomeId(incomeIdString);
        
        IncomeId incomeId = parseIncomeId(incomeIdString);
        
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found with ID: " + incomeIdString));
        
        return mapToResponse(income);
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