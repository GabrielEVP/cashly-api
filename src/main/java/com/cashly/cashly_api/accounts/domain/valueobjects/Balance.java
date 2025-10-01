package com.cashly.cashly_api.accounts.domain.valueobjects;

import java.math.BigDecimal;
import java.util.Objects;

public class Balance {
    private final BigDecimal value;

    public Balance(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Balance cannot be null");
        }
        this.value = value;
    }

    public BigDecimal getValue() {
        return new BigDecimal(value.toString());
    }

    public Balance add(Balance other) {
        if (other == null) {
            throw new IllegalArgumentException("Other balance cannot be null");
        }
        return new Balance(this.value.add(other.value));
    }

    public Balance subtract(Balance other) {
        if (other == null) {
            throw new IllegalArgumentException("Other balance cannot be null");
        }
        return new Balance(this.value.subtract(other.value));
    }

    public boolean isNegative() {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isPositive() {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isZero() {
        return value.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isGreaterThan(Balance other) {
        if (other == null) {
            throw new IllegalArgumentException("Other balance cannot be null");
        }
        return this.value.compareTo(other.value) > 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Balance balance = (Balance) obj;
        return Objects.equals(value, balance.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Balance{" + "value=" + value + '}';
    }
}
