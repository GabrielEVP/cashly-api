package com.cashly.cashly_api.incomes.infrastructure.persistence;

import com.cashly.cashly_api.incomes.application.ports.IncomeRepository;
import com.cashly.cashly_api.incomes.domain.entities.Income;
import com.cashly.cashly_api.incomes.domain.valueobjects.Amount;
import com.cashly.cashly_api.incomes.domain.valueobjects.Category;
import com.cashly.cashly_api.incomes.domain.valueobjects.IncomeId;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public Amount calculateTotalIncomeForPeriod(String userId, LocalDate startDate, LocalDate endDate) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999999999);
        
        BigDecimal total = springDataRepository.calculateTotalIncomeForPeriod(userId, startDateTime, endDateTime);
        return new Amount(total != null ? total : BigDecimal.ZERO);
    }

    @Override
    public Map<Category, Amount> calculateIncomeByCategory(String userId, LocalDate startDate, LocalDate endDate) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999999999);
        
        List<Object[]> results = springDataRepository.calculateIncomeByCategory(userId, startDateTime, endDateTime);
        Map<Category, Amount> incomeByCategory = new HashMap<>();
        
        for (Object[] result : results) {
            String categoryString = (String) result[0];
            BigDecimal amount = (BigDecimal) result[1];
            
            try {
                Category category = new Category(categoryString);
                incomeByCategory.put(category, new Amount(amount != null ? amount : BigDecimal.ZERO));
            } catch (IllegalArgumentException e) {
                // Skip invalid categories - this maintains data integrity
                continue;
            }
        }
        
        return incomeByCategory;
    }

    @Override
    public Map<Category, Double> calculateCategoryPercentages(String userId, LocalDate startDate, LocalDate endDate) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        Map<Category, Amount> incomeByCategory = calculateIncomeByCategory(userId, startDate, endDate);
        Amount totalIncome = calculateTotalIncomeForPeriod(userId, startDate, endDate);
        
        Map<Category, Double> percentages = new HashMap<>();
        
        if (totalIncome.getValue().compareTo(BigDecimal.ZERO) == 0) {
            return percentages; // Return empty map if no income
        }
        
        for (Map.Entry<Category, Amount> entry : incomeByCategory.entrySet()) {
            BigDecimal percentage = entry.getValue().getValue()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalIncome.getValue(), 2, java.math.RoundingMode.HALF_UP);
            percentages.put(entry.getKey(), percentage.doubleValue());
        }
        
        return percentages;
    }
}