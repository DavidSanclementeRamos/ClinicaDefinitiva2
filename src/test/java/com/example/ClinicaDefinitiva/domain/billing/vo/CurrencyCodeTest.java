package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class CurrencyCodeTest {

    @ParameterizedTest
    @ValueSource(strings = {"COP", "USD", "EUR", "GBP", "JPY"})
    @DisplayName("Crear CurrencyCode con código ISO 4217 válido")
    void shouldCreateValidCurrencyCode(String code) {
        CurrencyCode currency = CurrencyCode.of(code);
        assertThat(currency.getCode()).isEqualTo(code.toUpperCase());
        assertThat(currency.toJavaCurrency()).isNotNull();
    }

    @Test
    @DisplayName("Crear CurrencyCode con código nulo lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> CurrencyCode.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Crear CurrencyCode con código vacío lanza excepción")
    void shouldThrowForEmpty() {
        assertThatThrownBy(() -> CurrencyCode.of(""))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Crear CurrencyCode con código inválido lanza excepción")
    void shouldThrowForInvalidCode() {
        assertThatThrownBy(() -> CurrencyCode.of("XYZ"))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
