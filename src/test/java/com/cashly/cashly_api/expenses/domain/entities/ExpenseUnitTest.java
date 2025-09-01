package com.cashly.cashly_api.expenses.domain.entities;

import com.cashly.cashly_api.expenses.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseUnitTest {

    private ExpenseId validExpenseId;
    private Amount validAmount;
    private Description validDescription;
    private Category validCategory;
    private LocalDate validDate;
    private String validUserId;

    @BeforeEach
    void setUp() {
        validExpenseId = ExpenseId.generate();
        validAmount = new Amount(new BigDecimal("75.50"));
        validDescription = new Description("Grocery shopping");
        validCategory = new Category("FOOD_DINING");
        validDate = LocalDate.of(2024, 1, 15);
        validUserId = "user123";
    }

    @Test
    void should_CreateExpense_When_AllValidParametersProvided() {
        // Act
        Expense expense = new Expense(
            validExpenseId,
            validAmount,
            validDescription,
            validCategory,
            validDate,
            validUserId
        );

        // Assert
        assertNotNull(expense);
        assertEquals(validExpenseId, expense.getId());
        assertEquals(validAmount, expense.getAmount());
        assertEquals(validDescription, expense.getDescription());
        assertEquals(validCategory, expense.getCategory());
        assertEquals(validDate, expense.getDate());
        assertEquals(validUserId, expense.getUserId());
        assertNotNull(expense.getCreatedAt());
        assertNotNull(expense.getUpdatedAt());
    }

    @Test
    void should_CreateExpenseWithCurrentTimestamps_When_Created() {
        // Act
        Expense expense = new Expense(
            validExpenseId,
            validAmount,
            validDescription,
            validCategory,
            validDate,
            validUserId
        );

        // Assert
        assertNotNull(expense.getCreatedAt());
        assertNotNull(expense.getUpdatedAt());
        // Timestamps should be very close (within 1 second)
        assertTrue(Math.abs(expense.getCreatedAt().toEpochSecond(java.time.ZoneOffset.UTC) - 
                           expense.getUpdatedAt().toEpochSecond(java.time.ZoneOffset.UTC)) <= 1);
    }

    @Test
    void should_ThrowException_When_NullExpenseIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Expense(null, validAmount, validDescription, validCategory, validDate, validUserId)
        );

        assertEquals("Expense ID cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullAmountProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Expense(validExpenseId, null, validDescription, validCategory, validDate, validUserId)
        );

        assertEquals("Amount cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullDescriptionProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Expense(validExpenseId, validAmount, null, validCategory, validDate, validUserId)
        );

        assertEquals("Description cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullCategoryProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Expense(validExpenseId, validAmount, validDescription, null, validDate, validUserId)
        );

        assertEquals("Category cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullDateProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Expense(validExpenseId, validAmount, validDescription, validCategory, null, validUserId)
        );

        assertEquals("Date cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullUserIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Expense(validExpenseId, validAmount, validDescription, validCategory, validDate, null)
        );

        assertEquals("User ID cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_EmptyUserIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Expense(validExpenseId, validAmount, validDescription, validCategory, validDate, "")
        );

        assertEquals("User ID cannot be empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_BlankUserIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Expense(validExpenseId, validAmount, validDescription, validCategory, validDate, "   ")
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
            () -> new Expense(validExpenseId, validAmount, validDescription, validCategory, futureDate, validUserId)
        );

        assertEquals("Expense date cannot be in the future", exception.getMessage());
    }

    @Test
    void should_UpdateAmount_When_ValidAmountProvided() {
        // Arrange
        Expense expense = new Expense(validExpenseId, validAmount, validDescription, validCategory, validDate, validUserId);
        Amount newAmount = new Amount(new BigDecimal("125.75"));

        // Act
        expense.updateAmount(newAmount);

        // Assert
        assertEquals(newAmount, expense.getAmount());
        assertNotEquals(expense.getCreatedAt(), expense.getUpdatedAt());
    }

    @Test
    void should_UpdateDescription_When_ValidDescriptionProvided() {
        // Arrange
        Expense expense = new Expense(validExpenseId, validAmount, validDescription, validCategory, validDate, validUserId);
        Description newDescription = new Description("Restaurant dinner");

        // Act
        expense.updateDescription(newDescription);

        // Assert
        assertEquals(newDescription, expense.getDescription());
        assertNotEquals(expense.getCreatedAt(), expense.getUpdatedAt());
    }

    @Test
    void should_UpdateCategory_When_ValidCategoryProvided() {
        // Arrange
        Expense expense = new Expense(validExpenseId, validAmount, validDescription, validCategory, validDate, validUserId);
        Category newCategory = new Category("ENTERTAINMENT");

        // Act
        expense.updateCategory(newCategory);

        // Assert
        assertEquals(newCategory, expense.getCategory());
        assertNotEquals(expense.getCreatedAt(), expense.getUpdatedAt());
    }

    @Test
    void should_BeEqual_When_SameExpenseId() {
        // Arrange
        Expense expense1 = new Expense(validExpenseId, validAmount, validDescription, validCategory, validDate, validUserId);
        Expense expense2 = new Expense(validExpenseId, new Amount(new BigDecimal("999.99")), new Description("Different"), new Category("OTHER"), validDate, validUserId);

        // Act & Assert
        assertEquals(expense1, expense2);
        assertEquals(expense1.hashCode(), expense2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentExpenseIds() {
        // Arrange
        Expense expense1 = new Expense(ExpenseId.generate(), validAmount, validDescription, validCategory, validDate, validUserId);
        Expense expense2 = new Expense(ExpenseId.generate(), validAmount, validDescription, validCategory, validDate, validUserId);

        // Act & Assert
        assertNotEquals(expense1, expense2);
    }

    @Test
    void should_ReturnTrue_When_BelongsToUser() {
        // Arrange
        Expense expense = new Expense(validExpenseId, validAmount, validDescription, validCategory, validDate, validUserId);

        // Act & Assert
        assertTrue(expense.belongsToUser(validUserId));
        assertFalse(expense.belongsToUser("differentUser"));
    }

    @Test
    void should_ReturnTrue_When_IsFromSamePeriod() {
        // Arrange
        LocalDate dateInJanuary = LocalDate.of(2024, 1, 10);
        LocalDate anotherDateInJanuary = LocalDate.of(2024, 1, 20);
        LocalDate dateInFebruary = LocalDate.of(2024, 2, 10);
        
        Expense expenseInJanuary = new Expense(validExpenseId, validAmount, validDescription, validCategory, dateInJanuary, validUserId);

        // Act & Assert
        assertTrue(expenseInJanuary.isFromSamePeriod(anotherDateInJanuary));
        assertFalse(expenseInJanuary.isFromSamePeriod(dateInFebruary));
    }
}