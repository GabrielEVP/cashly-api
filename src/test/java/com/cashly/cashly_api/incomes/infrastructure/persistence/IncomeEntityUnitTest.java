package com.cashly.cashly_api.incomes.infrastructure.persistence;

import com.cashly.cashly_api.incomes.domain.entities.Income;
import com.cashly.cashly_api.incomes.domain.valueobjects.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("IncomeEntity Unit Tests")
class IncomeEntityUnitTest {

    @Test
    @DisplayName("should_CreateIncomeEntityFromDomainIncome_When_ValidIncomeProvided")
    void should_CreateIncomeEntityFromDomainIncome_When_ValidIncomeProvided() {
        // Arrange
        IncomeId incomeId = new IncomeId(UUID.randomUUID());
        Amount amount = new Amount(new BigDecimal("1500.00"));
        Description description = new Description("Monthly salary");
        Category category = new Category("SALARY");
        LocalDate date = LocalDate.now();
        String userId = "user123";
        
        Income domainIncome = new Income(incomeId, amount, description, category, date, userId);
        
        // Act
        IncomeEntity entity = IncomeEntity.fromDomain(domainIncome);
        
        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(incomeId.getValue().toString());
        assertThat(entity.getAmount()).isEqualTo(amount.getValue());
        assertThat(entity.getDescription()).isEqualTo(description.getValue());
        assertThat(entity.getCategory()).isEqualTo(category.getValue());
        assertThat(entity.getDate()).isEqualTo(date);
        assertThat(entity.getUserId()).isEqualTo(userId);
        assertThat(entity.getCreatedAt()).isEqualTo(domainIncome.getCreatedAt());
        assertThat(entity.getUpdatedAt()).isEqualTo(domainIncome.getUpdatedAt());
    }

    @Test
    @DisplayName("should_ConvertToDomainIncome_When_ValidIncomeEntityProvided")
    void should_ConvertToDomainIncome_When_ValidIncomeEntityProvided() {
        // Arrange
        String id = UUID.randomUUID().toString();
        BigDecimal amount = new BigDecimal("2000.50");
        String description = "Business revenue";
        String category = "BUSINESS";
        LocalDate date = LocalDate.now().minusDays(1);
        String userId = "user456";
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
        LocalDateTime updatedAt = LocalDateTime.now();
        
        IncomeEntity entity = new IncomeEntity();
        entity.setId(id);
        entity.setAmount(amount);
        entity.setDescription(description);
        entity.setCategory(category);
        entity.setDate(date);
        entity.setUserId(userId);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);
        
        // Act
        Income domainIncome = entity.toDomain();
        
        // Assert
        assertThat(domainIncome).isNotNull();
        assertThat(domainIncome.getId().getValue().toString()).isEqualTo(id);
        assertThat(domainIncome.getAmount().getValue()).isEqualTo(amount);
        assertThat(domainIncome.getDescription().getValue()).isEqualTo(description);
        assertThat(domainIncome.getCategory().getValue()).isEqualTo(category);
        assertThat(domainIncome.getDate()).isEqualTo(date);
        assertThat(domainIncome.getUserId()).isEqualTo(userId);
        assertThat(domainIncome.getCreatedAt()).isEqualTo(createdAt);
        assertThat(domainIncome.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("should_ThrowException_When_ConvertingEntityWithInvalidCategory")
    void should_ThrowException_When_ConvertingEntityWithInvalidCategory() {
        // Arrange
        IncomeEntity entity = new IncomeEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setAmount(new BigDecimal("1000.00"));
        entity.setDescription("Test income");
        entity.setCategory("INVALID_CATEGORY");
        entity.setDate(LocalDate.now());
        entity.setUserId("user123");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        
        // Act & Assert
        assertThatThrownBy(() -> entity.toDomain())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid category");
    }

    @Test
    @DisplayName("should_ThrowException_When_ConvertingEntityWithNegativeAmount")
    void should_ThrowException_When_ConvertingEntityWithNegativeAmount() {
        // Arrange
        IncomeEntity entity = new IncomeEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setAmount(new BigDecimal("-100.00"));
        entity.setDescription("Test income");
        entity.setCategory("SALARY");
        entity.setDate(LocalDate.now());
        entity.setUserId("user123");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        
        // Act & Assert
        assertThatThrownBy(() -> entity.toDomain())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Amount cannot be negative");
    }

    @Test
    @DisplayName("should_ThrowException_When_FromDomainReceivesNull")
    void should_ThrowException_When_FromDomainReceivesNull() {
        // Act & Assert
        assertThatThrownBy(() -> IncomeEntity.fromDomain(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Income cannot be null");
    }

    @Test
    @DisplayName("should_UpdateEntityFromDomainIncome_When_ValidIncomeProvided")
    void should_UpdateEntityFromDomainIncome_When_ValidIncomeProvided() {
        // Arrange
        String entityId = UUID.randomUUID().toString();
        IncomeEntity existingEntity = new IncomeEntity();
        existingEntity.setId(entityId);
        existingEntity.setAmount(new BigDecimal("1000.00"));
        existingEntity.setDescription("Old description");
        existingEntity.setCategory("SALARY");
        existingEntity.setDate(LocalDate.now());
        existingEntity.setUserId("user123");
        existingEntity.setCreatedAt(LocalDateTime.now().minusHours(1));
        existingEntity.setUpdatedAt(LocalDateTime.now().minusHours(1));

        IncomeId incomeId = new IncomeId(UUID.fromString(entityId));
        Amount newAmount = new Amount(new BigDecimal("1200.00"));
        Description newDescription = new Description("Updated description");
        Category category = new Category("BUSINESS");
        LocalDate date = LocalDate.now();
        String userId = "user123";
        
        Income updatedDomainIncome = new Income(incomeId, newAmount, newDescription, category, date, userId);
        
        // Act
        existingEntity.updateFromDomain(updatedDomainIncome);
        
        // Assert
        assertThat(existingEntity.getId()).isEqualTo(entityId); // ID should not change
        assertThat(existingEntity.getAmount()).isEqualTo(newAmount.getValue());
        assertThat(existingEntity.getDescription()).isEqualTo(newDescription.getValue());
        assertThat(existingEntity.getCategory()).isEqualTo(category.getValue());
        assertThat(existingEntity.getUpdatedAt()).isEqualTo(updatedDomainIncome.getUpdatedAt());
    }

    @Test
    @DisplayName("should_CreateEmptyEntity_When_DefaultConstructorUsed")
    void should_CreateEmptyEntity_When_DefaultConstructorUsed() {
        // Act
        IncomeEntity entity = new IncomeEntity();
        
        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getAmount()).isNull();
        assertThat(entity.getDescription()).isNull();
        assertThat(entity.getCategory()).isNull();
        assertThat(entity.getDate()).isNull();
        assertThat(entity.getUserId()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
    }
}