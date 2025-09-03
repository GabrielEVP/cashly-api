package com.cashly.cashly_api.incomes.domain.services;

import com.cashly.cashly_api.incomes.domain.entities.Income;
import com.cashly.cashly_api.incomes.domain.services.IncomeService.IncomeGrowthAnalysis;
import com.cashly.cashly_api.incomes.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IncomeServiceUnitTest {

    private IncomeService incomeService;
    private List<Income> testIncomes;
    private String validUserId;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        incomeService = new IncomeService();
        validUserId = "user123";
        startDate = LocalDate.of(2024, 1, 1);
        endDate = LocalDate.of(2024, 1, 31);
        
        testIncomes = Arrays.asList(
            new Income(
                IncomeId.generate(),
                new Amount(new BigDecimal("3000.00")),
                new Description("Salary"),
                new Category("SALARY"),
                LocalDate.of(2024, 1, 15),
                validUserId
            ),
            new Income(
                IncomeId.generate(),
                new Amount(new BigDecimal("500.00")),
                new Description("Freelance work"),
                new Category("BUSINESS"),
                LocalDate.of(2024, 1, 20),
                validUserId
            ),
            new Income(
                IncomeId.generate(),
                new Amount(new BigDecimal("200.00")),
                new Description("Investment returns"),
                new Category("INVESTMENT"),
                LocalDate.of(2024, 1, 25),
                validUserId
            ),
            new Income(
                IncomeId.generate(),
                new Amount(new BigDecimal("1000.00")),
                new Description("Other user income"),
                new Category("SALARY"),
                LocalDate.of(2024, 1, 10),
                "otherUser"
            )
        );
    }

    @Test
    void should_CalculateTotalIncome_When_ValidParametersProvided() {
        Amount result = incomeService.calculateTotalIncomeForPeriod(testIncomes, validUserId, startDate, endDate);
        
        assertEquals(new BigDecimal("3700.00"), result.getValue());
    }

    @Test
    void should_ReturnZeroAmount_When_NoIncomesInPeriod() {
        LocalDate futureStart = LocalDate.of(2024, 6, 1);
        LocalDate futureEnd = LocalDate.of(2024, 6, 30);
        
        Amount result = incomeService.calculateTotalIncomeForPeriod(testIncomes, validUserId, futureStart, futureEnd);
        
        assertEquals(BigDecimal.ZERO, result.getValue());
    }

    @Test
    void should_ReturnZeroAmount_When_NoIncomesForUser() {
        Amount result = incomeService.calculateTotalIncomeForPeriod(testIncomes, "nonexistentUser", startDate, endDate);
        
        assertEquals(BigDecimal.ZERO, result.getValue());
    }

    @Test
    void should_ThrowException_When_IncomesListIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.calculateTotalIncomeForPeriod(null, validUserId, startDate, endDate));
    }

    @Test
    void should_ThrowException_When_UserIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.calculateTotalIncomeForPeriod(testIncomes, null, startDate, endDate));
    }

    @Test
    void should_ThrowException_When_UserIdIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.calculateTotalIncomeForPeriod(testIncomes, "", startDate, endDate));
    }

    @Test
    void should_ThrowException_When_StartDateIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.calculateTotalIncomeForPeriod(testIncomes, validUserId, null, endDate));
    }

    @Test
    void should_ThrowException_When_EndDateIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.calculateTotalIncomeForPeriod(testIncomes, validUserId, startDate, null));
    }

    @Test
    void should_ThrowException_When_StartDateIsAfterEndDate() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.calculateTotalIncomeForPeriod(testIncomes, validUserId, endDate, startDate));
    }

    @Test
    void should_CalculateIncomeByCategory_When_ValidParametersProvided() {
        Map<Category, Amount> result = incomeService.calculateIncomeByCategory(testIncomes, validUserId, startDate, endDate);
        
        assertEquals(3, result.size());
        assertEquals(new BigDecimal("3000.00"), result.get(new Category("SALARY")).getValue());
        assertEquals(new BigDecimal("500.00"), result.get(new Category("BUSINESS")).getValue());
        assertEquals(new BigDecimal("200.00"), result.get(new Category("INVESTMENT")).getValue());
    }

    @Test
    void should_ReturnEmptyMap_When_NoIncomesInPeriodForCategory() {
        LocalDate futureStart = LocalDate.of(2024, 6, 1);
        LocalDate futureEnd = LocalDate.of(2024, 6, 30);
        
        Map<Category, Amount> result = incomeService.calculateIncomeByCategory(testIncomes, validUserId, futureStart, futureEnd);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void should_CalculateMonthlyAverage_When_ValidParametersProvided() {
        Amount result = incomeService.calculateMonthlyAverageIncome(testIncomes, validUserId, 3, LocalDate.of(2024, 1, 31));
        
        BigDecimal expected = new BigDecimal("3700.00").divide(BigDecimal.valueOf(3), 2, BigDecimal.ROUND_HALF_UP);
        assertEquals(expected, result.getValue());
    }

    @Test
    void should_ThrowException_When_MonthsIsZero() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.calculateMonthlyAverageIncome(testIncomes, validUserId, 0, LocalDate.now()));
    }

    @Test
    void should_ThrowException_When_MonthsIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.calculateMonthlyAverageIncome(testIncomes, validUserId, -1, LocalDate.now()));
    }

    @Test
    void should_ThrowException_When_ReferenceDateIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.calculateMonthlyAverageIncome(testIncomes, validUserId, 3, null));
    }

    @Test
    void should_AnalyzeIncomeGrowth_When_ValidParametersProvided() {
        YearMonth currentMonth = YearMonth.of(2024, 1);
        
        IncomeGrowthAnalysis result = incomeService.analyzeIncomeGrowth(testIncomes, validUserId, currentMonth);
        
        assertNotNull(result);
        assertEquals(currentMonth, result.getAnalysisMonth());
        assertEquals(new BigDecimal("3700.00"), result.getCurrentMonthIncome().getValue());
        assertEquals(BigDecimal.ZERO, result.getPreviousMonthIncome().getValue());
        assertEquals(new BigDecimal("100.00"), result.getGrowthPercentage());
    }

    @Test
    void should_ReturnZeroGrowth_When_BothMonthsHaveZeroIncome() {
        YearMonth futureMonth = YearMonth.of(2024, 6);
        
        IncomeGrowthAnalysis result = incomeService.analyzeIncomeGrowth(testIncomes, validUserId, futureMonth);
        
        assertEquals(BigDecimal.ZERO, result.getGrowthPercentage());
        assertEquals(BigDecimal.ZERO, result.getCurrentMonthIncome().getValue());
        assertEquals(BigDecimal.ZERO, result.getPreviousMonthIncome().getValue());
    }

    @Test
    void should_ThrowException_When_CurrentMonthIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            incomeService.analyzeIncomeGrowth(testIncomes, validUserId, null));
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

    @Test
    void should_CalculateCategoryPercentages_When_ValidParametersProvided() {
        Map<Category, BigDecimal> result = incomeService.calculateCategoryPercentages(testIncomes, validUserId, startDate, endDate);
        
        assertEquals(3, result.size());
        assertEquals(new BigDecimal("81.08"), result.get(new Category("SALARY")));
        assertEquals(new BigDecimal("13.51"), result.get(new Category("BUSINESS")));
        assertEquals(new BigDecimal("5.41"), result.get(new Category("INVESTMENT")));
    }

    @Test
    void should_ReturnZeroPercentages_When_NoIncomeInPeriod() {
        LocalDate futureStart = LocalDate.of(2024, 6, 1);
        LocalDate futureEnd = LocalDate.of(2024, 6, 30);
        
        Map<Category, BigDecimal> result = incomeService.calculateCategoryPercentages(testIncomes, validUserId, futureStart, futureEnd);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void should_HandleBoundaryDates_When_IncomeOnBoundaryDates() {
        Income boundaryIncome1 = new Income(
            IncomeId.generate(),
            new Amount(new BigDecimal("1000.00")),
            new Description("Start date income"),
            new Category("SALARY"),
            startDate,
            validUserId
        );
        
        Income boundaryIncome2 = new Income(
            IncomeId.generate(),
            new Amount(new BigDecimal("2000.00")),
            new Description("End date income"),
            new Category("SALARY"),
            endDate,
            validUserId
        );
        
        List<Income> boundaryIncomes = Arrays.asList(boundaryIncome1, boundaryIncome2);
        
        Amount result = incomeService.calculateTotalIncomeForPeriod(boundaryIncomes, validUserId, startDate, endDate);
        
        assertEquals(new BigDecimal("3000.00"), result.getValue());
    }
}