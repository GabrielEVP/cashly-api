package com.cashly.cashly_api.incomes.application.usecases;

import com.cashly.cashly_api.incomes.application.dto.CreateIncomeRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateIncomeUseCaseUnitTest {
    
    @Mock
    private IncomeRepository incomeRepository;
    
    private CreateIncomeUseCase createIncomeUseCase;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // This will fail until we create the use case
        createIncomeUseCase = new CreateIncomeUseCase(incomeRepository);
    }
    
    @Test
    public void should_CreateIncome_When_ValidRequestProvided() {
        // Arrange
        CreateIncomeRequest request = new CreateIncomeRequest(
            new BigDecimal("1000.00"), 
            "Salary payment", 
            "SALARY", 
            "user123"
        );
        
        Income savedIncome = new Income(
            IncomeId.generate(),
            new Amount(new BigDecimal("1000.00")),
            new Description("Salary payment"),
            new Category("SALARY"),
            LocalDate.now(),
            "user123"
        );
        
        when(incomeRepository.save(any(Income.class))).thenReturn(savedIncome);
        
        // Act
        IncomeResponse response = createIncomeUseCase.execute(request);
        
        // Assert
        assertNotNull(response);
        assertEquals(savedIncome.getId().getValue().toString(), response.getId());
        assertEquals(savedIncome.getAmount().getValue(), response.getAmount());
        assertEquals(savedIncome.getDescription().getValue(), response.getDescription());
        assertEquals(savedIncome.getCategory().getValue(), response.getCategory());
        assertEquals(savedIncome.getUserId(), response.getUserId());
        
        verify(incomeRepository, times(1)).save(any(Income.class));
    }
    
    @Test
    public void should_ThrowException_When_AmountIsNull() {
        // Arrange
        CreateIncomeRequest request = new CreateIncomeRequest(
            null, 
            "Salary payment", 
            "SALARY", 
            "user123"
        );
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            createIncomeUseCase.execute(request);
        });
        
        verify(incomeRepository, never()).save(any(Income.class));
    }
    
    @Test
    public void should_ThrowException_When_DescriptionIsNull() {
        // Arrange
        CreateIncomeRequest request = new CreateIncomeRequest(
            new BigDecimal("1000.00"), 
            null, 
            "SALARY", 
            "user123"
        );
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            createIncomeUseCase.execute(request);
        });
        
        verify(incomeRepository, never()).save(any(Income.class));
    }
    
    @Test
    public void should_ThrowException_When_CategoryIsInvalid() {
        // Arrange
        CreateIncomeRequest request = new CreateIncomeRequest(
            new BigDecimal("1000.00"), 
            "Salary payment", 
            "INVALID_CATEGORY", 
            "user123"
        );
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            createIncomeUseCase.execute(request);
        });
        
        verify(incomeRepository, never()).save(any(Income.class));
    }
    
    @Test
    public void should_ThrowException_When_UserIdIsNull() {
        // Arrange
        CreateIncomeRequest request = new CreateIncomeRequest(
            new BigDecimal("1000.00"), 
            "Salary payment", 
            "SALARY", 
            null
        );
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            createIncomeUseCase.execute(request);
        });
        
        verify(incomeRepository, never()).save(any(Income.class));
    }
    
    @Test
    public void should_ThrowException_When_AmountIsNegative() {
        // Arrange
        CreateIncomeRequest request = new CreateIncomeRequest(
            new BigDecimal("-100.00"), 
            "Invalid income", 
            "SALARY", 
            "user123"
        );
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            createIncomeUseCase.execute(request);
        });
        
        verify(incomeRepository, never()).save(any(Income.class));
    }
}