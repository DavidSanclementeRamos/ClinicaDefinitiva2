package com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Price {
    private final BigDecimal amount;
    private final String currency;

    public Price(BigDecimal amount, String currency) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = Objects.requireNonNull(currency);
    }

    public static Price of(double value, String currency) {
        return new Price(BigDecimal.valueOf(value), currency);
    }

    public static Price of(BigDecimal value, String currency) {
        return new Price(value, currency);
    }

    public static Price zero(String currency) {
        return new Price(BigDecimal.ZERO, currency);
    }

    public Price add(Price other) {
        requireSameCurrency(other);
        return new Price(this.amount.add(other.amount), currency);
    }
    public Price subtract(Price other) {
        requireSameCurrency(other);
        return new Price(this.amount.subtract(other.amount), currency);
    }

    public Price multiply(double factor) {
        return new Price(this.amount.multiply(BigDecimal.valueOf(factor)), currency);
    }

    public Price multiply(BigDecimal factor) {
        return new Price(this.amount.multiply(factor), currency);
    }

    public boolean isNegativeOrZero() {
        return this.amount.compareTo(BigDecimal.ZERO) <= 0;
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public int compareTo(Price other) {
        requireSameCurrency(other);
        return this.amount.compareTo(other.amount);
    }

    private void requireSameCurrency(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new DomainException("Currency mismatch: " + this.currency + " vs " + other.currency);
        }
    }
    public BigDecimal asBigDecimal() { return amount; }
    public String getCurrency() { return currency; }



}

}