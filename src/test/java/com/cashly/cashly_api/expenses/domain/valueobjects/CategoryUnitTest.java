package com.cashly.cashly_api.expenses.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryUnitTest {

    @Test
    void should_CreateCategory_When_ValidValueProvided() {
        // Arrange
        String validCategory = "FOOD_DINING";
        
        // Act
        Category category = new Category(validCategory);
        
        // Assert
        assertNotNull(category);
        assertEquals(validCategory, category.getValue());
    }

    @Test
    void should_ThrowException_When_NullValueProvided() {
        // Arrange
        String nullCategory = null;
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Category(nullCategory)
        );
        
        assertEquals("Category cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_EmptyValueProvided() {
        // Arrange
        String emptyCategory = "";
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Category(emptyCategory)
        );
        
        assertEquals("Category cannot be empty", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_BlankValueProvided() {
        // Arrange
        String blankCategory = "   ";
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Category(blankCategory)
        );
        
        assertEquals("Category cannot be empty", exception.getMessage());
    }

    @Test
    void should_ConvertToUpperCase_When_LowerCaseProvided() {
        // Arrange
        String lowerCaseCategory = "food_dining";
        
        // Act
        Category category = new Category(lowerCaseCategory);
        
        // Assert
        assertEquals("FOOD_DINING", category.getValue());
    }

    @Test
    void should_TrimAndConvertToUpperCase_When_ValueHasSpaces() {
        // Arrange
        String categoryWithSpaces = "  food_dining  ";
        
        // Act
        Category category = new Category(categoryWithSpaces);
        
        // Assert
        assertEquals("FOOD_DINING", category.getValue());
    }

    @Test
    void should_AcceptAllExpenseCategories_When_ValidCategoriesProvided() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> new Category("FOOD_DINING"));
        assertDoesNotThrow(() -> new Category("TRANSPORTATION"));
        assertDoesNotThrow(() -> new Category("HOUSING"));
        assertDoesNotThrow(() -> new Category("HEALTHCARE"));
        assertDoesNotThrow(() -> new Category("ENTERTAINMENT"));
        assertDoesNotThrow(() -> new Category("SHOPPING"));
        assertDoesNotThrow(() -> new Category("EDUCATION"));
        assertDoesNotThrow(() -> new Category("TRAVEL"));
        assertDoesNotThrow(() -> new Category("OTHER"));
    }

    @Test
    void should_ThrowException_When_InvalidCategoryProvided() {
        // Arrange
        String invalidCategory = "INVALID_CATEGORY";
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Category(invalidCategory)
        );
        
        assertEquals("Invalid category: INVALID_CATEGORY. Valid categories are: FOOD_DINING, TRANSPORTATION, HOUSING, HEALTHCARE, ENTERTAINMENT, SHOPPING, EDUCATION, TRAVEL, OTHER", exception.getMessage());
    }

    @Test
    void should_BeEqual_When_SameCategoryValue() {
        // Arrange
        Category category1 = new Category("FOOD_DINING");
        Category category2 = new Category("food_dining"); // Different case but should be equal
        
        // Act & Assert
        assertEquals(category1, category2);
        assertEquals(category1.hashCode(), category2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentCategoryValues() {
        // Arrange
        Category category1 = new Category("FOOD_DINING");
        Category category2 = new Category("TRANSPORTATION");
        
        // Act & Assert
        assertNotEquals(category1, category2);
    }

    @Test
    void should_ReturnTrue_When_IsFoodDiningCategory() {
        // Arrange
        Category foodDiningCategory = new Category("FOOD_DINING");
        Category transportationCategory = new Category("TRANSPORTATION");
        
        // Act & Assert
        assertTrue(foodDiningCategory.isFoodDining());
        assertFalse(transportationCategory.isFoodDining());
    }

    @Test
    void should_ReturnTrue_When_IsTransportationCategory() {
        // Arrange
        Category transportationCategory = new Category("TRANSPORTATION");
        Category housingCategory = new Category("HOUSING");
        
        // Act & Assert
        assertTrue(transportationCategory.isTransportation());
        assertFalse(housingCategory.isTransportation());
    }

    @Test
    void should_ReturnTrue_When_IsHousingCategory() {
        // Arrange
        Category housingCategory = new Category("HOUSING");
        Category healthcareCategory = new Category("HEALTHCARE");
        
        // Act & Assert
        assertTrue(housingCategory.isHousing());
        assertFalse(healthcareCategory.isHousing());
    }

    @Test
    void should_ReturnTrue_When_IsHealthcareCategory() {
        // Arrange
        Category healthcareCategory = new Category("HEALTHCARE");
        Category entertainmentCategory = new Category("ENTERTAINMENT");
        
        // Act & Assert
        assertTrue(healthcareCategory.isHealthcare());
        assertFalse(entertainmentCategory.isHealthcare());
    }

    @Test
    void should_ReturnAllValidCategories_When_Asked() {
        // Act
        String[] validCategories = Category.getValidCategories();
        
        // Assert
        assertNotNull(validCategories);
        assertEquals(9, validCategories.length);
        assertArrayEquals(new String[]{"FOOD_DINING", "TRANSPORTATION", "HOUSING", "HEALTHCARE", "ENTERTAINMENT", "SHOPPING", "EDUCATION", "TRAVEL", "OTHER"}, validCategories);
    }
}