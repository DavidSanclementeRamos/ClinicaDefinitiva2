package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class InvoiceNumberTest {

    @ParameterizedTest
    @ValueSource(strings = {"FAC-0001", "INV-0123", "BILL-1234", "PRF-99999999"})
    @DisplayName("Crear InvoiceNumber con formato válido")
    void shouldCreateValidNumber(String value) {
        InvoiceNumber number = InvoiceNumber.of(value);
        assertThat(number.getValue()).isEqualTo(value.toUpperCase());
    }

    @ParameterizedTest
    @ValueSource(strings = {"FAC0001", "FAC-1", "FAC-123456789", "FA-0001", "FAC-A001", ""})
    @DisplayName("Crear InvoiceNumber con formato inválido lanza excepción")
    void shouldThrowForInvalidFormat(String invalid) {
        assertThatThrownBy(() -> InvoiceNumber.of(invalid))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Crear InvoiceNumber con null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> InvoiceNumber.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Crear InvoiceNumber desde prefijo y secuencia")
    void shouldCreateFromPrefixAndSequence() {
        InvoiceNumber number = InvoiceNumber.from("FAC", 1);
        assertThat(number.getValue()).isEqualTo("FAC-0001");
        assertThat(number.getPrefix()).isEqualTo("FAC");
        assertThat(number.getSequence()).isEqualTo(1);
    }

    @Test
    @DisplayName("Crear InvoiceNumber con prefijo nulo lanza excepción")
    void shouldThrowForNullPrefix() {
        assertThatThrownBy(() -> InvoiceNumber.from(null, 1))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Crear InvoiceNumber con secuencia negativa lanza excepción")
    void shouldThrowForNegativeSequence() {
        assertThatThrownBy(() -> InvoiceNumber.from("FAC", -1))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("hasPrefix() funciona correctamente")
    void testHasPrefix() {
        InvoiceNumber number = InvoiceNumber.of("FAC-0001");
        assertThat(number.hasPrefix("FAC")).isTrue();
        assertThat(number.hasPrefix("INV")).isFalse();
    }
}
