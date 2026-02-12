package com.example.ClinicaDefinitiva.domain.dental.care.services.vo;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Currency;

public final class Price {


    private final BigDecimal amount;
    private final Currency currency;

    private Price(BigDecimal amount, Currency currency) {


            if (amount == null) {
                throw new ValueObjectValidationException(
                        ServiceVOError.ERR_SERVICE_PRICE_AMOUNT_REQUIRED,
                        VOContext.SERVICE_PRICE);
            }
            if (currency == null) {
                throw new ValueObjectValidationException(
                        ServiceVOError.ERR_SERVICE_PRICE_CURRENCY_REQUIRED,
                        VOContext.SERVICE_PRICE);
            }

            this.amount = amount.setScale(2, RoundingMode.HALF_UP);
            this.currency = currency;

            if (this.amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new ValueObjectValidationException(
                        ServiceVOError.ERR_SERVICE_PRICE_NEGATIVE,
                        VOContext.SERVICE_PRICE);
            }
    }

    // Factorías expresivas
    public static Price of(BigDecimal value, Currency currency) {
        return new Price(value, currency);
    }

    public static Price of(double value, Currency currency) {
        // Se mantiene por conveniencia, pero documentar que puede introducir imprecisiones
        return new Price(BigDecimal.valueOf(value), currency);
    }

    public static Price zero(Currency currency) {
        return new Price(BigDecimal.ZERO, currency);
    }

    // Operaciones aritméticas
    public Price add(Price other) {
        requireSameCurrency(other);
        return new Price(this.amount.add(other.amount), currency);
    }

    public Price subtract(Price other) {
        requireSameCurrency(other);
        return new Price(this.amount.subtract(other.amount), currency);
    }

    public Price multiply(BigDecimal factor) {
        return new Price(this.amount.multiply(factor), currency);
    }

    public Price multiply(double factor) {
        return new Price(this.amount.multiply(BigDecimal.valueOf(factor)), currency);
    }

    // Comparaciones semánticas
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

    // Validación de moneda
    private void requireSameCurrency(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new ValueObjectValidationException(ServiceVOError.ERR_PRICE_CURRENCY_MISMATCH,VOContext.SERVICE_PRICE);
        }
    }


    // Getters
    public BigDecimal asBigDecimal() { return amount; }
    public Currency getCurrency() { return currency; }

    // Igualdad semántica
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;
        Price other = (Price) o;
        return amount.equals(other.amount) && currency.equals(other.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency.getCurrencyCode();
    }
}