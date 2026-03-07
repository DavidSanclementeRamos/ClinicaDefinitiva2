package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.BillingVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

import java.util.Objects;

/**
 * Value Object: Quantity (Cantidad de ítems en una factura)
 *
 * Reglas de negocio:
 * - RN-BILLING-001: La cantidad debe ser positiva (> 0).
 * - RN-BILLING-002: La cantidad no puede exceder 1000 por ítem de factura.
 *
 * Características:
 * - Inmutable: una vez creada, no puede modificarse.
 * - Igualdad basada en valor: dos instancias con el mismo valor son equivalentes.
 *
 * Decisiones de diseño:
 * - Se encapsulan las validaciones en este VO para evitar lógica repetida en entidades.
 * - Se proveen métodos auxiliares para operaciones comunes (sumar, multiplicar).
 */
public final class Quantity {

    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 1000;

    private final int value;

    private Quantity(int value) {
        validate(value);
        this.value = value;
    }


    public static Quantity of(int value) {
        return new Quantity(value);
    }


    public static Quantity one() {
        return new Quantity(1);
    }


    private void validate(int value) {
        if (value < MIN_QUANTITY) {
            throw new ValueObjectValidationException(
                    BillingVOError.ERR_QUANTITY_MUST_BE_POSITIVE,
                    VOContext.BILLING);
        }

        if (value > MAX_QUANTITY) {
            throw new ValueObjectValidationException(
                    BillingVOError.ERR_QUANTITY_EXCEEDS_MAXIMUM,
                    VOContext.BILLING);
        }
    }

    public boolean isMultiple() {
        return value > 1;
    }

    public boolean isSingle() {
        return value == 1;
    }



    public Quantity add(Quantity other) {
        return new Quantity(this.value + other.value);
    }
    public Quantity multiply(int factor) {
        if (factor <= 0) {
            throw new IllegalArgumentException("El factor debe ser positivo");
        } return new Quantity(this.value * factor);
    }

    public int getValue() {
        return value;
    }

    public Integer asInteger() {
        return value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantity)) return false;
        Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }


}

