package com.cashly.cashly_api.expenses.infrastructure.persistence;

import com.cashly.cashly_api.expenses.application.ports.ExpenseRepository;
import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.valueobjects.Amount;
import com.cashly.cashly_api.expenses.domain.valueobjects.Category;
import com.cashly.cashly_api.expenses.domain.valueobjects.ExpenseId;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public Amount calculateTotalExpenseForPeriod(String userId, LocalDate startDate, LocalDate endDate) {
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
        
        BigDecimal total = springDataRepository.calculateTotalExpenseForPeriod(userId, startDateTime, endDateTime);
        return new Amount(total != null ? total : BigDecimal.ZERO);
    }

    @Override
    public Map<Category, Amount> calculateExpensesByCategory(String userId, LocalDate startDate, LocalDate endDate) {
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
        
        List<Object[]> results = springDataRepository.calculateExpensesByCategory(userId, startDateTime, endDateTime);
        Map<Category, Amount> expensesByCategory = new HashMap<>();
        
        for (Object[] result : results) {
            String categoryString = (String) result[0];
            BigDecimal amount = (BigDecimal) result[1];
            
            try {
                Category category = new Category(categoryString);
                expensesByCategory.put(category, new Amount(amount != null ? amount : BigDecimal.ZERO));
            } catch (IllegalArgumentException e) {
                // Skip invalid categories - this maintains data integrity
                continue;
            }
        }
        
        return expensesByCategory;
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

        Map<Category, Amount> expensesByCategory = calculateExpensesByCategory(userId, startDate, endDate);
        Amount totalExpenses = calculateTotalExpenseForPeriod(userId, startDate, endDate);
        
        Map<Category, Double> percentages = new HashMap<>();
        
        if (totalExpenses.getValue().compareTo(BigDecimal.ZERO) == 0) {
            return percentages; // Return empty map if no expenses
        }
        
        for (Map.Entry<Category, Amount> entry : expensesByCategory.entrySet()) {
            BigDecimal percentage = entry.getValue().getValue()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalExpenses.getValue(), 2, java.math.RoundingMode.HALF_UP);
            percentages.put(entry.getKey(), percentage.doubleValue());
        }
        
        return percentages;
    }

    @Override
    public Category analyzeHighestSpendingCategory(String userId, LocalDate startDate, LocalDate endDate) {
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
        
        String categoryString = springDataRepository.findHighestSpendingCategory(userId, startDateTime, endDateTime);
        
        if (categoryString == null) {
            return null; // No expenses found
        }
        
        try {
            return new Category(categoryString);
        } catch (IllegalArgumentException e) {
            return null; // Invalid category found, return null
        }
    }
}