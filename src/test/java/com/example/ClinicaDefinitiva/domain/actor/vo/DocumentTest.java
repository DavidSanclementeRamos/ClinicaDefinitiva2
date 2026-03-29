package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class DocumentTest {

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "1234567890", "123456"})
    @DisplayName("Crear documento con formato válido (6-10 dígitos)")
    void shouldCreateValidDocument(String raw) {
        Document doc = Document.of(raw);
        assertThat(doc.value()).isEqualTo(raw);
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "12345678901", "12-345", "abc123"})
    @DisplayName("Documento con formato inválido lanza excepción")
    void shouldThrowForInvalidDocument(String raw) {
        assertThatThrownBy(() -> Document.of(raw))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Documento nulo lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> Document.of(null))
                .isInstanceOf(ValueObjectValidationException.class)
                .hasMessageContaining("El documento no puede ser nulo");
    }

    @Test
    @DisplayName("Documento en blanco lanza excepción")
    void shouldThrowForBlank() {
        assertThatThrownBy(() -> Document.of("   "))
                .isInstanceOf(ValueObjectValidationException.class)
                .hasMessageContaining("El documento no puede estar vacío");
    }
}