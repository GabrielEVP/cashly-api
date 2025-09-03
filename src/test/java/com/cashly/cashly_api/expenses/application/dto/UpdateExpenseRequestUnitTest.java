package com.cashly.cashly_api.expenses.application.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UpdateExpenseRequestUnitTest {

    private BigDecimal validAmount;
    private String validDescription;
    private String validCategory;

    @BeforeEach
    void setUp() {
        validAmount = new BigDecimal("1200.00");
        validDescription = "Updated grocery shopping";
        validCategory = "FOOD_DINING";
    }

    @Test
    void should_CreateRequest_When_AllValidParametersProvided() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            validAmount,
            validDescription,
            validCategory
        );

        assertNotNull(request);
        assertEquals(validAmount, request.getAmount());
        assertEquals(validDescription, request.getDescription());
        assertEquals(validCategory, request.getCategory());
    }

    @Test
    void should_CreateRequest_When_AmountIsZero() {
        BigDecimal zeroAmount = BigDecimal.ZERO;
        
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            zeroAmount,
            validDescription,
            validCategory
        );

        assertNotNull(request);
        assertEquals(zeroAmount, request.getAmount());
        assertEquals(validDescription, request.getDescription());
        assertEquals(validCategory, request.getCategory());
    }

    @Test
    void should_CreateRequest_When_AmountIsVeryLarge() {
        BigDecimal largeAmount = new BigDecimal("999999999.99");
        
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            largeAmount,
            validDescription,
            validCategory
        );

        assertNotNull(request);
        assertEquals(largeAmount, request.getAmount());
    }

    @Test
    void should_CreateRequest_When_AmountIsVerySmall() {
        BigDecimal smallAmount = new BigDecimal("0.01");
        
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            smallAmount,
            validDescription,
            validCategory
        );

        assertNotNull(request);
        assertEquals(smallAmount, request.getAmount());
    }

    @Test
    void should_CreateRequest_When_AmountIsNull() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            null,
            validDescription,
            validCategory
        );

        assertNotNull(request);
        assertNull(request.getAmount());
        assertEquals(validDescription, request.getDescription());
        assertEquals(validCategory, request.getCategory());
    }

    @Test
    void should_CreateRequest_When_DescriptionIsNull() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            validAmount,
            null,
            validCategory
        );

        assertNotNull(request);
        assertEquals(validAmount, request.getAmount());
        assertNull(request.getDescription());
        assertEquals(validCategory, request.getCategory());
    }

    @Test
    void should_CreateRequest_When_DescriptionIsEmpty() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            validAmount,
            "",
            validCategory
        );

        assertNotNull(request);
        assertEquals(validAmount, request.getAmount());
        assertEquals("", request.getDescription());
        assertEquals(validCategory, request.getCategory());
    }

    @Test
    void should_CreateRequest_When_CategoryIsNull() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            validAmount,
            validDescription,
            null
        );

        assertNotNull(request);
        assertEquals(validAmount, request.getAmount());
        assertEquals(validDescription, request.getDescription());
        assertNull(request.getCategory());
    }

    @Test
    void should_CreateRequest_When_CategoryIsEmpty() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            validAmount,
            validDescription,
            ""
        );

        assertNotNull(request);
        assertEquals(validAmount, request.getAmount());
        assertEquals(validDescription, request.getDescription());
        assertEquals("", request.getCategory());
    }

    @Test
    void should_BeEqual_When_SameValuesProvided() {
        UpdateExpenseRequest request1 = new UpdateExpenseRequest(
            validAmount,
            validDescription,
            validCategory
        );
        
        UpdateExpenseRequest request2 = new UpdateExpenseRequest(
            validAmount,
            validDescription,
            validCategory
        );

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentAmounts() {
        UpdateExpenseRequest request1 = new UpdateExpenseRequest(
            validAmount,
            validDescription,
            validCategory
        );
        
        UpdateExpenseRequest request2 = new UpdateExpenseRequest(
            new BigDecimal("2000.00"),
            validDescription,
            validCategory
        );

        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentDescriptions() {
        UpdateExpenseRequest request1 = new UpdateExpenseRequest(
            validAmount,
            validDescription,
            validCategory
        );
        
        UpdateExpenseRequest request2 = new UpdateExpenseRequest(
            validAmount,
            "Different description",
            validCategory
        );

        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentCategories() {
        UpdateExpenseRequest request1 = new UpdateExpenseRequest(
            validAmount,
            validDescription,
            validCategory
        );
        
        UpdateExpenseRequest request2 = new UpdateExpenseRequest(
            validAmount,
            validDescription,
            "HOUSING"
        );

        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void should_BeEqualToItself_When_SameInstance() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            validAmount,
            validDescription,
            validCategory
        );

        assertEquals(request, request);
    }

    @Test
    void should_NotBeEqual_When_ComparedToNull() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            validAmount,
            validDescription,
            validCategory
        );

        assertNotEquals(request, null);
    }

    @Test
    void should_NotBeEqual_When_ComparedToDifferentClass() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            validAmount,
            validDescription,
            validCategory
        );

        assertNotEquals(request, "not an UpdateExpenseRequest");
    }

    @Test
    void should_HandleNullValuesInEquals_When_BothHaveNull() {
        UpdateExpenseRequest request1 = new UpdateExpenseRequest(
            null,
            null,
            null
        );
        
        UpdateExpenseRequest request2 = new UpdateExpenseRequest(
            null,
            null,
            null
        );

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void should_ProduceCorrectToString_When_Called() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            validAmount,
            validDescription,
            validCategory
        );

        String result = request.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("UpdateExpenseRequest"));
        assertTrue(result.contains(validAmount.toString()));
        assertTrue(result.contains(validDescription));
        assertTrue(result.contains(validCategory));
    }

    @Test
    void should_HandleNullsInToString_When_NullValuesPresent() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            null,
            null,
            null
        );

        String result = request.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("UpdateExpenseRequest"));
        assertTrue(result.contains("null"));
    }

    @Test
    void should_HandleDecimalPrecision_When_AmountHasMultipleDecimals() {
        BigDecimal preciseAmount = new BigDecimal("1234.5678");
        
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            preciseAmount,
            validDescription,
            validCategory
        );

        assertEquals(preciseAmount, request.getAmount());
    }

    @Test
    void should_HandleLongDescription_When_DescriptionIsVeryLong() {
        String longDescription = "This is a very long description that contains many words and characters to test how the system handles longer text inputs in the update request";
        
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            validAmount,
            longDescription,
            validCategory
        );

        assertEquals(longDescription, request.getDescription());
    }

    @Test
    void should_HandleSpecialCharacters_When_DescriptionContainsSpecialChars() {
        String specialDescription = "Café & restaurant bill - 50% discount!";
        
        UpdateExpenseRequest request = new UpdateExpenseRequest(
            validAmount,
            specialDescription,
            validCategory
        );

        assertEquals(specialDescription, request.getDescription());
    }
}