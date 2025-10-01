package com.cashly.cashly_api.expenses.domain.services;

import com.cashly.cashly_api.expenses.domain.services.ExpenseService.CategoryAnalysis;
import com.cashly.cashly_api.expenses.domain.valueobjects.Amount;
import com.cashly.cashly_api.expenses.domain.valueobjects.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CategoryAnalysisUnitTest {

    private Category testCategory;
    private Amount testAmount;
    private BigDecimal testPercentage;

    @BeforeEach
    void setUp() {
        testCategory = new Category("FOOD_DINING");
        testAmount = new Amount(new BigDecimal("1200.00"));
        testPercentage = new BigDecimal("45.50");
    }

    @Test
    void should_CreateCategoryAnalysis_When_ValidParametersProvided() {
        CategoryAnalysis analysis = new CategoryAnalysis(
            testCategory,
            testAmount,
            testPercentage
        );

        assertNotNull(analysis);
        assertEquals(testCategory, analysis.getCategory());
        assertEquals(testAmount, analysis.getAmount());
        assertEquals(testPercentage, analysis.getPercentageOfTotal());
    }

    @Test
    void should_CreateCategoryAnalysis_When_CategoryIsNull() {
        CategoryAnalysis analysis = new CategoryAnalysis(
            null,
            testAmount,
            testPercentage
        );

        assertNotNull(analysis);
        assertNull(analysis.getCategory());
        assertEquals(testAmount, analysis.getAmount());
        assertEquals(testPercentage, analysis.getPercentageOfTotal());
    }

    @Test
    void should_CreateCategoryAnalysis_When_AmountIsZero() {
        Amount zeroAmount = new Amount(BigDecimal.ZERO);
        CategoryAnalysis analysis = new CategoryAnalysis(
            testCategory,
            zeroAmount,
            BigDecimal.ZERO
        );

        assertNotNull(analysis);
        assertEquals(testCategory, analysis.getCategory());
        assertEquals(zeroAmount, analysis.getAmount());
        assertEquals(BigDecimal.ZERO, analysis.getPercentageOfTotal());
    }

    @Test
    void should_CreateCategoryAnalysis_When_PercentageIsZero() {
        CategoryAnalysis analysis = new CategoryAnalysis(
            testCategory,
            testAmount,
            BigDecimal.ZERO
        );

        assertNotNull(analysis);
        assertEquals(testCategory, analysis.getCategory());
        assertEquals(testAmount, analysis.getAmount());
        assertEquals(BigDecimal.ZERO, analysis.getPercentageOfTotal());
    }

    @Test
    void should_CreateCategoryAnalysis_When_PercentageIsHundred() {
        BigDecimal hundredPercent = new BigDecimal("100.00");
        CategoryAnalysis analysis = new CategoryAnalysis(
            testCategory,
            testAmount,
            hundredPercent
        );

        assertNotNull(analysis);
        assertEquals(testCategory, analysis.getCategory());
        assertEquals(testAmount, analysis.getAmount());
        assertEquals(hundredPercent, analysis.getPercentageOfTotal());
    }

    @Test
    void should_HandleVeryLargeAmount_When_CreatingAnalysis() {
        Amount largeAmount = new Amount(new BigDecimal("999999999.99"));
        BigDecimal largePercentage = new BigDecimal("99.99");
        
        CategoryAnalysis analysis = new CategoryAnalysis(
            testCategory,
            largeAmount,
            largePercentage
        );

        assertNotNull(analysis);
        assertEquals(testCategory, analysis.getCategory());
        assertEquals(largeAmount, analysis.getAmount());
        assertEquals(largePercentage, analysis.getPercentageOfTotal());
    }

    @Test
    void should_HandleVerySmallAmount_When_CreatingAnalysis() {
        Amount smallAmount = new Amount(new BigDecimal("0.01"));
        BigDecimal smallPercentage = new BigDecimal("0.01");
        
        CategoryAnalysis analysis = new CategoryAnalysis(
            testCategory,
            smallAmount,
            smallPercentage
        );

        assertNotNull(analysis);
        assertEquals(testCategory, analysis.getCategory());
        assertEquals(smallAmount, analysis.getAmount());
        assertEquals(smallPercentage, analysis.getPercentageOfTotal());
    }

    @Test
    void should_HandleHighPrecisionPercentage_When_CreatingAnalysis() {
        BigDecimal precisePercentage = new BigDecimal("33.3333");
        
        CategoryAnalysis analysis = new CategoryAnalysis(
            testCategory,
            testAmount,
            precisePercentage
        );

        assertNotNull(analysis);
        assertEquals(testCategory, analysis.getCategory());
        assertEquals(testAmount, analysis.getAmount());
        assertEquals(precisePercentage, analysis.getPercentageOfTotal());
    }

    @Test
    void should_ProduceCorrectToString_When_Called() {
        CategoryAnalysis analysis = new CategoryAnalysis(
            testCategory,
            testAmount,
            testPercentage
        );

        String result = analysis.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("CategoryAnalysis"));
        assertTrue(result.contains(testCategory.toString()));
        assertTrue(result.contains(testAmount.toString()));
        assertTrue(result.contains(testPercentage.toString()));
    }

    @Test
    void should_ProduceCorrectToString_When_CategoryIsNull() {
        CategoryAnalysis analysis = new CategoryAnalysis(
            null,
            testAmount,
            testPercentage
        );

        String result = analysis.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("CategoryAnalysis"));
        assertTrue(result.contains("null"));
        assertTrue(result.contains(testAmount.toString()));
        assertTrue(result.contains(testPercentage.toString()));
    }

    @Test
    void should_HandleDifferentCategoryTypes_When_CreatingAnalysis() {
        Category housingCategory = new Category("HOUSING");
        CategoryAnalysis analysis = new CategoryAnalysis(
            housingCategory,
            testAmount,
            testPercentage
        );

        assertNotNull(analysis);
        assertEquals(housingCategory, analysis.getCategory());
        assertEquals(testAmount, analysis.getAmount());
        assertEquals(testPercentage, analysis.getPercentageOfTotal());
    }

    @Test
    void should_ThrowException_When_CreatingAnalysisWithNegativeAmount() {
        BigDecimal negativePercentage = new BigDecimal("-10.00");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Amount(new BigDecimal("-500.00"))
        );

        assertEquals("Amount cannot be negative", exception.getMessage());
    }

    @Test
    void should_HandleEmptyAnalysisScenario_When_NoDataAvailable() {
        Amount zeroAmount = new Amount(BigDecimal.ZERO);
        BigDecimal zeroPercentage = BigDecimal.ZERO;
        
        CategoryAnalysis emptyAnalysis = new CategoryAnalysis(
            null,
            zeroAmount,
            zeroPercentage
        );

        assertNotNull(emptyAnalysis);
        assertNull(emptyAnalysis.getCategory());
        assertEquals(zeroAmount, emptyAnalysis.getAmount());
        assertEquals(zeroPercentage, emptyAnalysis.getPercentageOfTotal());
    }

    @Test
    void should_MaintainImmutability_When_AccessingProperties() {
        CategoryAnalysis analysis = new CategoryAnalysis(
            testCategory,
            testAmount,
            testPercentage
        );

        Category retrievedCategory = analysis.getCategory();
        Amount retrievedAmount = analysis.getAmount();
        BigDecimal retrievedPercentage = analysis.getPercentageOfTotal();

        assertEquals(testCategory, retrievedCategory);
        assertEquals(testAmount, retrievedAmount);
        assertEquals(testPercentage, retrievedPercentage);
        
        assertNotSame(testCategory, retrievedCategory);
        assertNotSame(testAmount, retrievedAmount);
        assertNotSame(testPercentage, retrievedPercentage);
    }
}