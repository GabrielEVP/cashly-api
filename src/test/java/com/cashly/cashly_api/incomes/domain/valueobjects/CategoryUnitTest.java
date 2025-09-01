package com.cashly.cashly_api.incomes.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryUnitTest {

    @Test
    void should_CreateCategory_When_ValidValueProvided() {
        // Arrange
        String validCategory = "SALARY";
        
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
        String lowerCaseCategory = "salary";
        
        // Act
        Category category = new Category(lowerCaseCategory);
        
        // Assert
        assertEquals("SALARY", category.getValue());
    }

    @Test
    void should_TrimAndConvertToUpperCase_When_ValueHasSpaces() {
        // Arrange
        String categoryWithSpaces = "  salary  ";
        
        // Act
        Category category = new Category(categoryWithSpaces);
        
        // Assert
        assertEquals("SALARY", category.getValue());
    }

    @Test
    void should_AcceptPredefinedCategory_When_ValidCategoryProvided() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> new Category("SALARY"));
        assertDoesNotThrow(() -> new Category("BUSINESS"));
        assertDoesNotThrow(() -> new Category("INVESTMENT"));
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
        
        assertEquals("Invalid category: INVALID_CATEGORY. Valid categories are: SALARY, BUSINESS, INVESTMENT, OTHER", exception.getMessage());
    }

    @Test
    void should_BeEqual_When_SameCategoryValue() {
        // Arrange
        Category category1 = new Category("SALARY");
        Category category2 = new Category("salary"); // Different case but should be equal
        
        // Act & Assert
        assertEquals(category1, category2);
        assertEquals(category1.hashCode(), category2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentCategoryValues() {
        // Arrange
        Category category1 = new Category("SALARY");
        Category category2 = new Category("BUSINESS");
        
        // Act & Assert
        assertNotEquals(category1, category2);
    }

    @Test
    void should_ReturnTrue_When_IsSalaryCategory() {
        // Arrange
        Category salaryCategory = new Category("SALARY");
        Category businessCategory = new Category("BUSINESS");
        
        // Act & Assert
        assertTrue(salaryCategory.isSalary());
        assertFalse(businessCategory.isSalary());
    }

    @Test
    void should_ReturnTrue_When_IsBusinessCategory() {
        // Arrange
        Category businessCategory = new Category("BUSINESS");
        Category salaryCategory = new Category("SALARY");
        
        // Act & Assert
        assertTrue(businessCategory.isBusiness());
        assertFalse(salaryCategory.isBusiness());
    }

    @Test
    void should_ReturnAllValidCategories_When_Asked() {
        // Act
        String[] validCategories = Category.getValidCategories();
        
        // Assert
        assertNotNull(validCategories);
        assertEquals(4, validCategories.length);
        assertArrayEquals(new String[]{"SALARY", "BUSINESS", "INVESTMENT", "OTHER"}, validCategories);
    }
}