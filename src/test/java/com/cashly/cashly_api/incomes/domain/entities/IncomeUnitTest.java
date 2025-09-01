package com.cashly.cashly_api.incomes.domain.entities;

import com.cashly.cashly_api.incomes.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class IncomeUnitTest {

    private IncomeId validIncomeId;
    private Amount validAmount;
    private Description validDescription;
    private Category validCategory;
    private LocalDate validDate;
    private String validUserId;

    @BeforeEach
    void setUp() {
        validIncomeId = IncomeId.generate();
        validAmount = new Amount(new BigDecimal("1500.00"));
        validDescription = new Description("Monthly salary");
        validCategory = new Category("SALARY");
        validDate = LocalDate.of(2024, 1, 15);
        validUserId = "user123";
    }

    @Test
    void should_CreateIncome_When_AllValidParametersProvided() {
        // Act
        Income income = new Income(
            validIncomeId,
            validAmount,
            validDescription,
            validCategory,
            validDate,
            validUserId
        );

        // Assert
        assertNotNull(income);
        assertEquals(validIncomeId, income.getId());
        assertEquals(validAmount, income.getAmount());
        assertEquals(validDescription, income.getDescription());
        assertEquals(validCategory, income.getCategory());
        assertEquals(validDate, income.getDate());
        assertEquals(validUserId, income.getUserId());
        assertNotNull(income.getCreatedAt());
        assertNotNull(income.getUpdatedAt());
    }

    @Test
    void should_CreateIncomeWithCurrentTimestamps_When_Created() {
        // Act
        Income income = new Income(
            validIncomeId,
            validAmount,
            validDescription,
            validCategory,
            validDate,
            validUserId
        );

        // Assert
        assertNotNull(income.getCreatedAt());
        assertNotNull(income.getUpdatedAt());
        // Timestamps should be very close (within 1 second)
        assertTrue(Math.abs(income.getCreatedAt().toEpochSecond(java.time.ZoneOffset.UTC) - 
                           income.getUpdatedAt().toEpochSecond(java.time.ZoneOffset.UTC)) <= 1);
    }

    @Test
    void should_ThrowException_When_NullIncomeIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Income(null, validAmount, validDescription, validCategory, validDate, validUserId)
        );

        assertEquals("Income ID cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullAmountProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Income(validIncomeId, null, validDescription, validCategory, validDate, validUserId)
        );

        assertEquals("Amount cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullDescriptionProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Income(validIncomeId, validAmount, null, validCategory, validDate, validUserId)
        );

        assertEquals("Description cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullCategoryProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Income(validIncomeId, validAmount, validDescription, null, validDate, validUserId)
        );

        assertEquals("Category cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullDateProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Income(validIncomeId, validAmount, validDescription, validCategory, null, validUserId)
        );

        assertEquals("Date cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullUserIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Income(validIncomeId, validAmount, validDescription, validCategory, validDate, null)
        );

        assertEquals("User ID cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_EmptyUserIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Income(validIncomeId, validAmount, validDescription, validCategory, validDate, "")
        );

        assertEquals("User ID cannot be empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_BlankUserIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Income(validIncomeId, validAmount, validDescription, validCategory, validDate, "   ")
        );

        assertEquals("User ID cannot be empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_FutureDateProvided() {
        // Arrange
        LocalDate futureDate = LocalDate.now().plusDays(1);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Income(validIncomeId, validAmount, validDescription, validCategory, futureDate, validUserId)
        );

        assertEquals("Income date cannot be in the future", exception.getMessage());
    }

    @Test
    void should_UpdateAmount_When_ValidAmountProvided() {
        // Arrange
        Income income = new Income(validIncomeId, validAmount, validDescription, validCategory, validDate, validUserId);
        Amount newAmount = new Amount(new BigDecimal("2000.00"));

        // Act
        income.updateAmount(newAmount);

        // Assert
        assertEquals(newAmount, income.getAmount());
        assertNotEquals(income.getCreatedAt(), income.getUpdatedAt());
    }

    @Test
    void should_UpdateDescription_When_ValidDescriptionProvided() {
        // Arrange
        Income income = new Income(validIncomeId, validAmount, validDescription, validCategory, validDate, validUserId);
        Description newDescription = new Description("Updated salary");

        // Act
        income.updateDescription(newDescription);

        // Assert
        assertEquals(newDescription, income.getDescription());
        assertNotEquals(income.getCreatedAt(), income.getUpdatedAt());
    }

    @Test
    void should_UpdateCategory_When_ValidCategoryProvided() {
        // Arrange
        Income income = new Income(validIncomeId, validAmount, validDescription, validCategory, validDate, validUserId);
        Category newCategory = new Category("BUSINESS");

        // Act
        income.updateCategory(newCategory);

        // Assert
        assertEquals(newCategory, income.getCategory());
        assertNotEquals(income.getCreatedAt(), income.getUpdatedAt());
    }

    @Test
    void should_BeEqual_When_SameIncomeId() {
        // Arrange
        Income income1 = new Income(validIncomeId, validAmount, validDescription, validCategory, validDate, validUserId);
        Income income2 = new Income(validIncomeId, new Amount(new BigDecimal("999.99")), new Description("Different"), new Category("OTHER"), validDate, validUserId);

        // Act & Assert
        assertEquals(income1, income2);
        assertEquals(income1.hashCode(), income2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentIncomeIds() {
        // Arrange
        Income income1 = new Income(IncomeId.generate(), validAmount, validDescription, validCategory, validDate, validUserId);
        Income income2 = new Income(IncomeId.generate(), validAmount, validDescription, validCategory, validDate, validUserId);

        // Act & Assert
        assertNotEquals(income1, income2);
    }

    @Test
    void should_ReturnTrue_When_BelongsToUser() {
        // Arrange
        Income income = new Income(validIncomeId, validAmount, validDescription, validCategory, validDate, validUserId);

        // Act & Assert
        assertTrue(income.belongsToUser(validUserId));
        assertFalse(income.belongsToUser("differentUser"));
    }

    @Test
    void should_ReturnTrue_When_IsFromSamePeriod() {
        // Arrange
        LocalDate dateInJanuary = LocalDate.of(2024, 1, 10);
        LocalDate anotherDateInJanuary = LocalDate.of(2024, 1, 20);
        LocalDate dateInFebruary = LocalDate.of(2024, 2, 10);
        
        Income incomeInJanuary = new Income(validIncomeId, validAmount, validDescription, validCategory, dateInJanuary, validUserId);

        // Act & Assert
        assertTrue(incomeInJanuary.isFromSamePeriod(anotherDateInJanuary));
        assertFalse(incomeInJanuary.isFromSamePeriod(dateInFebruary));
    }
}