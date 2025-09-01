package com.cashly.cashly_api.incomes.application.usecases;

import com.cashly.cashly_api.incomes.application.dto.IncomeResponse;
import com.cashly.cashly_api.incomes.application.dto.UpdateIncomeRequest;
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

public class UpdateIncomeUseCaseUnitTest {
    
    @Mock
    private IncomeRepository incomeRepository;
    
    private UpdateIncomeUseCase updateIncomeUseCase;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        updateIncomeUseCase = new UpdateIncomeUseCase(incomeRepository);
    }
    
    @Test
    public void should_UpdateIncome_When_ValidRequestProvided() {
        // Arrange
        String incomeIdString = "123e4567-e89b-12d3-a456-426614174000";
        IncomeId incomeId = new IncomeId(UUID.fromString(incomeIdString));
        UpdateIncomeRequest request = new UpdateIncomeRequest("Updated description", "BUSINESS");
        
        Income existingIncome = new Income(
            incomeId,
            new Amount(new BigDecimal("1000.00")),
            new Description("Original description"),
            new Category("SALARY"),
            LocalDate.now(),
            "user123"
        );
        
        when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(existingIncome));
        when(incomeRepository.save(any(Income.class))).thenReturn(existingIncome);
        
        // Act
        IncomeResponse response = updateIncomeUseCase.execute(incomeIdString, request);
        
        // Assert
        assertNotNull(response);
        assertEquals(incomeIdString, response.getId());
        
        verify(incomeRepository, times(1)).findById(incomeId);
        verify(incomeRepository, times(1)).save(any(Income.class));
    }
    
    @Test
    public void should_UpdateOnlyDescription_When_CategoryIsNull() {
        // Arrange
        String incomeIdString = "123e4567-e89b-12d3-a456-426614174000";
        IncomeId incomeId = new IncomeId(UUID.fromString(incomeIdString));
        UpdateIncomeRequest request = new UpdateIncomeRequest("Updated description", null);
        
        Income existingIncome = new Income(
            incomeId,
            new Amount(new BigDecimal("1000.00")),
            new Description("Original description"),
            new Category("SALARY"),
            LocalDate.now(),
            "user123"
        );
        
        when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(existingIncome));
        when(incomeRepository.save(any(Income.class))).thenReturn(existingIncome);
        
        // Act
        IncomeResponse response = updateIncomeUseCase.execute(incomeIdString, request);
        
        // Assert
        assertNotNull(response);
        assertEquals(incomeIdString, response.getId());
        
        verify(incomeRepository, times(1)).findById(incomeId);
        verify(incomeRepository, times(1)).save(any(Income.class));
    }
    
    @Test
    public void should_ThrowException_When_IncomeNotFound() {
        // Arrange
        String incomeIdString = "123e4567-e89b-12d3-a456-426614174000";
        IncomeId incomeId = new IncomeId(UUID.fromString(incomeIdString));
        UpdateIncomeRequest request = new UpdateIncomeRequest("Updated description", "BUSINESS");
        
        when(incomeRepository.findById(incomeId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            updateIncomeUseCase.execute(incomeIdString, request);
        });
        
        verify(incomeRepository, times(1)).findById(incomeId);
        verify(incomeRepository, never()).save(any(Income.class));
    }
    
    @Test
    public void should_ThrowException_When_IdIsNull() {
        // Arrange
        String incomeIdString = null;
        UpdateIncomeRequest request = new UpdateIncomeRequest("Updated description", "BUSINESS");
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            updateIncomeUseCase.execute(incomeIdString, request);
        });
        
        verify(incomeRepository, never()).findById(any(IncomeId.class));
        verify(incomeRepository, never()).save(any(Income.class));
    }
    
    @Test
    public void should_ThrowException_When_RequestIsNull() {
        // Arrange
        String incomeIdString = "123e4567-e89b-12d3-a456-426614174000";
        UpdateIncomeRequest request = null;
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            updateIncomeUseCase.execute(incomeIdString, request);
        });
        
        verify(incomeRepository, never()).findById(any(IncomeId.class));
        verify(incomeRepository, never()).save(any(Income.class));
    }
}