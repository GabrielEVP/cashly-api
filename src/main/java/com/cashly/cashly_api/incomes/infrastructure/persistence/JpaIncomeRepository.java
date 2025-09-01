package com.cashly.cashly_api.incomes.infrastructure.persistence;

import com.cashly.cashly_api.incomes.application.ports.IncomeRepository;
import com.cashly.cashly_api.incomes.domain.entities.Income;
import com.cashly.cashly_api.incomes.domain.valueobjects.Category;
import com.cashly.cashly_api.incomes.domain.valueobjects.IncomeId;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JPA implementation of IncomeRepository port.
 * Adapts between domain Income entities and persistence layer IncomeEntity.
 */
@Repository
public class JpaIncomeRepository implements IncomeRepository {

    private final SpringDataIncomeRepository springDataRepository;

    public JpaIncomeRepository(SpringDataIncomeRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Income save(Income income) {
        if (income == null) {
            throw new IllegalArgumentException("Income cannot be null");
        }

        IncomeEntity entity = IncomeEntity.fromDomain(income);
        IncomeEntity savedEntity = springDataRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Income> findById(IncomeId id) {
        if (id == null) {
            throw new IllegalArgumentException("Income ID cannot be null");
        }

        Optional<IncomeEntity> entity = springDataRepository.findById(id.getValue().toString());
        return entity.map(IncomeEntity::toDomain);
    }

    @Override
    public List<Income> findByUserId(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }

        List<IncomeEntity> entities = springDataRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return entities.stream()
                .map(IncomeEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Income> findByUserIdAndDateRange(String userId, LocalDateTime start, LocalDateTime end) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (start == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (end == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }

        List<IncomeEntity> entities = springDataRepository
                .findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, start, end);
        return entities.stream()
                .map(IncomeEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Income> findByUserIdAndCategory(String userId, Category category) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        List<IncomeEntity> entities = springDataRepository
                .findByUserIdAndCategoryOrderByCreatedAtDesc(userId, category.getValue());
        return entities.stream()
                .map(IncomeEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(IncomeId id) {
        if (id == null) {
            throw new IllegalArgumentException("Income ID cannot be null");
        }

        springDataRepository.deleteById(id.getValue().toString());
    }

    @Override
    public boolean existsById(IncomeId id) {
        if (id == null) {
            throw new IllegalArgumentException("Income ID cannot be null");
        }

        return springDataRepository.existsById(id.getValue().toString());
    }
}