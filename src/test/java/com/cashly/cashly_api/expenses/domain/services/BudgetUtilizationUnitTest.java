package com.cashly.cashly_api.expenses.domain.services;

import com.cashly.cashly_api.expenses.domain.services.ExpenseService.BudgetUtilization;
import com.cashly.cashly_api.expenses.domain.valueobjects.Amount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BudgetUtilizationUnitTest {

    private Amount budgetLimit;
    private Amount actualExpense;
    private Amount remaining;
    private BigDecimal utilizationPercentage;
    private boolean isOverBudget;

    @BeforeEach
    void setUp() {
        budgetLimit = new Amount(new BigDecimal("2000.00"));
        actualExpense = new Amount(new BigDecimal("1500.00"));
        remaining = new Amount(new BigDecimal("500.00"));
        utilizationPercentage = new BigDecimal("75.00");
        isOverBudget = false;
    }

    @Test
    void should_CreateBudgetUtilization_When_ValidParametersProvided() {
        BudgetUtilization utilization = new BudgetUtilization(
            budgetLimit,
            actualExpense,
            remaining,
            utilizationPercentage,
            isOverBudget
        );

        assertNotNull(utilization);
        assertEquals(budgetLimit, utilization.getBudgetLimit());
        assertEquals(actualExpense, utilization.getActualExpense());
        assertEquals(remaining, utilization.getRemaining());
        assertEquals(utilizationPercentage, utilization.getUtilizationPercentage());
        assertEquals(isOverBudget, utilization.isOverBudget());
    }

    @Test
    void should_CreateBudgetUtilization_When_ExactlyOnBudget() {
        Amount onBudgetExpense = new Amount(new BigDecimal("2000.00"));
        Amount zeroRemaining = new Amount(BigDecimal.ZERO);
        BigDecimal hundredPercent = new BigDecimal("100.00");
        
        BudgetUtilization utilization = new BudgetUtilization(
            budgetLimit,
            onBudgetExpense,
            zeroRemaining,
            hundredPercent,
            false
        );

        assertNotNull(utilization);
        assertEquals(budgetLimit, utilization.getBudgetLimit());
        assertEquals(onBudgetExpense, utilization.getActualExpense());
        assertEquals(zeroRemaining, utilization.getRemaining());
        assertEquals(hundredPercent, utilization.getUtilizationPercentage());
        assertFalse(utilization.isOverBudget());
        assertEquals(BigDecimal.ZERO, utilization.getOverspendAmount().getValue());
    }

    @Test
    void should_CreateBudgetUtilization_When_OverBudget() {
        Amount overBudgetExpense = new Amount(new BigDecimal("2500.00"));
        Amount zeroRemaining = new Amount(BigDecimal.ZERO);
        BigDecimal overPercentage = new BigDecimal("125.00");
        
        BudgetUtilization utilization = new BudgetUtilization(
            budgetLimit,
            overBudgetExpense,
            zeroRemaining,
            overPercentage,
            true
        );

        assertNotNull(utilization);
        assertEquals(budgetLimit, utilization.getBudgetLimit());
        assertEquals(overBudgetExpense, utilization.getActualExpense());
        assertEquals(zeroRemaining, utilization.getRemaining());
        assertEquals(overPercentage, utilization.getUtilizationPercentage());
        assertTrue(utilization.isOverBudget());
        assertEquals(new BigDecimal("500.00"), utilization.getOverspendAmount().getValue());
    }

    @Test
    void should_ReturnZeroOverspend_When_NotOverBudget() {
        BudgetUtilization utilization = new BudgetUtilization(
            budgetLimit,
            actualExpense,
            remaining,
            utilizationPercentage,
            false
        );

        Amount overspend = utilization.getOverspendAmount();
        assertEquals(BigDecimal.ZERO, overspend.getValue());
    }

    @Test
    void should_CalculateOverspendAmount_When_OverBudget() {
        Amount overBudgetExpense = new Amount(new BigDecimal("2300.00"));
        Amount zeroRemaining = new Amount(BigDecimal.ZERO);
        
        BudgetUtilization utilization = new BudgetUtilization(
            budgetLimit,
            overBudgetExpense,
            zeroRemaining,
            new BigDecimal("115.00"),
            true
        );

        Amount overspend = utilization.getOverspendAmount();
        assertEquals(new BigDecimal("300.00"), overspend.getValue());
    }

    @Test
    void should_HandleZeroBudgetLimit_When_CreatingUtilization() {
        Amount zeroBudget = new Amount(BigDecimal.ZERO);
        Amount someExpense = new Amount(new BigDecimal("100.00"));
        
        BudgetUtilization utilization = new BudgetUtilization(
            zeroBudget,
            someExpense,
            new Amount(BigDecimal.ZERO),
            new BigDecimal("100.00"),
            true
        );

        assertNotNull(utilization);
        assertEquals(zeroBudget, utilization.getBudgetLimit());
        assertEquals(someExpense, utilization.getActualExpense());
        assertTrue(utilization.isOverBudget());
        assertEquals(new BigDecimal("100.00"), utilization.getOverspendAmount().getValue());
    }

    @Test
    void should_HandleZeroActualExpense_When_CreatingUtilization() {
        Amount zeroExpense = new Amount(BigDecimal.ZERO);
        
        BudgetUtilization utilization = new BudgetUtilization(
            budgetLimit,
            zeroExpense,
            budgetLimit,
            BigDecimal.ZERO,
            false
        );

        assertNotNull(utilization);
        assertEquals(budgetLimit, utilization.getBudgetLimit());
        assertEquals(zeroExpense, utilization.getActualExpense());
        assertEquals(budgetLimit, utilization.getRemaining());
        assertEquals(BigDecimal.ZERO, utilization.getUtilizationPercentage());
        assertFalse(utilization.isOverBudget());
        assertEquals(BigDecimal.ZERO, utilization.getOverspendAmount().getValue());
    }

    @Test
    void should_HandleVeryLargeNumbers_When_CreatingUtilization() {
        Amount largeBudget = new Amount(new BigDecimal("999999999.99"));
        Amount largeExpense = new Amount(new BigDecimal("500000000.00"));
        Amount largeRemaining = new Amount(new BigDecimal("499999999.99"));
        BigDecimal largePercentage = new BigDecimal("50.00");
        
        BudgetUtilization utilization = new BudgetUtilization(
            largeBudget,
            largeExpense,
            largeRemaining,
            largePercentage,
            false
        );

        assertNotNull(utilization);
        assertEquals(largeBudget, utilization.getBudgetLimit());
        assertEquals(largeExpense, utilization.getActualExpense());
        assertEquals(largeRemaining, utilization.getRemaining());
        assertEquals(largePercentage, utilization.getUtilizationPercentage());
        assertFalse(utilization.isOverBudget());
    }

    @Test
    void should_HandleDecimalPrecision_When_CreatingUtilization() {
        Amount precisionBudget = new Amount(new BigDecimal("1000.33"));
        Amount precisionExpense = new Amount(new BigDecimal("750.25"));
        Amount precisionRemaining = new Amount(new BigDecimal("250.08"));
        BigDecimal precisionPercentage = new BigDecimal("75.01");
        
        BudgetUtilization utilization = new BudgetUtilization(
            precisionBudget,
            precisionExpense,
            precisionRemaining,
            precisionPercentage,
            false
        );

        assertNotNull(utilization);
        assertEquals(precisionBudget, utilization.getBudgetLimit());
        assertEquals(precisionExpense, utilization.getActualExpense());
        assertEquals(precisionRemaining, utilization.getRemaining());
        assertEquals(precisionPercentage, utilization.getUtilizationPercentage());
        assertFalse(utilization.isOverBudget());
    }

    @Test
    void should_ProduceCorrectToString_When_Called() {
        BudgetUtilization utilization = new BudgetUtilization(
            budgetLimit,
            actualExpense,
            remaining,
            utilizationPercentage,
            isOverBudget
        );

        String result = utilization.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("BudgetUtilization"));
        assertTrue(result.contains(budgetLimit.toString()));
        assertTrue(result.contains(actualExpense.toString()));
        assertTrue(result.contains(remaining.toString()));
        assertTrue(result.contains(utilizationPercentage.toString()));
        assertTrue(result.contains(String.valueOf(isOverBudget)));
    }

    @Test
    void should_HandleMaxOverspendScenario_When_OverBudget() {
        Amount smallBudget = new Amount(new BigDecimal("100.00"));
        Amount hugeExpense = new Amount(new BigDecimal("10000.00"));
        
        BudgetUtilization utilization = new BudgetUtilization(
            smallBudget,
            hugeExpense,
            new Amount(BigDecimal.ZERO),
            new BigDecimal("10000.00"),
            true
        );

        Amount overspend = utilization.getOverspendAmount();
        assertEquals(new BigDecimal("9900.00"), overspend.getValue());
        assertTrue(utilization.isOverBudget());
    }

    @Test
    void should_HandleMinimalOverspendScenario_When_SlightlyOverBudget() {
        Amount budgetAmount = new Amount(new BigDecimal("1000.00"));
        Amount slightlyOverExpense = new Amount(new BigDecimal("1000.01"));
        
        BudgetUtilization utilization = new BudgetUtilization(
            budgetAmount,
            slightlyOverExpense,
            new Amount(BigDecimal.ZERO),
            new BigDecimal("100.00"),
            true
        );

        Amount overspend = utilization.getOverspendAmount();
        assertEquals(new BigDecimal("0.01"), overspend.getValue());
        assertTrue(utilization.isOverBudget());
    }

    @Test
    void should_MaintainImmutability_When_AccessingProperties() {
        BudgetUtilization utilization = new BudgetUtilization(
            budgetLimit,
            actualExpense,
            remaining,
            utilizationPercentage,
            isOverBudget
        );

        Amount retrievedBudget = utilization.getBudgetLimit();
        Amount retrievedExpense = utilization.getActualExpense();
        Amount retrievedRemaining = utilization.getRemaining();

        assertEquals(budgetLimit, retrievedBudget);
        assertEquals(actualExpense, retrievedExpense);
        assertEquals(remaining, retrievedRemaining);
        
        assertNotSame(budgetLimit, retrievedBudget);
        assertNotSame(actualExpense, retrievedExpense);
        assertNotSame(remaining, retrievedRemaining);
    }
}