package com.cashly.cashly_api.incomes.domain.services;

import com.cashly.cashly_api.incomes.application.ports.IncomeRepository;
import com.cashly.cashly_api.incomes.domain.entities.Income;
import com.cashly.cashly_api.incomes.domain.services.IncomeService.IncomeGrowthAnalysis;
import com.cashly.cashly_api.incomes.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncomeServiceUnitTest {

    @Mock
    private IncomeRepository incomeRepository;
    
    private IncomeService incomeService;
    private String validUserId;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        incomeService = new IncomeService(incomeRepository);
        validUserId = "user123";
        startDate = LocalDate.of(2024, 1, 1);
        endDate = LocalDate.of(2024, 1, 31);
    }

    @Test
    void should_CalculateMonthlyAverage_When_ValidParametersProvided() {
        // Arrange
        LocalDate referenceDate = LocalDate.of(2024, 1, 31);
        Amount expectedTotal = new Amount(new BigDecimal("3700.00"));
        when(incomeRepository.calculateTotalIncomeForPeriod(eq(validUserId), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(expectedTotal);
        
        // Act
        Amount result = incomeService.calculateMonthlyAverageIncome(validUserId, 3, referenceDate);
        
        // Assert
        BigDecimal expected = new BigDecimal("3700.00").divide(BigDecimal.valueOf(3), 2, BigDecimal.ROUND_HALF_UP);
        assertEquals(expected, result.getValue());
        verify(incomeRepository).calculateTotalIncomeForPeriod(eq(validUserId), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void should_ThrowException_When_MonthsIsZero() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.calculateMonthlyAverageIncome(validUserId, 0, LocalDate.now()));
    }

    @Test
    void should_ThrowException_When_MonthsIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.calculateMonthlyAverageIncome(validUserId, -1, LocalDate.now()));
    }

    @Test
    void should_ThrowException_When_ReferenceDateIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.calculateMonthlyAverageIncome(validUserId, 3, null));
    }

    @Test
    void should_ThrowException_When_UserIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.calculateMonthlyAverageIncome(null, 3, LocalDate.now()));
    }

    @Test
    void should_ThrowException_When_UserIdIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.calculateMonthlyAverageIncome("", 3, LocalDate.now()));
    }

    @Test
    void should_AnalyzeIncomeGrowth_When_ValidParametersProvided() {
        // Arrange
        YearMonth currentMonth = YearMonth.of(2024, 1);
        Amount currentIncome = new Amount(new BigDecimal("3700.00"));
        Amount previousIncome = new Amount(BigDecimal.ZERO);
        
        when(incomeRepository.calculateTotalIncomeForPeriod(eq(validUserId), eq(currentMonth.atDay(1)), eq(currentMonth.atEndOfMonth())))
            .thenReturn(currentIncome);
        when(incomeRepository.calculateTotalIncomeForPeriod(eq(validUserId), eq(currentMonth.minusMonths(1).atDay(1)), eq(currentMonth.minusMonths(1).atEndOfMonth())))
            .thenReturn(previousIncome);
        
        // Act
        IncomeGrowthAnalysis result = incomeService.analyzeIncomeGrowth(validUserId, currentMonth);
        
        // Assert
        assertNotNull(result);
        assertEquals(currentMonth, result.getAnalysisMonth());
        assertEquals(new BigDecimal("3700.00"), result.getCurrentMonthIncome().getValue());
        assertEquals(BigDecimal.ZERO, result.getPreviousMonthIncome().getValue());
        assertEquals(new BigDecimal("100.00"), result.getGrowthPercentage());
        
        verify(incomeRepository, times(2)).calculateTotalIncomeForPeriod(eq(validUserId), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void should_ReturnZeroGrowth_When_BothMonthsHaveZeroIncome() {
        // Arrange
        YearMonth futureMonth = YearMonth.of(2024, 6);
        Amount zeroIncome = new Amount(BigDecimal.ZERO);
        
        when(incomeRepository.calculateTotalIncomeForPeriod(eq(validUserId), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(zeroIncome);
        
        // Act
        IncomeGrowthAnalysis result = incomeService.analyzeIncomeGrowth(validUserId, futureMonth);
        
        // Assert
        assertEquals(BigDecimal.ZERO, result.getGrowthPercentage());
        assertEquals(BigDecimal.ZERO, result.getCurrentMonthIncome().getValue());
        assertEquals(BigDecimal.ZERO, result.getPreviousMonthIncome().getValue());
        
        verify(incomeRepository, times(2)).calculateTotalIncomeForPeriod(eq(validUserId), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void should_ThrowException_When_CurrentMonthIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.analyzeIncomeGrowth(validUserId, null));
    }

    @Test
    void should_ThrowException_When_UserIdIsNullForIncomeGrowth() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.analyzeIncomeGrowth(null, YearMonth.of(2024, 1)));
    }

    @Test
    void should_ThrowException_When_UserIdIsEmptyForIncomeGrowth() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.analyzeIncomeGrowth("", YearMonth.of(2024, 1)));
    }

    @Test
    void should_ReturnTrue_When_IncomeIsSignificant() {
        Amount testAmount = new Amount(new BigDecimal("5000.00"));
        Amount averageIncome = new Amount(new BigDecimal("3000.00"));
        BigDecimal threshold = new BigDecimal("1.5");
        
        boolean result = incomeService.isSignificantIncome(testAmount, averageIncome, threshold);
        
        assertTrue(result);
    }

    @Test
    void should_ReturnFalse_When_IncomeIsNotSignificant() {
        Amount testAmount = new Amount(new BigDecimal("2000.00"));
        Amount averageIncome = new Amount(new BigDecimal("3000.00"));
        BigDecimal threshold = new BigDecimal("1.5");
        
        boolean result = incomeService.isSignificantIncome(testAmount, averageIncome, threshold);
        
        assertFalse(result);
    }

    @Test
    void should_ThrowException_When_AmountIsNullForSignificance() {
        Amount averageIncome = new Amount(new BigDecimal("3000.00"));
        BigDecimal threshold = new BigDecimal("1.5");
        
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.isSignificantIncome(null, averageIncome, threshold));
    }

    @Test
    void should_ThrowException_When_AverageIncomeIsNullForSignificance() {
        Amount testAmount = new Amount(new BigDecimal("5000.00"));
        BigDecimal threshold = new BigDecimal("1.5");
        
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.isSignificantIncome(testAmount, null, threshold));
    }

    @Test
    void should_ThrowException_When_ThresholdIsNullForSignificance() {
        Amount testAmount = new Amount(new BigDecimal("5000.00"));
        Amount averageIncome = new Amount(new BigDecimal("3000.00"));
        
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.isSignificantIncome(testAmount, averageIncome, null));
    }

    @Test
    void should_ThrowException_When_ThresholdIsZeroForSignificance() {
        Amount testAmount = new Amount(new BigDecimal("5000.00"));
        Amount averageIncome = new Amount(new BigDecimal("3000.00"));
        BigDecimal threshold = BigDecimal.ZERO;
        
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.isSignificantIncome(testAmount, averageIncome, threshold));
    }

    @Test
    void should_ThrowException_When_ThresholdIsNegativeForSignificance() {
        Amount testAmount = new Amount(new BigDecimal("5000.00"));
        Amount averageIncome = new Amount(new BigDecimal("3000.00"));
        BigDecimal threshold = new BigDecimal("-1.0");
        
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.isSignificantIncome(testAmount, averageIncome, threshold));
    }
}