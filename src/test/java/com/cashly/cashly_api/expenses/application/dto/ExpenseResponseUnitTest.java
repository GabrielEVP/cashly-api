package com.cashly.cashly_api.expenses.application.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseResponseUnitTest {

    private String validId;
    private BigDecimal validAmount;
    private String validDescription;
    private String validCategory;
    private String validUserId;
    private LocalDateTime validCreatedAt;
    private LocalDateTime validUpdatedAt;

    @BeforeEach
    void setUp() {
        validId = "expense-123";
        validAmount = new BigDecimal("1500.00");
        validDescription = "Grocery shopping";
        validCategory = "FOOD_DINING";
        validUserId = "user123";
        validCreatedAt = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
        validUpdatedAt = LocalDateTime.of(2024, 1, 16, 14, 45, 0);
    }

    @Test
    void should_CreateResponse_When_AllValidParametersProvided() {
        ExpenseResponse response = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertNotNull(response);
        assertEquals(validId, response.getId());
        assertEquals(validAmount, response.getAmount());
        assertEquals(validDescription, response.getDescription());
        assertEquals(validCategory, response.getCategory());
        assertEquals(validUserId, response.getUserId());
        assertEquals(validCreatedAt, response.getCreatedAt());
        assertEquals(validUpdatedAt, response.getUpdatedAt());
    }

    @Test
    void should_CreateResponse_When_IdIsNull() {
        ExpenseResponse response = new ExpenseResponse(
            null,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertNotNull(response);
        assertNull(response.getId());
        assertEquals(validAmount, response.getAmount());
        assertEquals(validDescription, response.getDescription());
        assertEquals(validCategory, response.getCategory());
        assertEquals(validUserId, response.getUserId());
        assertEquals(validCreatedAt, response.getCreatedAt());
        assertEquals(validUpdatedAt, response.getUpdatedAt());
    }

    @Test
    void should_CreateResponse_When_AmountIsZero() {
        BigDecimal zeroAmount = BigDecimal.ZERO;
        
        ExpenseResponse response = new ExpenseResponse(
            validId,
            zeroAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertNotNull(response);
        assertEquals(zeroAmount, response.getAmount());
    }

    @Test
    void should_CreateResponse_When_AmountIsNull() {
        ExpenseResponse response = new ExpenseResponse(
            validId,
            null,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertNotNull(response);
        assertNull(response.getAmount());
    }

    @Test
    void should_CreateResponse_When_DescriptionIsNull() {
        ExpenseResponse response = new ExpenseResponse(
            validId,
            validAmount,
            null,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertNotNull(response);
        assertNull(response.getDescription());
    }

    @Test
    void should_CreateResponse_When_CategoryIsNull() {
        ExpenseResponse response = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            null,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertNotNull(response);
        assertNull(response.getCategory());
    }

    @Test
    void should_CreateResponse_When_UserIdIsNull() {
        ExpenseResponse response = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            null,
            validCreatedAt,
            validUpdatedAt
        );

        assertNotNull(response);
        assertNull(response.getUserId());
    }

    @Test
    void should_CreateResponse_When_CreatedAtIsNull() {
        ExpenseResponse response = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            null,
            validUpdatedAt
        );

        assertNotNull(response);
        assertNull(response.getCreatedAt());
    }

    @Test
    void should_CreateResponse_When_UpdatedAtIsNull() {
        ExpenseResponse response = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            null
        );

        assertNotNull(response);
        assertNull(response.getUpdatedAt());
    }

    @Test
    void should_CreateResponse_When_CreatedAtEqualsUpdatedAt() {
        LocalDateTime sameDateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
        
        ExpenseResponse response = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            sameDateTime,
            sameDateTime
        );

        assertNotNull(response);
        assertEquals(sameDateTime, response.getCreatedAt());
        assertEquals(sameDateTime, response.getUpdatedAt());
    }

    @Test
    void should_BeEqual_When_SameValuesProvided() {
        ExpenseResponse response1 = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );
        
        ExpenseResponse response2 = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentIds() {
        ExpenseResponse response1 = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );
        
        ExpenseResponse response2 = new ExpenseResponse(
            "different-id",
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentAmounts() {
        ExpenseResponse response1 = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );
        
        ExpenseResponse response2 = new ExpenseResponse(
            validId,
            new BigDecimal("2000.00"),
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentDescriptions() {
        ExpenseResponse response1 = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );
        
        ExpenseResponse response2 = new ExpenseResponse(
            validId,
            validAmount,
            "Different description",
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentCategories() {
        ExpenseResponse response1 = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );
        
        ExpenseResponse response2 = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            "HOUSING",
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentUserIds() {
        ExpenseResponse response1 = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );
        
        ExpenseResponse response2 = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            "user456",
            validCreatedAt,
            validUpdatedAt
        );

        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentCreatedAt() {
        ExpenseResponse response1 = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );
        
        ExpenseResponse response2 = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            LocalDateTime.of(2024, 2, 1, 10, 30, 0),
            validUpdatedAt
        );

        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentUpdatedAt() {
        ExpenseResponse response1 = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );
        
        ExpenseResponse response2 = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            LocalDateTime.of(2024, 2, 1, 14, 45, 0)
        );

        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void should_BeEqualToItself_When_SameInstance() {
        ExpenseResponse response = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertEquals(response, response);
    }

    @Test
    void should_NotBeEqual_When_ComparedToNull() {
        ExpenseResponse response = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertNotEquals(response, null);
    }

    @Test
    void should_NotBeEqual_When_ComparedToDifferentClass() {
        ExpenseResponse response = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertNotEquals(response, "not an ExpenseResponse");
    }

    @Test
    void should_HandleNullValuesInEquals_When_BothHaveNull() {
        ExpenseResponse response1 = new ExpenseResponse(
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
        
        ExpenseResponse response2 = new ExpenseResponse(
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void should_ProduceCorrectToString_When_Called() {
        ExpenseResponse response = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        String result = response.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("ExpenseResponse"));
        assertTrue(result.contains(validId));
        assertTrue(result.contains(validAmount.toString()));
        assertTrue(result.contains(validDescription));
        assertTrue(result.contains(validCategory));
        assertTrue(result.contains(validUserId));
        assertTrue(result.contains(validCreatedAt.toString()));
        assertTrue(result.contains(validUpdatedAt.toString()));
    }

    @Test
    void should_HandleNullsInToString_When_NullValuesPresent() {
        ExpenseResponse response = new ExpenseResponse(
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        String result = response.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("ExpenseResponse"));
        assertTrue(result.contains("null"));
    }

    @Test
    void should_HandleVeryLargeAmount_When_AmountIsMaxValue() {
        BigDecimal largeAmount = new BigDecimal("999999999999.99");
        
        ExpenseResponse response = new ExpenseResponse(
            validId,
            largeAmount,
            validDescription,
            validCategory,
            validUserId,
            validCreatedAt,
            validUpdatedAt
        );

        assertEquals(largeAmount, response.getAmount());
    }

    @Test
    void should_HandleFutureDateTime_When_CreatedAtIsInFuture() {
        LocalDateTime futureDateTime = LocalDateTime.of(2030, 12, 31, 23, 59, 59);
        
        ExpenseResponse response = new ExpenseResponse(
            validId,
            validAmount,
            validDescription,
            validCategory,
            validUserId,
            futureDateTime,
            futureDateTime
        );

        assertEquals(futureDateTime, response.getCreatedAt());
        assertEquals(futureDateTime, response.getUpdatedAt());
    }
}