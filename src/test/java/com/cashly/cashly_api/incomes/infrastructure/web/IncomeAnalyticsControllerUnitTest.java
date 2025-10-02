package com.cashly.cashly_api.incomes.infrastructure.web;

import com.cashly.cashly_api.incomes.domain.services.IncomeService;
import com.cashly.cashly_api.incomes.domain.services.IncomeService.IncomeGrowthAnalysis;
import com.cashly.cashly_api.incomes.domain.valueobjects.Amount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IncomeAnalyticsControllerUnitTest {

    @Mock
    private IncomeService incomeService;

    private IncomeAnalyticsController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new IncomeAnalyticsController(incomeService);
    }

    @Test
    void should_ReturnIncomeGrowthAnalysis_When_ValidGrowthAnalysisRequested() {
        // Arrange
        String userId = "user123";
        String monthParam = "2024-03";
        YearMonth month = YearMonth.parse(monthParam);
        
        IncomeGrowthAnalysis expectedAnalysis = new IncomeGrowthAnalysis(
            new Amount(BigDecimal.valueOf(5000)),
            new Amount(BigDecimal.valueOf(5500)),
            BigDecimal.valueOf(10.00),
            month
        );
        
        when(incomeService.analyzeIncomeGrowth(userId, month)).thenReturn(expectedAnalysis);

        // Act
        ResponseEntity<IncomeGrowthAnalysis> result = controller.getIncomeGrowthAnalysis(userId, monthParam);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedAnalysis, result.getBody());
        verify(incomeService).analyzeIncomeGrowth(userId, month);
    }

    @Test
    void should_ReturnBadRequest_When_GrowthAnalysisRequestedWithNullUserId() {
        // Act
        ResponseEntity<IncomeGrowthAnalysis> result = controller.getIncomeGrowthAnalysis(null, "2024-03");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(incomeService);
    }

    @Test
    void should_ReturnBadRequest_When_GrowthAnalysisRequestedWithEmptyUserId() {
        // Act
        ResponseEntity<IncomeGrowthAnalysis> result = controller.getIncomeGrowthAnalysis("", "2024-03");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(incomeService);
    }

    @Test
    void should_ReturnBadRequest_When_GrowthAnalysisRequestedWithInvalidMonth() {
        // Act
        ResponseEntity<IncomeGrowthAnalysis> result = controller.getIncomeGrowthAnalysis("user123", "invalid-month");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(incomeService);
    }

    @Test
    void should_ReturnBadRequest_When_GrowthAnalysisRequestedWithNullMonth() {
        // Act
        ResponseEntity<IncomeGrowthAnalysis> result = controller.getIncomeGrowthAnalysis("user123", null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(incomeService);
    }

    @Test
    void should_ThrowIllegalArgumentException_When_GrowthAnalysisServiceThrowsIllegalArgumentException() {
        // Arrange
        String userId = "user123";
        String monthParam = "2024-03";

        when(incomeService.analyzeIncomeGrowth(userId, YearMonth.parse(monthParam)))
            .thenThrow(new IllegalArgumentException("Invalid input"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            controller.getIncomeGrowthAnalysis(userId, monthParam)
        );
    }

    @Test
    void should_ThrowException_When_GrowthAnalysisServiceThrowsException() {
        // Arrange
        String userId = "user123";
        String monthParam = "2024-03";

        when(incomeService.analyzeIncomeGrowth(userId, YearMonth.parse(monthParam)))
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            controller.getIncomeGrowthAnalysis(userId, monthParam)
        );
    }

    @Test
    void should_ReturnMonthlyAverage_When_ValidMonthlyAverageRequested() {
        // Arrange
        String userId = "user123";
        String monthsParam = "6";
        String referenceDateParam = "2024-03-31";
        
        int months = Integer.parseInt(monthsParam);
        LocalDate referenceDate = LocalDate.parse(referenceDateParam);
        
        Amount expectedAverage = new Amount(BigDecimal.valueOf(5200.75));
        
        when(incomeService.calculateMonthlyAverageIncome(userId, months, referenceDate))
            .thenReturn(expectedAverage);

        // Act
        ResponseEntity<Amount> result = controller.getMonthlyAverage(userId, monthsParam, referenceDateParam);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedAverage, result.getBody());
        verify(incomeService).calculateMonthlyAverageIncome(userId, months, referenceDate);
    }

    @Test
    void should_ReturnBadRequest_When_MonthlyAverageRequestedWithNullUserId() {
        // Act
        ResponseEntity<Amount> result = controller.getMonthlyAverage(null, "6", "2024-03-31");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(incomeService);
    }

    @Test
    void should_ReturnBadRequest_When_MonthlyAverageRequestedWithEmptyUserId() {
        // Act
        ResponseEntity<Amount> result = controller.getMonthlyAverage("   ", "6", "2024-03-31");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(incomeService);
    }

    @Test
    void should_ReturnBadRequest_When_MonthlyAverageRequestedWithInvalidMonths() {
        // Act
        ResponseEntity<Amount> result = controller.getMonthlyAverage("user123", "invalid", "2024-03-31");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(incomeService);
    }

    @Test
    void should_ReturnBadRequest_When_MonthlyAverageRequestedWithNullMonths() {
        // Act
        ResponseEntity<Amount> result = controller.getMonthlyAverage("user123", null, "2024-03-31");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(incomeService);
    }

    @Test
    void should_ReturnBadRequest_When_MonthlyAverageRequestedWithInvalidDate() {
        // Act
        ResponseEntity<Amount> result = controller.getMonthlyAverage("user123", "6", "invalid-date");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(incomeService);
    }

    @Test
    void should_ReturnBadRequest_When_MonthlyAverageRequestedWithNullDate() {
        // Act
        ResponseEntity<Amount> result = controller.getMonthlyAverage("user123", "6", null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(incomeService);
    }

    @Test
    void should_ThrowIllegalArgumentException_When_MonthlyAverageServiceThrowsIllegalArgumentException() {
        // Arrange
        String userId = "user123";
        String monthsParam = "6";
        String referenceDateParam = "2024-03-31";

        when(incomeService.calculateMonthlyAverageIncome(userId, 6, LocalDate.parse(referenceDateParam)))
            .thenThrow(new IllegalArgumentException("Invalid input"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            controller.getMonthlyAverage(userId, monthsParam, referenceDateParam)
        );
    }

    @Test
    void should_ThrowException_When_MonthlyAverageServiceThrowsException() {
        // Arrange
        String userId = "user123";
        String monthsParam = "6";
        String referenceDateParam = "2024-03-31";

        when(incomeService.calculateMonthlyAverageIncome(userId, 6, LocalDate.parse(referenceDateParam)))
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            controller.getMonthlyAverage(userId, monthsParam, referenceDateParam)
        );
    }
}