package com.cashly.cashly_api.accounts.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTypeUnitTest {

    @Test
    void should_CreateAccountType_When_ValidTypeStringProvided() {
        // Act
        AccountType accountType = new AccountType("CHECKING");

        // Assert
        assertNotNull(accountType);
        assertEquals("CHECKING", accountType.getValue());
        assertEquals(AccountType.Type.CHECKING, accountType.getType());
    }

    @Test
    void should_CreateAccountType_When_ValidTypeEnumProvided() {
        // Act
        AccountType accountType = new AccountType(AccountType.Type.SAVINGS);

        // Assert
        assertNotNull(accountType);
        assertEquals("SAVINGS", accountType.getValue());
        assertEquals(AccountType.Type.SAVINGS, accountType.getType());
    }

    @Test
    void should_NormalizeToUpperCase_When_LowercaseTypeProvided() {
        // Act
        AccountType accountType = new AccountType("checking");

        // Assert
        assertEquals("CHECKING", accountType.getValue());
    }

    @Test
    void should_ThrowException_When_NullStringProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new AccountType((String) null)
        );
        assertEquals("Account type cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullEnumProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new AccountType((AccountType.Type) null)
        );
        assertEquals("Account type cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_InvalidTypeProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new AccountType("INVALID")
        );
        assertTrue(exception.getMessage().contains("Invalid account type"));
    }

    @Test
    void should_ReturnTrue_When_CreditCardTypeChecked() {
        // Arrange
        AccountType accountType = new AccountType("CREDIT_CARD");

        // Act & Assert
        assertTrue(accountType.isCreditType());
        assertFalse(accountType.isDebitType());
    }

    @Test
    void should_ReturnTrue_When_CheckingTypeChecked() {
        // Arrange
        AccountType accountType = new AccountType("CHECKING");

        // Act & Assert
        assertTrue(accountType.isDebitType());
        assertFalse(accountType.isCreditType());
    }

    @Test
    void should_ReturnTrue_When_SavingsTypeChecked() {
        // Arrange
        AccountType accountType = new AccountType("SAVINGS");

        // Act & Assert
        assertTrue(accountType.isDebitType());
        assertFalse(accountType.isCreditType());
    }

    @Test
    void should_ReturnTrue_When_CashTypeChecked() {
        // Arrange
        AccountType accountType = new AccountType("CASH");

        // Act & Assert
        assertTrue(accountType.isDebitType());
        assertFalse(accountType.isCreditType());
    }

    @Test
    void should_ReturnTrue_When_InvestmentTypeChecked() {
        // Arrange
        AccountType accountType = new AccountType("INVESTMENT");

        // Act & Assert
        assertTrue(accountType.isDebitType());
        assertFalse(accountType.isCreditType());
    }

    @Test
    void should_BeEqual_When_SameTypeValue() {
        // Arrange
        AccountType type1 = new AccountType("CHECKING");
        AccountType type2 = new AccountType(AccountType.Type.CHECKING);

        // Act & Assert
        assertEquals(type1, type2);
        assertEquals(type1.hashCode(), type2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentTypeValue() {
        // Arrange
        AccountType type1 = new AccountType("CHECKING");
        AccountType type2 = new AccountType("SAVINGS");

        // Act & Assert
        assertNotEquals(type1, type2);
    }

    @Test
    void should_ReturnStringRepresentation_When_ToStringCalled() {
        // Arrange
        AccountType accountType = new AccountType("CHECKING");

        // Act
        String result = accountType.toString();

        // Assert
        assertTrue(result.contains("AccountType"));
        assertTrue(result.contains("CHECKING"));
    }
}
