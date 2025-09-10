package com.cashly.cashly_api.expenses.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpringDataExpenseRepository extends JpaRepository<ExpenseEntity, String> {

    List<ExpenseEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    List<ExpenseEntity> findByUserIdAndCategoryOrderByCreatedAtDesc(String userId, String category);

    List<ExpenseEntity> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(String userId, 
                                                                           LocalDateTime startDate, 
                                                                           LocalDateTime endDate);
    
    // Heavy computation queries for database-optimized operations
    
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e " +
           "WHERE e.userId = :userId " +
           "AND e.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalExpenseForPeriod(@Param("userId") String userId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT e.category, COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e " +
           "WHERE e.userId = :userId " +
           "AND e.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY e.category")
    List<Object[]> calculateExpensesByCategory(@Param("userId") String userId,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT e.category FROM ExpenseEntity e " +
           "WHERE e.userId = :userId " +
           "AND e.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY e.category " +
           "ORDER BY SUM(e.amount) DESC " +
           "LIMIT 1")
    String findHighestSpendingCategory(@Param("userId") String userId,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
}