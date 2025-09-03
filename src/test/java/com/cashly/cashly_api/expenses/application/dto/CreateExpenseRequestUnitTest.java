package com.cashly.cashly_api.expenses.application.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CreateExpenseRequestUnitTest {

    private BigDecimal validAmount;
    private String validDescription;
    private String validCategory;
    private String validUserId;

    @BeforeEach
    void setUp() {
        validAmount = new BigDecimal("1500.00");
        validDescription = "Grocery shopping";
        validCategory = "FOOD_DINING";
        validUserId = "user123";
    }

    @Test
    void should_CreateRequest_When_AllValidParametersProvided() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            validAmount,
            validDescription,
            validCategory,
            validUserId
        );

        assertNotNull(request);
        assertEquals(validAmount, request.getAmount());
        assertEquals(validDescription, request.getDescription());
        assertEquals(validCategory, request.getCategory());
        assertEquals(validUserId, request.getUserId());
    }

    @Test
    void should_CreateRequest_When_AmountIsZero() {
        BigDecimal zeroAmount = BigDecimal.ZERO;
        
        CreateExpenseRequest request = new CreateExpenseRequest(
            zeroAmount,
            validDescription,
            validCategory,
            validUserId
        );

        assertNotNull(request);
        assertEquals(zeroAmount, request.getAmount());
        assertEquals(validDescription, request.getDescription());
        assertEquals(validCategory, request.getCategory());
        assertEquals(validUserId, request.getUserId());
    }

    @Test
    void should_CreateRequest_When_AmountIsVeryLarge() {
        BigDecimal largeAmount = new BigDecimal("999999999.99");
        
        CreateExpenseRequest request = new CreateExpenseRequest(
            largeAmount,
            validDescription,
            validCategory,
            validUserId
        );

        assertNotNull(request);
        assertEquals(largeAmount, request.getAmount());
    }

    @Test
    void should_CreateRequest_When_AmountIsVerySmall() {
        BigDecimal smallAmount = new BigDecimal("0.01");
        
        CreateExpenseRequest request = new CreateExpenseRequest(
            smallAmount,
            validDescription,
            validCategory,
            validUserId
        );

        assertNotNull(request);
        assertEquals(smallAmount, request.getAmount());
    }

    @Test
    void should_CreateRequest_When_AmountIsNull() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            null,
            validDescription,
            validCategory,
            validUserId
        );

        assertNotNull(request);
        assertNull(request.getAmount());
        assertEquals(validDescription, request.getDescription());
        assertEquals(validCategory, request.getCategory());
        assertEquals(validUserId, request.getUserId());
    }

    @Test
    void should_CreateRequest_When_DescriptionIsNull() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            validAmount,
            null,
            validCategory,
            validUserId
        );

        assertNotNull(request);
        assertEquals(validAmount, request.getAmount());
        assertNull(request.getDescription());
        assertEquals(validCategory, request.getCategory());
        assertEquals(validUserId, request.getUserId());
    }

    @Test
    void should_CreateRequest_When_DescriptionIsEmpty() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            validAmount,
            "",
            validCategory,
            validUserId
        );

        assertNotNull(request);
        assertEquals(validAmount, request.getAmount());
        assertEquals("", request.getDescription());
        assertEquals(validCategory, request.getCategory());
        assertEquals(validUserId, request.getUserId());
    }

    @Test
    void should_CreateRequest_When_CategoryIsNull() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            validAmount,
            validDescription,
            null,
            validUserId
        );

        assertNotNull(request);
        assertEquals(validAmount, request.getAmount());
        assertEquals(validDescription, request.getDescription());
        assertNull(request.getCategory());
        assertEquals(validUserId, request.getUserId());
    }

    @Test
    void should_CreateRequest_When_CategoryIsEmpty() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            validAmount,
            validDescription,
            "",
            validUserId
        );

        assertNotNull(request);
        assertEquals(validAmount, request.getAmount());
        assertEquals(validDescription, request.getDescription());
        assertEquals("", request.getCategory());
        assertEquals(validUserId, request.getUserId());
    }

    @Test
    void should_CreateRequest_When_UserIdIsNull() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            validAmount,
            validDescription,
            validCategory,
            null
        );

        assertNotNull(request);
        assertEquals(validAmount, request.getAmount());
        assertEquals(validDescription, request.getDescription());
        assertEquals(validCategory, request.getCategory());
        assertNull(request.getUserId());
    }

    @Test
    void should_CreateRequest_When_UserIdIsEmpty() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            validAmount,
            validDescription,
            validCategory,
            ""
        );

        assertNotNull(request);
        assertEquals(validAmount, request.getAmount());
        assertEquals(validDescription, request.getDescription());
        assertEquals(validCategory, request.getCategory());
        assertEquals("", request.getUserId());
    }

    @Test
    void should_BeEqual_When_SameValuesProvided() {
        CreateExpenseRequest request1 = new CreateExpenseRequest(
            validAmount,
            validDescription,
            validCategory,
            validUserId
        );
        
        CreateExpenseRequest request2 = new CreateExpenseRequest(
            validAmount,
            validDescription,
            validCategory,
            validUserId
        );

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentAmounts() {
        CreateExpenseRequest request1 = new CreateExpenseRequest(
            validAmount,
            validDescription,
            validCategory,
            validUserId
        );
        
        CreateExpenseRequest request2 = new CreateExpenseRequest(
            new BigDecimal("2000.00"),
            validDescription,
            validCategory,
            validUserId
        );

        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentDescriptions() {
        CreateExpenseRequest request1 = new CreateExpenseRequest(
            validAmount,
            validDescription,
            validCategory,
            validUserId
        );
        
        CreateExpenseRequest request2 = new CreateExpenseRequest(
            validAmount,
            "Different description",
            validCategory,
            validUserId
        );

        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentCategories() {
        CreateExpenseRequest request1 = new CreateExpenseRequest(
            validAmount,
            validDescription,
            validCategory,
            validUserId
        );
        
        CreateExpenseRequest request2 = new CreateExpenseRequest(
            validAmount,
            validDescription,
            "HOUSING",
            validUserId
        );

        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentUserIds() {
        CreateExpenseRequest request1 = new CreateExpenseRequest(
            validAmount,
            validDescription,
            validCategory,
            validUserId
        );
        
        CreateExpenseRequest request2 = new CreateExpenseRequest(
            validAmount,
            validDescription,
            validCategory,
            "user456"
        );

        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void should_BeEqualToItself_When_SameInstance() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            validAmount,
            validDescription,
            validCategory,
            validUserId
        );

        assertEquals(request, request);
    }

    @Test
    void should_NotBeEqual_When_ComparedToNull() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            validAmount,
            validDescription,
            validCategory,
            validUserId
        );

        assertNotEquals(request, null);
    }

    @Test
    void should_NotBeEqual_When_ComparedToDifferentClass() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            validAmount,
            validDescription,
            validCategory,
            validUserId
        );

        assertNotEquals(request, "not a CreateExpenseRequest");
    }

    @Test
    void should_HandleNullValuesInEquals_When_BothHaveNull() {
        CreateExpenseRequest request1 = new CreateExpenseRequest(
            null,
            null,
            null,
            null
        );
        
        CreateExpenseRequest request2 = new CreateExpenseRequest(
            null,
            null,
            null,
            null
        );

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void should_ProduceCorrectToString_When_Called() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            validAmount,
            validDescription,
            validCategory,
            validUserId
        );

        String result = request.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("CreateExpenseRequest"));
        assertTrue(result.contains(validAmount.toString()));
        assertTrue(result.contains(validDescription));
        assertTrue(result.contains(validCategory));
        assertTrue(result.contains(validUserId));
    }

    @Test
    void should_HandleNullsInToString_When_NullValuesPresent() {
        CreateExpenseRequest request = new CreateExpenseRequest(
            null,
            null,
            null,
            null
        );

        String result = request.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("CreateExpenseRequest"));
        assertTrue(result.contains("null"));
    }
}