package com.cashly.cashly_api.accounts.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyUnitTest {

    @Test
    void should_CreateCurrency_When_ValidCurrencyCodeProvided() {
        // Act
        Currency currency = new Currency("USD");

        // Assert
        assertNotNull(currency);
        assertEquals("USD", currency.getValue());
    }

    @Test
    void should_NormalizeToUpperCase_When_LowercaseCurrencyProvided() {
        // Act
        Currency currency = new Currency("usd");

        // Assert
        assertEquals("USD", currency.getValue());
    }

    @Test
    void should_TrimWhitespace_When_CurrencyHasSpaces() {
        // Act
        Currency currency = new Currency("  EUR  ");

        // Assert
        assertEquals("EUR", currency.getValue());
    }

    @Test
    void should_ThrowException_When_NullCurrencyProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Currency(null)
        );
        assertEquals("Currency cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_InvalidCurrencyProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Currency("XXX")
        );
        assertTrue(exception.getMessage().contains("Invalid currency code"));
    }

    @Test
    void should_CreateCurrency_When_USDProvided() {
        // Act
        Currency currency = new Currency("USD");

        // Assert
        assertEquals("USD", currency.getValue());
    }

    @Test
    void should_CreateCurrency_When_EURProvided() {
        // Act
        Currency currency = new Currency("EUR");

        // Assert
        assertEquals("EUR", currency.getValue());
    }

    @Test
    void should_CreateCurrency_When_MXNProvided() {
        // Act
        Currency currency = new Currency("MXN");

        // Assert
        assertEquals("MXN", currency.getValue());
    }

    @Test
    void should_CreateCurrency_When_GBPProvided() {
        // Act
        Currency currency = new Currency("GBP");

        // Assert
        assertEquals("GBP", currency.getValue());
    }

    @Test
    void should_CreateCurrency_When_JPYProvided() {
        // Act
        Currency currency = new Currency("JPY");

        // Assert
        assertEquals("JPY", currency.getValue());
    }

    @Test
    void should_BeEqual_When_SameCurrencyValue() {
        // Arrange
        Currency currency1 = new Currency("USD");
        Currency currency2 = new Currency("usd");

        // Act & Assert
        assertEquals(currency1, currency2);
        assertEquals(currency1.hashCode(), currency2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentCurrencyValue() {
        // Arrange
        Currency currency1 = new Currency("USD");
        Currency currency2 = new Currency("EUR");

        // Act & Assert
        assertNotEquals(currency1, currency2);
    }

    @Test
    void should_ReturnStringRepresentation_When_ToStringCalled() {
        // Arrange
        Currency currency = new Currency("USD");

        // Act
        String result = currency.toString();

        // Assert
        assertTrue(result.contains("Currency"));
        assertTrue(result.contains("USD"));
    }
}
