package com.cashly.cashly_api.incomes.application.ports;

import com.cashly.cashly_api.incomes.domain.entities.Income;
import com.cashly.cashly_api.incomes.domain.valueobjects.Category;
import com.cashly.cashly_api.incomes.domain.valueobjects.IncomeId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Port interface for Income repository operations.
 * This interface defines the contract that infrastructure layer must implement
 * to provide persistence capabilities for Income entities.
 */
public interface IncomeRepository {
    
    /**
     * Saves an Income entity.
     * @param income the income to save
     * @return the saved income
     */
    Income save(Income income);
    
    /**
     * Finds an Income by its ID.
     * @param id the income ID
     * @return an Optional containing the income if found, empty otherwise
     */
    Optional<Income> findById(IncomeId id);
    
    /**
     * Finds all incomes for a specific user.
     * @param userId the user ID
     * @return a list of incomes for the user
     */
    List<Income> findByUserId(String userId);
    
    /**
     * Finds all incomes for a user within a date range.
     * @param userId the user ID
     * @param start the start date/time (inclusive)
     * @param end the end date/time (inclusive)
     * @return a list of incomes within the date range
     */
    List<Income> findByUserIdAndDateRange(String userId, LocalDateTime start, LocalDateTime end);
    
    /**
     * Finds all incomes for a user in a specific category.
     * @param userId the user ID
     * @param category the income category
     * @return a list of incomes in the specified category
     */
    List<Income> findByUserIdAndCategory(String userId, Category category);
    
    /**
     * Deletes an Income by its ID.
     * @param id the income ID
     */
    void deleteById(IncomeId id);
    
    /**
     * Checks if an Income exists by its ID.
     * @param id the income ID
     * @return true if the income exists, false otherwise
     */
    boolean existsById(IncomeId id);
}