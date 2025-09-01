package com.cashly.cashly_api.incomes.infrastructure.persistence;

import com.cashly.cashly_api.incomes.domain.entities.Income;
import com.cashly.cashly_api.incomes.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JpaIncomeRepository Unit Tests")
class JpaIncomeRepositoryUnitTest {

    @Mock
    private SpringDataIncomeRepository springDataRepository;

    private JpaIncomeRepository jpaIncomeRepository;

    @BeforeEach
    void setUp() {
        jpaIncomeRepository = new JpaIncomeRepository(springDataRepository);
    }

    @Test
    @DisplayName("should_SaveIncome_When_ValidIncomeProvided")
    void should_SaveIncome_When_ValidIncomeProvided() {
        // Arrange
        Income domainIncome = createSampleIncome();
        IncomeEntity savedEntity = IncomeEntity.fromDomain(domainIncome);
        
        given(springDataRepository.save(any(IncomeEntity.class))).willReturn(savedEntity);
        
        // Act
        Income savedIncome = jpaIncomeRepository.save(domainIncome);
        
        // Assert
        assertThat(savedIncome).isNotNull();
        assertThat(savedIncome.getId()).isEqualTo(domainIncome.getId());
        assertThat(savedIncome.getAmount()).isEqualTo(domainIncome.getAmount());
        verify(springDataRepository).save(any(IncomeEntity.class));
    }

    @Test
    @DisplayName("should_FindIncomeById_When_IncomeExists")
    void should_FindIncomeById_When_IncomeExists() {
        // Arrange
        IncomeId incomeId = new IncomeId(UUID.randomUUID());
        IncomeEntity entity = createSampleIncomeEntity(incomeId.getValue().toString());
        
        given(springDataRepository.findById(incomeId.getValue().toString())).willReturn(Optional.of(entity));
        
        // Act
        Optional<Income> foundIncome = jpaIncomeRepository.findById(incomeId);
        
        // Assert
        assertThat(foundIncome).isPresent();
        assertThat(foundIncome.get().getId()).isEqualTo(incomeId);
        verify(springDataRepository).findById(incomeId.getValue().toString());
    }

    @Test
    @DisplayName("should_ReturnEmpty_When_IncomeNotFound")
    void should_ReturnEmpty_When_IncomeNotFound() {
        // Arrange
        IncomeId incomeId = new IncomeId(UUID.randomUUID());
        
        given(springDataRepository.findById(incomeId.getValue().toString())).willReturn(Optional.empty());
        
        // Act
        Optional<Income> foundIncome = jpaIncomeRepository.findById(incomeId);
        
        // Assert
        assertThat(foundIncome).isEmpty();
        verify(springDataRepository).findById(incomeId.getValue().toString());
    }

    @Test
    @DisplayName("should_FindIncomesByUserId_When_UserHasIncomes")
    void should_FindIncomesByUserId_When_UserHasIncomes() {
        // Arrange
        String userId = "user123";
        List<IncomeEntity> entities = Arrays.asList(
            createSampleIncomeEntity("1"),
            createSampleIncomeEntity("2")
        );
        
        given(springDataRepository.findByUserIdOrderByCreatedAtDesc(userId)).willReturn(entities);
        
        // Act
        List<Income> incomes = jpaIncomeRepository.findByUserId(userId);
        
        // Assert
        assertThat(incomes).hasSize(2);
        assertThat(incomes).allMatch(income -> income.getUserId().equals(userId));
        verify(springDataRepository).findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Test
    @DisplayName("should_FindIncomesByUserIdAndDateRange_When_FilteringByDateRange")
    void should_FindIncomesByUserIdAndDateRange_When_FilteringByDateRange() {
        // Arrange
        String userId = "user123";
        LocalDateTime startDate = LocalDateTime.now().minusDays(10);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);
        List<IncomeEntity> entities = Arrays.asList(createSampleIncomeEntity("1"));
        
        given(springDataRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, startDate, endDate))
            .willReturn(entities);
        
        // Act
        List<Income> incomes = jpaIncomeRepository.findByUserIdAndDateRange(userId, startDate, endDate);
        
