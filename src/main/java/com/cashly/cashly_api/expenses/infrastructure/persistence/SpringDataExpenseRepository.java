package com.cashly.cashly_api.expenses.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpringDataExpenseRepository extends JpaRepository<ExpenseEntity, String> {

    List<ExpenseEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    List<ExpenseEntity> findByUserIdAndCategoryOrderByCreatedAtDesc(String userId, String category);

    List<ExpenseEntity> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(String userId, 
                                                                           LocalDateTime startDate, 
                                                                           LocalDateTime endDate);
}