package com.cashly.cashly_api.incomes.infrastructure.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("SpringDataIncomeRepository Unit Tests")
class SpringDataIncomeRepositoryUnitTest {

    @Autowired
    private SpringDataIncomeRepository repository;

    @Test
    @DisplayName("should_SaveAndFindIncomeEntity_When_ValidEntityProvided")
    void should_SaveAndFindIncomeEntity_When_ValidEntityProvided() {
        // Arrange
        IncomeEntity entity = createSampleIncomeEntity("1", "user123", "SALARY");
        
        // Act
        IncomeEntity savedEntity = repository.save(entity);
        Optional<IncomeEntity> foundEntity = repository.findById("1");
        
        // Assert
        assertThat(savedEntity).isNotNull();
        assertThat(foundEntity).isPresent();
        assertThat(foundEntity.get().getId()).isEqualTo("1");
        assertThat(foundEntity.get().getUserId()).isEqualTo("user123");
        assertThat(foundEntity.get().getCategory()).isEqualTo("SALARY");
    }

    @Test
    @DisplayName("should_FindIncomesByUserId_When_UserHasIncomes")
    void should_FindIncomesByUserId_When_UserHasIncomes() {
        // Arrange
        IncomeEntity income1 = createSampleIncomeEntity("1", "user123", "SALARY");
        IncomeEntity income2 = createSampleIncomeEntity("2", "user123", "BUSINESS");
        IncomeEntity income3 = createSampleIncomeEntity("3", "user456", "SALARY");
        
        repository.save(income1);
        repository.save(income2);
        repository.save(income3);
        
        // Act
        List<IncomeEntity> userIncomes = repository.findByUserIdOrderByCreatedAtDesc("user123");
        
        // Assert
        assertThat(userIncomes).hasSize(2);
        assertThat(userIncomes).extracting(IncomeEntity::getUserId).containsOnly("user123");
        assertThat(userIncomes).extracting(IncomeEntity::getId).containsExactly("2", "1"); // Ordered by createdAt desc
    }

    @Test
    @DisplayName("should_FindIncomesByUserIdAndCategory_When_FilteringByCategory")
    void should_FindIncomesByUserIdAndCategory_When_FilteringByCategory() {
        // Arrange
        IncomeEntity income1 = createSampleIncomeEntity("1", "user123", "SALARY");
        IncomeEntity income2 = createSampleIncomeEntity("2", "user123", "BUSINESS");
        IncomeEntity income3 = createSampleIncomeEntity("3", "user123", "SALARY");
        
        repository.save(income1);
        repository.save(income2);
        repository.save(income3);
        
        // Act
        List<IncomeEntity> salaryIncomes = repository.findByUserIdAndCategoryOrderByCreatedAtDesc("user123", "SALARY");
        
        // Assert
        assertThat(salaryIncomes).hasSize(2);
        assertThat(salaryIncomes).extracting(IncomeEntity::getCategory).containsOnly("SALARY");
        assertThat(salaryIncomes).extracting(IncomeEntity::getId).containsExactly("3", "1"); // Ordered by createdAt desc
    }

    @Test
    @DisplayName("should_FindIncomesByUserIdAndDateRange_When_FilteringByDateRange")
    void should_FindIncomesByUserIdAndDateRange_When_FilteringByDateRange() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(10);
        LocalDateTime endDate = now.minusDays(1);
        
        IncomeEntity income1 = createSampleIncomeEntity("1", "user123", "SALARY");
        income1.setCreatedAt(now.minusDays(15)); // Outside range
        
        IncomeEntity income2 = createSampleIncomeEntity("2", "user123", "BUSINESS");
        income2.setCreatedAt(now.minusDays(5)); // Inside range
        
        IncomeEntity income3 = createSampleIncomeEntity("3", "user123", "INVESTMENT");
        income3.setCreatedAt(now.minusDays(3)); // Inside range
        
        repository.save(income1);
        repository.save(income2);
        repository.save(income3);
        
        // Act
        List<IncomeEntity> incomesInRange = repository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            "user123", startDate, endDate);
        
        // Assert
        assertThat(incomesInRange).hasSize(2);
        assertThat(incomesInRange).extracting(IncomeEntity::getId).containsExactly("3", "2");
    }

    @Test
    @DisplayName("should_ReturnEmptyList_When_NoIncomesFoundForUser")
    void should_ReturnEmptyList_When_NoIncomesFoundForUser() {
        // Arrange
        IncomeEntity income = createSampleIncomeEntity("1", "user123", "SALARY");
        repository.save(income);
        
        // Act
        List<IncomeEntity> userIncomes = repository.findByUserIdOrderByCreatedAtDesc("nonexistent");
        
        // Assert
        assertThat(userIncomes).isEmpty();
    }

    @Test
    @DisplayName("should_ReturnEmptyList_When_NoIncomesFoundForCategory")
    void should_ReturnEmptyList_When_NoIncomesFoundForCategory() {
        // Arrange
        IncomeEntity income = createSampleIncomeEntity("1", "user123", "SALARY");
        repository.save(income);
        
        // Act
        List<IncomeEntity> incomes = repository.findByUserIdAndCategoryOrderByCreatedAtDesc("user123", "INVESTMENT");
        
        // Assert
        assertThat(incomes).isEmpty();
    }

    @Test
    @DisplayName("should_DeleteIncome_When_ValidIdProvided")
    void should_DeleteIncome_When_ValidIdProvided() {
        // Arrange
        IncomeEntity income = createSampleIncomeEntity("1", "user123", "SALARY");
        repository.save(income);
        
        // Act
        repository.deleteById("1");
        Optional<IncomeEntity> deletedIncome = repository.findById("1");
        
        // Assert
        assertThat(deletedIncome).isNotPresent();
    }

    @Test
    @DisplayName("should_CheckExistence_When_ValidIdProvided")
    void should_CheckExistence_When_ValidIdProvided() {
        // Arrange
        IncomeEntity income = createSampleIncomeEntity("1", "user123", "SALARY");
        repository.save(income);
        
        // Act & Assert
        assertThat(repository.existsById("1")).isTrue();
        assertThat(repository.existsById("nonexistent")).isFalse();
    }

    private IncomeEntity createSampleIncomeEntity(String id, String userId, String category) {
        IncomeEntity entity = new IncomeEntity();
        entity.setId(id);
        entity.setAmount(new BigDecimal("1000.00"));
        entity.setDescription("Test income");
        entity.setCategory(category);
        entity.setDate(LocalDate.now());
        entity.setUserId(userId);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}