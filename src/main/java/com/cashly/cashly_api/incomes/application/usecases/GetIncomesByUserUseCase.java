package com.cashly.cashly_api.incomes.application.usecases;

import com.cashly.cashly_api.incomes.application.dto.IncomeResponse;
import com.cashly.cashly_api.incomes.application.ports.IncomeRepository;
import com.cashly.cashly_api.incomes.domain.entities.Income;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Use case for retrieving all incomes for a specific user.
 * Orchestrates the retrieval of Income entities and converts them to response DTOs.
 * Follows Clean Architecture principles by using the repository port interface.
 */
@Service
public class GetIncomesByUserUseCase {
    
    private final IncomeRepository incomeRepository;
    
    public GetIncomesByUserUseCase(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }
    
    /**
     * Retrieves all incomes for a specific user.
     * 
     * @param userId the ID of the user whose incomes are to be retrieved
     * @return a list of IncomeResponse objects representing the user's incomes
     * @throws IllegalArgumentException if the userId is null, empty, or blank
     */
    public List<IncomeResponse> execute(String userId) {
        validateUserId(userId);
        
        List<Income> incomes = incomeRepository.findByUserId(userId);
        
        return incomes.stream()
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