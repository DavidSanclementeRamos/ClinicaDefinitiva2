package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class QuantityTest {

    @Test
    @DisplayName("VO-BIL-008: Cantidad positiva válida")
    void of_withValidPositive_shouldCreate() {
        Quantity quantity = Quantity.of(5);
        assertThat(quantity.getValue()).isEqualTo(5);
        assertThat(quantity.asInteger()).isEqualTo(5);
        assertThat(quantity.toString()).isEqualTo("5");
    }

    @Test
    @DisplayName("VO-BIL-009: Cantidad cero lanza excepción")
    void of_withZero_shouldThrow() {
        assertThatThrownBy(() -> Quantity.of(0))
                .isInstanceOf(ValueObjectValidationException.class)
                .hasMessageContaining("La cantidad debe ser mayor o igual a 1");
    }

    @Test
    @DisplayName("VO-BIL-009: Cantidad negativa lanza excepción")
    void of_withNegative_shouldThrow() {
        assertThatThrownBy(() -> Quantity.of(-5))
                .isInstanceOf(ValueObjectValidationException.class)
                .hasMessageContaining("La cantidad debe ser mayor o igual a 1");
    }

    @Test
    @DisplayName("VO-BIL-010: Cantidad excede máximo (1000) lanza excepción")
    void of_withExceedsMax_shouldThrow() {
        assertThatThrownBy(() -> Quantity.of(1500))
                .isInstanceOf(ValueObjectValidationException.class)
                .hasMessageContaining("La cantidad no puede exceder 1000 ítems por línea de factura");
    }

    @Test
    @DisplayName("Cantidad 1 es single")
    void one_isSingle() {
        Quantity quantity = Quantity.one();
        assertThat(quantity.getValue()).isEqualTo(1);
        assertThat(quantity.isSingle()).isTrue();
        assertThat(quantity.isMultiple()).isFalse();
    }

    @Test
    @DisplayName("Cantidad mayor a 1 es multiple")
    void multiple_isMultiple() {
        Quantity quantity = Quantity.of(3);
        assertThat(quantity.isMultiple()).isTrue();
        assertThat(quantity.isSingle()).isFalse();
    }

    @Test
    @DisplayName("Suma de dos cantidades")
    void add_shouldSum() {
        Quantity q1 = Quantity.of(5);
        Quantity q2 = Quantity.of(3);
        Quantity result = q1.add(q2);
        assertThat(result.getValue()).isEqualTo(8);
    }

    @Test
    @DisplayName("Multiplicación por factor positivo")
    void multiply_withPositiveFactor_shouldMultiply() {
        Quantity q = Quantity.of(4);
        Quantity result = q.multiply(3);
        assertThat(result.getValue()).isEqualTo(12);
    }

    @Test
    @DisplayName("Multiplicación por factor cero lanza excepción")
    void multiply_withZeroFactor_shouldThrow() {
        Quantity q = Quantity.of(4);
        assertThatThrownBy(() -> q.multiply(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El factor debe ser positivo");
    }

    @Test
    @DisplayName("Multiplicación por factor negativo lanza excepción")
    void multiply_withNegativeFactor_shouldThrow() {
        Quantity q = Quantity.of(4);
        assertThatThrownBy(() -> q.multiply(-2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El factor debe ser positivo");
    }

    @Test
    @DisplayName("Igualdad basada en valor")
    void equals_shouldCompareByValue() {
        Quantity q1 = Quantity.of(5);
        Quantity q2 = Quantity.of(5);
        Quantity q3 = Quantity.of(7);
        assertThat(q1).isEqualTo(q2);
        assertThat(q1).isNotEqualTo(q3);
        assertThat(q1).isNotEqualTo(null);
        assertThat(q1).isNotEqualTo("5");
    }

    @Test
    @DisplayName("HashCode consistente con equals")
    void hashCode_shouldBeConsistent() {
        Quantity q1 = Quantity.of(5);
        Quantity q2 = Quantity.of(5);
        assertThat(q1.hashCode()).isEqualTo(q2.hashCode());
    }
}
