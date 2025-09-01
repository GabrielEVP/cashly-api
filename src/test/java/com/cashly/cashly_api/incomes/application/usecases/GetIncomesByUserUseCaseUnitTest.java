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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GetIncomesByUserUseCaseUnitTest {
    
    @Mock
    private IncomeRepository incomeRepository;
    
    private GetIncomesByUserUseCase getIncomesByUserUseCase;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // This will fail until we create the use case
        getIncomesByUserUseCase = new GetIncomesByUserUseCase(incomeRepository);
    }
    
    @Test
    public void should_ReturnIncomes_When_UserHasIncomes() {
        // Arrange
        String userId = "user123";
        Income income1 = new Income(
            IncomeId.generate(),
            new Amount(new BigDecimal("1000.00")),
            new Description("Salary payment"),
            new Category("SALARY"),
            LocalDate.now(),
            userId
        );
        Income income2 = new Income(
            IncomeId.generate(),
            new Amount(new BigDecimal("500.00")),
            new Description("Freelance payment"),
            new Category("BUSINESS"),
            LocalDate.now(),
            userId
        );
        
        List<Income> incomes = Arrays.asList(income1, income2);
        when(incomeRepository.findByUserId(userId)).thenReturn(incomes);
        
        // Act
        List<IncomeResponse> responses = getIncomesByUserUseCase.execute(userId);
        
        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        
        IncomeResponse response1 = responses.get(0);
        assertEquals(income1.getId().getValue().toString(), response1.getId());
        assertEquals(income1.getAmount().getValue(), response1.getAmount());
        assertEquals(income1.getDescription().getValue(), response1.getDescription());
        assertEquals(income1.getCategory().getValue(), response1.getCategory());
        assertEquals(income1.getUserId(), response1.getUserId());
        
        IncomeResponse response2 = responses.get(1);
        assertEquals(income2.getId().getValue().toString(), response2.getId());
        assertEquals(income2.getAmount().getValue(), response2.getAmount());
        assertEquals(income2.getDescription().getValue(), response2.getDescription());
        assertEquals(income2.getCategory().getValue(), response2.getCategory());
        assertEquals(income2.getUserId(), response2.getUserId());
        
        verify(incomeRepository, times(1)).findByUserId(userId);
    }
    
    @Test
    public void should_ReturnEmptyList_When_UserHasNoIncomes() {
        // Arrange
        String userId = "user123";
        when(incomeRepository.findByUserId(userId)).thenReturn(Collections.emptyList());
        
        // Act
        List<IncomeResponse> responses = getIncomesByUserUseCase.execute(userId);
        
        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        
        verify(incomeRepository, times(1)).findByUserId(userId);
    }
    
    @Test
    public void should_ThrowException_When_UserIdIsNull() {
        // Arrange
        String userId = null;
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            getIncomesByUserUseCase.execute(userId);
        });
        
        verify(incomeRepository, never()).findByUserId(anyString());
    }
    
    @Test
    public void should_ThrowException_When_UserIdIsEmpty() {
        // Arrange
        String userId = "";
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            getIncomesByUserUseCase.execute(userId);
        });
        
        verify(incomeRepository, never()).findByUserId(anyString());
    }
    
    @Test
    public void should_ThrowException_When_UserIdIsBlank() {
        // Arrange
        String userId = "   ";
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            getIncomesByUserUseCase.execute(userId);
        });
        
        verify(incomeRepository, never()).findByUserId(anyString());
    }
}