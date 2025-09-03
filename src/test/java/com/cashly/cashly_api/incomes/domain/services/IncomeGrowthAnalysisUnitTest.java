package com.cashly.cashly_api.incomes.domain.services;

import com.cashly.cashly_api.incomes.domain.services.IncomeService.IncomeGrowthAnalysis;
import com.cashly.cashly_api.incomes.domain.valueobjects.Amount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class IncomeGrowthAnalysisUnitTest {

    private Amount previousIncome;
    private Amount currentIncome;
    private BigDecimal growthPercentage;
    private YearMonth analysisMonth;

    @BeforeEach
    void setUp() {
        previousIncome = new Amount(new BigDecimal("2000.00"));
        currentIncome = new Amount(new BigDecimal("2500.00"));
        growthPercentage = new BigDecimal("25.00");
        analysisMonth = YearMonth.of(2024, 1);
    }

    @Test
    void should_CreateIncomeGrowthAnalysis_When_ValidParametersProvided() {
        IncomeGrowthAnalysis analysis = new IncomeGrowthAnalysis(
            previousIncome,
            currentIncome,
            growthPercentage,
            analysisMonth
        );

        assertNotNull(analysis);
        assertEquals(previousIncome, analysis.getPreviousMonthIncome());
        assertEquals(currentIncome, analysis.getCurrentMonthIncome());
        assertEquals(growthPercentage, analysis.getGrowthPercentage());
        assertEquals(analysisMonth, analysis.getAnalysisMonth());
    }

    @Test
    void should_ReturnTrue_When_GrowthIsPositive() {
        IncomeGrowthAnalysis analysis = new IncomeGrowthAnalysis(
            previousIncome,
            currentIncome,
            new BigDecimal("10.50"),
            analysisMonth
        );

        assertTrue(analysis.hasPositiveGrowth());
        assertFalse(analysis.hasNegativeGrowth());
    }

    @Test
    void should_ReturnTrue_When_GrowthIsNegative() {
        IncomeGrowthAnalysis analysis = new IncomeGrowthAnalysis(
            previousIncome,
            currentIncome,
            new BigDecimal("-15.25"),
            analysisMonth
        );

        assertTrue(analysis.hasNegativeGrowth());
        assertFalse(analysis.hasPositiveGrowth());
    }

    @Test
    void should_ReturnFalse_When_GrowthIsZero() {
        IncomeGrowthAnalysis analysis = new IncomeGrowthAnalysis(
            previousIncome,
            currentIncome,
            BigDecimal.ZERO,
            analysisMonth
        );

        assertFalse(analysis.hasPositiveGrowth());
        assertFalse(analysis.hasNegativeGrowth());
    }

    @Test
    void should_CalculateGrowthAmount_When_CurrentIsHigherThanPrevious() {
        IncomeGrowthAnalysis analysis = new IncomeGrowthAnalysis(
            previousIncome,
            currentIncome,
            growthPercentage,
            analysisMonth
        );

        Amount growthAmount = analysis.getGrowthAmount();
        assertEquals(new BigDecimal("500.00"), growthAmount.getValue());
    }

    @Test
    void should_CalculateNegativeGrowthAmount_When_CurrentIsLowerThanPrevious() {
        Amount lowerCurrentIncome = new Amount(new BigDecimal("1500.00"));
        IncomeGrowthAnalysis analysis = new IncomeGrowthAnalysis(
            previousIncome,
            lowerCurrentIncome,
            new BigDecimal("-25.00"),
            analysisMonth
        );

        Amount growthAmount = analysis.getGrowthAmount();
        assertEquals(new BigDecimal("500.00"), growthAmount.getValue());
    }

    @Test
    void should_CalculateZeroGrowthAmount_When_IncomesAreEqual() {
        IncomeGrowthAnalysis analysis = new IncomeGrowthAnalysis(
            previousIncome,
            previousIncome,
            BigDecimal.ZERO,
            analysisMonth
        );

        Amount growthAmount = analysis.getGrowthAmount();
        assertEquals(BigDecimal.ZERO, growthAmount.getValue());
    }

    @Test
    void should_HandleZeroPreviousIncome_When_CalculatingGrowthAmount() {
        Amount zeroPreviousIncome = new Amount(BigDecimal.ZERO);
        IncomeGrowthAnalysis analysis = new IncomeGrowthAnalysis(
            zeroPreviousIncome,
            currentIncome,
            new BigDecimal("100.00"),
            analysisMonth
        );

        Amount growthAmount = analysis.getGrowthAmount();
        assertEquals(new BigDecimal("2500.00"), growthAmount.getValue());
    }

    @Test
    void should_HandleZeroCurrentIncome_When_CalculatingGrowthAmount() {
        Amount zeroCurrentIncome = new Amount(BigDecimal.ZERO);
        IncomeGrowthAnalysis analysis = new IncomeGrowthAnalysis(
            previousIncome,
            zeroCurrentIncome,
            new BigDecimal("-100.00"),
            analysisMonth
        );

        Amount growthAmount = analysis.getGrowthAmount();
        assertEquals(new BigDecimal("2000.00"), growthAmount.getValue());
    }

    @Test
    void should_ProduceCorrectToString_When_Called() {
        IncomeGrowthAnalysis analysis = new IncomeGrowthAnalysis(
            previousIncome,
            currentIncome,
            growthPercentage,
            analysisMonth
        );

        String result = analysis.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("IncomeGrowthAnalysis"));
        assertTrue(result.contains(previousIncome.toString()));
        assertTrue(result.contains(currentIncome.toString()));
        assertTrue(result.contains(growthPercentage.toString()));
        assertTrue(result.contains(analysisMonth.toString()));
    }

    @Test
    void should_HandleLargeNumbers_When_CalculatingGrowthAmount() {
        Amount largePrevious = new Amount(new BigDecimal("999999.99"));
        Amount largeCurrent = new Amount(new BigDecimal("1000000.01"));
        IncomeGrowthAnalysis analysis = new IncomeGrowthAnalysis(
            largePrevious,
            largeCurrent,
            new BigDecimal("0.00"),
            analysisMonth
        );

        Amount growthAmount = analysis.getGrowthAmount();
        assertEquals(new BigDecimal("0.02"), growthAmount.getValue());
    }

    @Test
    void should_HandleDecimalPrecision_When_CalculatingGrowthAmount() {
        Amount precisionPrevious = new Amount(new BigDecimal("100.33"));
        Amount precisionCurrent = new Amount(new BigDecimal("200.67"));
        IncomeGrowthAnalysis analysis = new IncomeGrowthAnalysis(
            precisionPrevious,
            precisionCurrent,
            new BigDecimal("100.00"),
            analysisMonth
        );

        Amount growthAmount = analysis.getGrowthAmount();
        assertEquals(new BigDecimal("100.34"), growthAmount.getValue());
    }
}