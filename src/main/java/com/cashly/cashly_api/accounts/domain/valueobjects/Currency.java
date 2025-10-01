package com.cashly.cashly_api.accounts.domain.valueobjects;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Currency {
    private static final List<String> VALID_CURRENCIES = Arrays.asList(
        "USD", "EUR", "MXN", "GBP", "JPY", "CAD", "AUD", "CHF", "CNY", "BRL"
    );

    private final String value;

    public Currency(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        String normalizedValue = value.trim().toUpperCase();
        if (!VALID_CURRENCIES.contains(normalizedValue)) {
            throw new IllegalArgumentException("Invalid currency code. Must be one of: " +
                String.join(", ", VALID_CURRENCIES));
        }
        this.value = normalizedValue;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Currency currency = (Currency) obj;
        return Objects.equals(value, currency.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Currency{" + "value='" + value + '\'' + '}';
    }
}
