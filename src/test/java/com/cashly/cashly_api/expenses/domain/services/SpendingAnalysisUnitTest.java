package com.cashly.cashly_api.expenses.domain.services;

import com.cashly.cashly_api.expenses.domain.services.ExpenseService.SpendingAnalysis;
import com.cashly.cashly_api.expenses.domain.valueobjects.Amount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class SpendingAnalysisUnitTest {

    private Amount previousExpense;
    private Amount currentExpense;
    private BigDecimal changePercentage;
    private YearMonth analysisMonth;

    @BeforeEach
    void setUp() {
        previousExpense = new Amount(new BigDecimal("1500.00"));
        currentExpense = new Amount(new BigDecimal("1800.00"));
        changePercentage = new BigDecimal("20.00");
        analysisMonth = YearMonth.of(2024, 1);
    }

    @Test
    void should_CreateSpendingAnalysis_When_ValidParametersProvided() {
        SpendingAnalysis analysis = new SpendingAnalysis(
            previousExpense,
            currentExpense,
            changePercentage,
            analysisMonth
        );

        assertNotNull(analysis);
        assertEquals(previousExpense, analysis.getPreviousMonthExpense());
        assertEquals(currentExpense, analysis.getCurrentMonthExpense());
        assertEquals(changePercentage, analysis.getChangePercentage());
        assertEquals(analysisMonth, analysis.getAnalysisMonth());
    }

    @Test
    void should_ReturnTrue_When_SpendingHasIncreased() {
        SpendingAnalysis analysis = new SpendingAnalysis(
            previousExpense,
            currentExpense,
            new BigDecimal("15.75"),
            analysisMonth
        );

        assertTrue(analysis.hasIncreasedSpending());
        assertFalse(analysis.hasDecreasedSpending());
    }

    @Test
    void should_ReturnTrue_When_SpendingHasDecreased() {
        SpendingAnalysis analysis = new SpendingAnalysis(
            previousExpense,
            currentExpense,
            new BigDecimal("-10.50"),
            analysisMonth
        );

        assertTrue(analysis.hasDecreasedSpending());
        assertFalse(analysis.hasIncreasedSpending());
    }

    @Test
    void should_ReturnFalse_When_SpendingHasNotChanged() {
        SpendingAnalysis analysis = new SpendingAnalysis(
            previousExpense,
            currentExpense,
            BigDecimal.ZERO,
            analysisMonth
        );

        assertFalse(analysis.hasIncreasedSpending());
        assertFalse(analysis.hasDecreasedSpending());
    }

    @Test
    void should_CalculateChangeAmount_When_CurrentIsHigherThanPrevious() {
        SpendingAnalysis analysis = new SpendingAnalysis(
            previousExpense,
            currentExpense,
            changePercentage,
            analysisMonth
        );

        Amount changeAmount = analysis.getChangeAmount();
        assertEquals(new BigDecimal("300.00"), changeAmount.getValue());
    }

    @Test
    void should_CalculateNegativeChangeAmount_When_CurrentIsLowerThanPrevious() {
        Amount lowerCurrentExpense = new Amount(new BigDecimal("1200.00"));
        SpendingAnalysis analysis = new SpendingAnalysis(
            previousExpense,
            lowerCurrentExpense,
            new BigDecimal("-20.00"),
            analysisMonth
        );

        Amount changeAmount = analysis.getChangeAmount();
        assertEquals(new BigDecimal("300.00"), changeAmount.getValue());
    }

    @Test
    void should_CalculateZeroChangeAmount_When_ExpensesAreEqual() {
        SpendingAnalysis analysis = new SpendingAnalysis(
            previousExpense,
            previousExpense,
            BigDecimal.ZERO,
            analysisMonth
        );

        Amount changeAmount = analysis.getChangeAmount();
        assertEquals(BigDecimal.ZERO, changeAmount.getValue());
    }

    @Test
    void should_HandleZeroPreviousExpense_When_CalculatingChangeAmount() {
        Amount zeroPreviousExpense = new Amount(BigDecimal.ZERO);
        SpendingAnalysis analysis = new SpendingAnalysis(
            zeroPreviousExpense,
            currentExpense,
            new BigDecimal("100.00"),
            analysisMonth
        );

        Amount changeAmount = analysis.getChangeAmount();
        assertEquals(new BigDecimal("1800.00"), changeAmount.getValue());
    }

    @Test
    void should_HandleZeroCurrentExpense_When_CalculatingChangeAmount() {
        Amount zeroCurrentExpense = new Amount(BigDecimal.ZERO);
        SpendingAnalysis analysis = new SpendingAnalysis(
            previousExpense,
            zeroCurrentExpense,
            new BigDecimal("-100.00"),
            analysisMonth
        );

        Amount changeAmount = analysis.getChangeAmount();
        assertEquals(new BigDecimal("1500.00"), changeAmount.getValue());
    }

    @Test
    void should_ProduceCorrectToString_When_Called() {
        SpendingAnalysis analysis = new SpendingAnalysis(
            previousExpense,
            currentExpense,
            changePercentage,
            analysisMonth
        );

        String result = analysis.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("SpendingAnalysis"));
        assertTrue(result.contains(previousExpense.toString()));
        assertTrue(result.contains(currentExpense.toString()));
        assertTrue(result.contains(changePercentage.toString()));
        assertTrue(result.contains(analysisMonth.toString()));
    }

    @Test
    void should_HandleLargeNumbers_When_CalculatingChangeAmount() {
        Amount largePrevious = new Amount(new BigDecimal("999999.99"));
        Amount largeCurrent = new Amount(new BigDecimal("1000000.01"));
        SpendingAnalysis analysis = new SpendingAnalysis(
            largePrevious,
            largeCurrent,
            new BigDecimal("0.00"),
            analysisMonth
        );

        Amount changeAmount = analysis.getChangeAmount();
        assertEquals(new BigDecimal("0.02"), changeAmount.getValue());
    }

    @Test
    void should_HandleDecimalPrecision_When_CalculatingChangeAmount() {
        Amount precisionPrevious = new Amount(new BigDecimal("150.33"));
        Amount precisionCurrent = new Amount(new BigDecimal("275.67"));
        SpendingAnalysis analysis = new SpendingAnalysis(
            precisionPrevious,
            precisionCurrent,
            new BigDecimal("83.33"),
            analysisMonth
        );

        Amount changeAmount = analysis.getChangeAmount();
        assertEquals(new BigDecimal("125.34"), changeAmount.getValue());
    }

    @Test
    void should_HandleVerySmallChange_When_CalculatingChangeAmount() {
        Amount smallPrevious = new Amount(new BigDecimal("100.01"));
        Amount smallCurrent = new Amount(new BigDecimal("100.02"));
        SpendingAnalysis analysis = new SpendingAnalysis(
            smallPrevious,
            smallCurrent,
            new BigDecimal("0.01"),
            analysisMonth
        );

        Amount changeAmount = analysis.getChangeAmount();
        assertEquals(new BigDecimal("0.01"), changeAmount.getValue());
    }

    @Test
    void should_HandleNegativePercentageWithPositiveChange_When_DataInconsistent() {
        SpendingAnalysis analysis = new SpendingAnalysis(
            previousExpense,
            currentExpense,
            new BigDecimal("-5.00"),
            analysisMonth
        );

        Amount changeAmount = analysis.getChangeAmount();
        assertTrue(analysis.hasDecreasedSpending());
        assertEquals(new BigDecimal("300.00"), changeAmount.getValue());
    }
}