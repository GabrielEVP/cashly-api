package com.cashly.cashly_api.incomes.application.usecases;

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

public class DeleteIncomeUseCaseUnitTest {
    
    @Mock
    private IncomeRepository incomeRepository;
    
    private DeleteIncomeUseCase deleteIncomeUseCase;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteIncomeUseCase = new DeleteIncomeUseCase(incomeRepository);
    }
    
    @Test
    public void should_DeleteIncome_When_IncomeExists() {
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
        doNothing().when(incomeRepository).deleteById(incomeId);
        
        // Act
        assertDoesNotThrow(() -> deleteIncomeUseCase.execute(incomeIdString));
        
        // Assert
        verify(incomeRepository, times(1)).findById(incomeId);
        verify(incomeRepository, times(1)).deleteById(incomeId);
    }
    
    @Test
    public void should_ThrowException_When_IncomeNotFound() {
        // Arrange
        String incomeIdString = "123e4567-e89b-12d3-a456-426614174000";
        IncomeId incomeId = new IncomeId(UUID.fromString(incomeIdString));
        
        when(incomeRepository.findById(incomeId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            deleteIncomeUseCase.execute(incomeIdString);
        });
        
        verify(incomeRepository, times(1)).findById(incomeId);
        verify(incomeRepository, never()).deleteById(any(IncomeId.class));
    }
    
    @Test
    public void should_ThrowException_When_IdIsNull() {
        // Arrange
        String incomeIdString = null;
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            deleteIncomeUseCase.execute(incomeIdString);
        });
        
        verify(incomeRepository, never()).findById(any(IncomeId.class));
        verify(incomeRepository, never()).deleteById(any(IncomeId.class));
    }
    
    @Test
    public void should_ThrowException_When_IdIsInvalidUUID() {
        // Arrange
        String incomeIdString = "invalid-uuid";
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            deleteIncomeUseCase.execute(incomeIdString);
        });
        
        verify(incomeRepository, never()).findById(any(IncomeId.class));
        verify(incomeRepository, never()).deleteById(any(IncomeId.class));
    }
}