package com.cashly.cashly_api.incomes.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository interface for IncomeEntity.
 * Provides CRUD operations and custom query methods for income persistence.
 */
@Repository
public interface SpringDataIncomeRepository extends JpaRepository<IncomeEntity, String> {

    /**
     * Finds all incomes for a specific user, ordered by creation date descending.
     * @param userId the user ID
     * @return a list of income entities for the user
     */
    List<IncomeEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    /**
     * Finds all incomes for a user in a specific category, ordered by creation date descending.
     * @param userId the user ID
     * @param category the income category
     * @return a list of income entities in the specified category
     */
    List<IncomeEntity> findByUserIdAndCategoryOrderByCreatedAtDesc(String userId, String category);

    /**
     * Finds all incomes for a user within a date range, ordered by creation date descending.
     * @param userId the user ID
     * @param startDate the start date/time (inclusive)
     * @param endDate the end date/time (inclusive)
     * @return a list of income entities within the date range
     */
    List<IncomeEntity> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(String userId, 
                                                                           LocalDateTime startDate, 
                                                                           LocalDateTime endDate);
}