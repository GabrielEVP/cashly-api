package com.cashly.cashly_api.incomes.application.usecases;

import com.cashly.cashly_api.incomes.application.dto.IncomeResponse;
import com.cashly.cashly_api.incomes.application.ports.IncomeRepository;
import com.cashly.cashly_api.incomes.domain.entities.Income;
import com.cashly.cashly_api.incomes.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GetIncomeByIdUseCaseUnitTest {
    
    @Mock
    private IncomeRepository incomeRepository;
    
    private GetIncomeByIdUseCase getIncomeByIdUseCase;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        getIncomeByIdUseCase = new GetIncomeByIdUseCase(incomeRepository);
    }
    
    @Test
    public void should_ReturnIncome_When_IncomeExists() {
        // Arrange
        String incomeIdString = "123e4567-e89b-12d3-a456-426614174000";
        IncomeId incomeId = new IncomeId(UUID.fromString(incomeIdString));
        Income income = new Income(
            incomeId,
            new Amount(new BigDecimal("1000.00")),
            new Description("Salary payment"),
            new Category("SALARY"),
            LocalDate.now(),
            "user123"
        );
        
        when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(income));
        
        // Act
        IncomeResponse response = getIncomeByIdUseCase.execute(incomeIdString);
        
        // Assert
        assertNotNull(response);
        assertEquals(income.getId().getValue().toString(), response.getId());
        assertEquals(income.getAmount().getValue(), response.getAmount());
        assertEquals(income.getDescription().getValue(), response.getDescription());
        assertEquals(income.getCategory().getValue(), response.getCategory());
        assertEquals(income.getUserId(), response.getUserId());
        
        verify(incomeRepository, times(1)).findById(incomeId);
    }
    
    @Test
    public void should_ThrowException_When_IncomeNotFound() {
        // Arrange
        String incomeIdString = "123e4567-e89b-12d3-a456-426614174000";
        IncomeId incomeId = new IncomeId(UUID.fromString(incomeIdString));
        
        when(incomeRepository.findById(incomeId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            getIncomeByIdUseCase.execute(incomeIdString);
        });
        
        verify(incomeRepository, times(1)).findById(incomeId);
    }
    
    @Test
    public void should_ThrowException_When_IdIsNull() {
        // Arrange
        String incomeIdString = null;
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            getIncomeByIdUseCase.execute(incomeIdString);
        });
        
        verify(incomeRepository, never()).findById(any(IncomeId.class));
    }
    
    @Test
    public void should_ThrowException_When_IdIsInvalidUUID() {
        // Arrange
        String incomeIdString = "invalid-uuid";
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            getIncomeByIdUseCase.execute(incomeIdString);
        });
        
        verify(incomeRepository, never()).findById(any(IncomeId.class));
    }
}