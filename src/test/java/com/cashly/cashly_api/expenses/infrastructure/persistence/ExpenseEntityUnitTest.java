package com.cashly.cashly_api.expenses.infrastructure.persistence;

import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseEntityUnitTest {

    private Expense testExpense;
    private ExpenseEntity testEntity;
    private String testId;
    private BigDecimal testAmount;
    private String testDescription;
    private String testCategory;
    private LocalDate testDate;
    private String testUserId;
    private LocalDateTime testCreatedAt;
    private LocalDateTime testUpdatedAt;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID().toString();
        testAmount = new BigDecimal("750.00");
        testDescription = "Test expense";
        testCategory = "FOOD_DINING";
        testDate = LocalDate.of(2024, 1, 15);
        testUserId = "user123";
        testCreatedAt = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
        testUpdatedAt = LocalDateTime.of(2024, 1, 16, 14, 45, 0);

        testExpense = new Expense(
            new ExpenseId(UUID.fromString(testId)),
            new Amount(testAmount),
            new Description(testDescription),
            new Category(testCategory),
            testDate,
            testUserId
        );

        testEntity = new ExpenseEntity();
        testEntity.setId(testId);
        testEntity.setAmount(testAmount);
        testEntity.setDescription(testDescription);
        testEntity.setCategory(testCategory);
        testEntity.setDate(testDate);
        testEntity.setUserId(testUserId);
        testEntity.setCreatedAt(testCreatedAt);
        testEntity.setUpdatedAt(testUpdatedAt);
    }

    @Test
    void should_CreateExpenseEntity_When_DefaultConstructorCalled() {
        ExpenseEntity entity = new ExpenseEntity();
        
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getAmount());
        assertNull(entity.getDescription());
        assertNull(entity.getCategory());
        assertNull(entity.getDate());
        assertNull(entity.getUserId());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    void should_CreateEntityFromDomain_When_ValidExpenseProvided() {
        ExpenseEntity entity = ExpenseEntity.fromDomain(testExpense);
        
        assertNotNull(entity);
        assertEquals(testId, entity.getId());
        assertEquals(testAmount, entity.getAmount());
        assertEquals(testDescription, entity.getDescription());
        assertEquals(testCategory, entity.getCategory());
        assertEquals(testDate, entity.getDate());
        assertEquals(testUserId, entity.getUserId());
        assertEquals(testExpense.getCreatedAt(), entity.getCreatedAt());
        assertEquals(testExpense.getUpdatedAt(), entity.getUpdatedAt());
    }

    @Test
    void should_ThrowException_When_CreatingEntityFromNullExpense() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ExpenseEntity.fromDomain(null)
        );
        
        assertEquals("Expense cannot be null", exception.getMessage());
    }

    @Test
    void should_UpdateEntityFromDomain_When_ValidExpenseProvided() {
        Expense updatedExpense = new Expense(
            new ExpenseId(UUID.fromString(testId)),
            new Amount(new BigDecimal("900.00")),
            new Description("Updated expense"),
            new Category("TRANSPORTATION"),
            testDate,
            testUserId
        );
        
        testEntity.updateFromDomain(updatedExpense);
        
        assertEquals(new BigDecimal("900.00"), testEntity.getAmount());
        assertEquals("Updated expense", testEntity.getDescription());
        assertEquals("TRANSPORTATION", testEntity.getCategory());
        assertEquals(updatedExpense.getUpdatedAt(), testEntity.getUpdatedAt());
    }

    @Test
    void should_ThrowException_When_UpdatingFromNullExpense() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> testEntity.updateFromDomain(null)
        );
        
        assertEquals("Expense cannot be null", exception.getMessage());
    }

    @Test
    void should_ConvertToDomain_When_EntityHasValidData() {
        Expense domainExpense = testEntity.toDomain();
        
        assertNotNull(domainExpense);
        assertEquals(testId, domainExpense.getId().getValue().toString());
        assertEquals(testAmount, domainExpense.getAmount().getValue());
        assertEquals(testDescription, domainExpense.getDescription().getValue());
        assertEquals(testCategory, domainExpense.getCategory().getValue());
        assertEquals(testDate, domainExpense.getDate());
        assertEquals(testUserId, domainExpense.getUserId());
        assertEquals(testCreatedAt, domainExpense.getCreatedAt());
        assertEquals(testUpdatedAt, domainExpense.getUpdatedAt());
    }

    @Test
    void should_HandleZeroAmount_When_ConvertingToDomain() {
        testEntity.setAmount(BigDecimal.ZERO);
        
        Expense domainExpense = testEntity.toDomain();
        
        assertNotNull(domainExpense);
        assertEquals(BigDecimal.ZERO, domainExpense.getAmount().getValue());
    }

    @Test
    void should_HandleLargeAmount_When_ConvertingToDomain() {
        BigDecimal largeAmount = new BigDecimal("999999999.99");
        testEntity.setAmount(largeAmount);
        
        Expense domainExpense = testEntity.toDomain();
        
        assertNotNull(domainExpense);
        assertEquals(largeAmount, domainExpense.getAmount().getValue());
    }

    @Test
    void should_ThrowException_When_ConvertingEmptyDescriptionToDomain() {
        testEntity.setDescription("");
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> testEntity.toDomain()
        );
        
        assertEquals("Description cannot be empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_ConvertingEmptyCategoryToDomain() {
        testEntity.setCategory("");
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> testEntity.toDomain()
        );
        
        assertEquals("Category cannot be empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_ConvertingEmptyUserIdToDomain() {
        testEntity.setUserId("");
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> testEntity.toDomain()
        );
        
        assertEquals("User ID cannot be empty", exception.getMessage());
    }

    @Test
    void should_SetAndGetId_When_ValidIdProvided() {
        String newId = UUID.randomUUID().toString();
        testEntity.setId(newId);
        
        assertEquals(newId, testEntity.getId());
    }

    @Test
    void should_SetAndGetAmount_When_ValidAmountProvided() {
        BigDecimal newAmount = new BigDecimal("1200.50");
        testEntity.setAmount(newAmount);
        
        assertEquals(newAmount, testEntity.getAmount());
    }

    @Test
    void should_SetAndGetDescription_When_ValidDescriptionProvided() {
        String newDescription = "New test description";
        testEntity.setDescription(newDescription);
        
        assertEquals(newDescription, testEntity.getDescription());
    }

    @Test
    void should_SetAndGetCategory_When_ValidCategoryProvided() {
        String newCategory = "HOUSING";
        testEntity.setCategory(newCategory);
        
        assertEquals(newCategory, testEntity.getCategory());
    }

    @Test
    void should_SetAndGetDate_When_ValidDateProvided() {
        LocalDate newDate = LocalDate.of(2024, 6, 15);
        testEntity.setDate(newDate);
        
        assertEquals(newDate, testEntity.getDate());
    }

    @Test
    void should_SetAndGetUserId_When_ValidUserIdProvided() {
        String newUserId = "user456";
        testEntity.setUserId(newUserId);
        
        assertEquals(newUserId, testEntity.getUserId());
    }

    @Test
    void should_SetAndGetCreatedAt_When_ValidCreatedAtProvided() {
        LocalDateTime newCreatedAt = LocalDateTime.of(2024, 2, 1, 12, 0, 0);
        testEntity.setCreatedAt(newCreatedAt);
        
        assertEquals(newCreatedAt, testEntity.getCreatedAt());
    }

    @Test
    void should_SetAndGetUpdatedAt_When_ValidUpdatedAtProvided() {
        LocalDateTime newUpdatedAt = LocalDateTime.of(2024, 2, 2, 15, 30, 0);
        testEntity.setUpdatedAt(newUpdatedAt);
        
        assertEquals(newUpdatedAt, testEntity.getUpdatedAt());
    }

    @Test
    void should_BeEqual_When_EntitiesHaveSameId() {
        ExpenseEntity entity1 = new ExpenseEntity();
        entity1.setId(testId);
        
        ExpenseEntity entity2 = new ExpenseEntity();
        entity2.setId(testId);
        
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_EntitiesHaveDifferentIds() {
        ExpenseEntity entity1 = new ExpenseEntity();
        entity1.setId(testId);
        
        ExpenseEntity entity2 = new ExpenseEntity();
        entity2.setId(UUID.randomUUID().toString());
        
        assertNotEquals(entity1, entity2);
        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void should_BeEqualToItself_When_SameInstance() {
        assertEquals(testEntity, testEntity);
    }

    @Test
    void should_NotBeEqual_When_ComparedToNull() {
        assertNotEquals(testEntity, null);
    }

    @Test
    void should_NotBeEqual_When_ComparedToDifferentClass() {
        assertNotEquals(testEntity, "not an ExpenseEntity");
    }

    @Test
    void should_HandleNullId_When_CheckingEquality() {
        ExpenseEntity entity1 = new ExpenseEntity();
        entity1.setId(null);
        
        ExpenseEntity entity2 = new ExpenseEntity();
        entity2.setId(null);
        
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void should_ProduceCorrectToString_When_Called() {
        String result = testEntity.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("ExpenseEntity"));
        assertTrue(result.contains(testId));
        assertTrue(result.contains(testAmount.toString()));
        assertTrue(result.contains(testDescription));
        assertTrue(result.contains(testCategory));
        assertTrue(result.contains(testDate.toString()));
        assertTrue(result.contains(testUserId));
        assertTrue(result.contains(testCreatedAt.toString()));
        assertTrue(result.contains(testUpdatedAt.toString()));
    }

    @Test
    void should_HandleNullsInToString_When_NullValuesPresent() {
        ExpenseEntity nullEntity = new ExpenseEntity();
        
        String result = nullEntity.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("ExpenseEntity"));
        assertTrue(result.contains("null"));
    }

    @Test
    void should_RoundTripConversion_When_ConvertingBetweenDomainAndEntity() {
        ExpenseEntity entityFromDomain = ExpenseEntity.fromDomain(testExpense);
        Expense domainFromEntity = entityFromDomain.toDomain();
        
        assertEquals(testExpense.getId().getValue().toString(), domainFromEntity.getId().getValue().toString());
        assertEquals(testExpense.getAmount().getValue(), domainFromEntity.getAmount().getValue());
        assertEquals(testExpense.getDescription().getValue(), domainFromEntity.getDescription().getValue());
        assertEquals(testExpense.getCategory().getValue(), domainFromEntity.getCategory().getValue());
        assertEquals(testExpense.getDate(), domainFromEntity.getDate());
        assertEquals(testExpense.getUserId(), domainFromEntity.getUserId());
    }

    @Test
    void should_HandleDecimalPrecision_When_ConvertingAmount() {
        BigDecimal preciseAmount = new BigDecimal("123.4567");
        testEntity.setAmount(preciseAmount);
        
        Expense domainExpense = testEntity.toDomain();
        
        assertEquals(preciseAmount, domainExpense.getAmount().getValue());
    }

    @Test
    void should_HandleSpecialCharacters_When_ConvertingDescription() {
        String specialDescription = "Café & Restaurant - 50% off!";
        testEntity.setDescription(specialDescription);
        
        Expense domainExpense = testEntity.toDomain();
        
        assertEquals(specialDescription, domainExpense.getDescription().getValue());
    }

    @Test
    void should_HandleLongDescription_When_ConvertingToDomain() {
        String longDescription = "This is a very long description that contains many words and characters to test the system's ability to handle longer text inputs in the expense description field";
        testEntity.setDescription(longDescription);
        
        Expense domainExpense = testEntity.toDomain();
        
        assertEquals(longDescription, domainExpense.getDescription().getValue());
    }
}