        // Assert
        assertThat(incomes).hasSize(1);
        verify(springDataRepository).findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, startDate, endDate);
    }

    @Test
    @DisplayName("should_FindIncomesByUserIdAndCategory_When_FilteringByCategory")
    void should_FindIncomesByUserIdAndCategory_When_FilteringByCategory() {
        // Arrange
        String userId = "user123";
        Category category = new Category("SALARY");
        List<IncomeEntity> entities = Arrays.asList(createSampleIncomeEntity("1"));
        
        given(springDataRepository.findByUserIdAndCategoryOrderByCreatedAtDesc(userId, "SALARY"))
            .willReturn(entities);
        
        // Act
        List<Income> incomes = jpaIncomeRepository.findByUserIdAndCategory(userId, category);
        
        // Assert
        assertThat(incomes).hasSize(1);
        verify(springDataRepository).findByUserIdAndCategoryOrderByCreatedAtDesc(userId, "SALARY");
    }

    @Test
    @DisplayName("should_DeleteIncomeById_When_ValidIdProvided")
    void should_DeleteIncomeById_When_ValidIdProvided() {
        // Arrange
        IncomeId incomeId = new IncomeId(UUID.randomUUID());
        
        // Act
        jpaIncomeRepository.deleteById(incomeId);
        
        // Assert
        verify(springDataRepository).deleteById(incomeId.getValue().toString());
    }

    @Test
    @DisplayName("should_CheckExistenceById_When_ValidIdProvided")
    void should_CheckExistenceById_When_ValidIdProvided() {
        // Arrange
        IncomeId incomeId = new IncomeId(UUID.randomUUID());
        
        given(springDataRepository.existsById(incomeId.getValue().toString())).willReturn(true);
        
        // Act
        boolean exists = jpaIncomeRepository.existsById(incomeId);
        
        // Assert
        assertThat(exists).isTrue();
        verify(springDataRepository).existsById(incomeId.getValue().toString());
    }

    @Test
    @DisplayName("should_ReturnFalse_When_IncomeDoesNotExist")
    void should_ReturnFalse_When_IncomeDoesNotExist() {
        // Arrange
        IncomeId incomeId = new IncomeId(UUID.randomUUID());
        
        given(springDataRepository.existsById(incomeId.getValue().toString())).willReturn(false);
        
        // Act
        boolean exists = jpaIncomeRepository.existsById(incomeId);
        
        // Assert
        assertThat(exists).isFalse();
        verify(springDataRepository).existsById(incomeId.getValue().toString());
    }

    @Test
    @DisplayName("should_ThrowException_When_SaveReceivesNull")
    void should_ThrowException_When_SaveReceivesNull() {
        // Act & Assert
        assertThatThrownBy(() -> jpaIncomeRepository.save(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Income cannot be null");
        
        verifyNoInteractions(springDataRepository);
    }

    @Test
    @DisplayName("should_ThrowException_When_FindByIdReceivesNull")
    void should_ThrowException_When_FindByIdReceivesNull() {
        // Act & Assert
        assertThatThrownBy(() -> jpaIncomeRepository.findById(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Income ID cannot be null");
        
        verifyNoInteractions(springDataRepository);
    }

    @Test
    @DisplayName("should_ThrowException_When_FindByUserIdReceivesNull")
    void should_ThrowException_When_FindByUserIdReceivesNull() {
        // Act & Assert
        assertThatThrownBy(() -> jpaIncomeRepository.findByUserId(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("User ID cannot be null");
        
        verifyNoInteractions(springDataRepository);
    }

    @Test
    @DisplayName("should_ThrowException_When_FindByUserIdReceivesEmpty")
    void should_ThrowException_When_FindByUserIdReceivesEmpty() {
        // Act & Assert
        assertThatThrownBy(() -> jpaIncomeRepository.findByUserId(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("User ID cannot be empty");
        
        verifyNoInteractions(springDataRepository);
    }

    @Test
    @DisplayName("should_ThrowException_When_DateRangeParametersAreNull")
    void should_ThrowException_When_DateRangeParametersAreNull() {
        // Arrange
        String userId = "user123";
        LocalDateTime validDate = LocalDateTime.now();
        
        // Act & Assert
        assertThatThrownBy(() -> jpaIncomeRepository.findByUserIdAndDateRange(null, validDate, validDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("User ID cannot be null");
            
        assertThatThrownBy(() -> jpaIncomeRepository.findByUserIdAndDateRange(userId, null, validDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Start date cannot be null");
            
        assertThatThrownBy(() -> jpaIncomeRepository.findByUserIdAndDateRange(userId, validDate, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("End date cannot be null");
        
        verifyNoInteractions(springDataRepository);
    }

    @Test
    @DisplayName("should_ThrowException_When_CategoryParametersAreNull")
    void should_ThrowException_When_CategoryParametersAreNull() {
        // Arrange
        String userId = "user123";
        Category validCategory = new Category("SALARY");
        
        // Act & Assert
        assertThatThrownBy(() -> jpaIncomeRepository.findByUserIdAndCategory(null, validCategory))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("User ID cannot be null");
            
        assertThatThrownBy(() -> jpaIncomeRepository.findByUserIdAndCategory(userId, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Category cannot be null");
        
        verifyNoInteractions(springDataRepository);
    }

    @Test
    @DisplayName("should_ThrowException_When_DeleteByIdReceivesNull")
    void should_ThrowException_When_DeleteByIdReceivesNull() {
        // Act & Assert
        assertThatThrownBy(() -> jpaIncomeRepository.deleteById(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Income ID cannot be null");
        
        verifyNoInteractions(springDataRepository);
    }

    @Test
    @DisplayName("should_ThrowException_When_ExistsByIdReceivesNull")
    void should_ThrowException_When_ExistsByIdReceivesNull() {
        // Act & Assert
        assertThatThrownBy(() -> jpaIncomeRepository.existsById(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Income ID cannot be null");
        
        verifyNoInteractions(springDataRepository);
    }

    private Income createSampleIncome() {
        IncomeId incomeId = new IncomeId(UUID.randomUUID());
        Amount amount = new Amount(new BigDecimal("1500.00"));
        Description description = new Description("Test income");
        Category category = new Category("SALARY");
        LocalDate date = LocalDate.now();
        String userId = "user123";
        
        return new Income(incomeId, amount, description, category, date, userId);
    }

    private IncomeEntity createSampleIncomeEntity(String id) {
        IncomeEntity entity = new IncomeEntity();
        // Ensure ID is a valid UUID
        String validId = id.length() == 1 ? UUID.randomUUID().toString() : id;
        entity.setId(validId);
        entity.setAmount(new BigDecimal("1500.00"));
        entity.setDescription("Test income");
        entity.setCategory("SALARY");
        entity.setDate(LocalDate.now());
        entity.setUserId("user123");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}