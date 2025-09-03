package com.cashly.cashly_api.expenses.infrastructure.persistence;

import com.cashly.cashly_api.expenses.application.ports.ExpenseRepository;
import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.valueobjects.Category;
import com.cashly.cashly_api.expenses.domain.valueobjects.ExpenseId;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaExpenseRepository implements ExpenseRepository {

    private final SpringDataExpenseRepository springDataRepository;

    public JpaExpenseRepository(SpringDataExpenseRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Expense save(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense cannot be null");
        }

        ExpenseEntity entity = ExpenseEntity.fromDomain(expense);
        ExpenseEntity savedEntity = springDataRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Expense> findById(ExpenseId id) {
        if (id == null) {
            throw new IllegalArgumentException("Expense ID cannot be null");
        }

        Optional<ExpenseEntity> entity = springDataRepository.findById(id.getValue().toString());
        return entity.map(ExpenseEntity::toDomain);
    }

    @Override
    public List<Expense> findByUserId(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }

        List<ExpenseEntity> entities = springDataRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return entities.stream()
                .map(ExpenseEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Expense> findByUserIdAndDateRange(String userId, LocalDateTime start, LocalDateTime end) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (start == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (end == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }

        List<ExpenseEntity> entities = springDataRepository
                .findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, start, end);
        return entities.stream()
                .map(ExpenseEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Expense> findByUserIdAndCategory(String userId, Category category) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        List<ExpenseEntity> entities = springDataRepository
                .findByUserIdAndCategoryOrderByCreatedAtDesc(userId, category.getValue());
        return entities.stream()
                .map(ExpenseEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(ExpenseId id) {
        if (id == null) {
            throw new IllegalArgumentException("Expense ID cannot be null");
        }

        springDataRepository.deleteById(id.getValue().toString());
    }

    @Override
    public boolean existsById(ExpenseId id) {
        if (id == null) {
            throw new IllegalArgumentException("Expense ID cannot be null");
        }

        return springDataRepository.existsById(id.getValue().toString());
    }
}