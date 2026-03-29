package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ServiceCodeTest {

    @Test
    @DisplayName("Crear código válido")
    void shouldCreateValidCode() {
        ServiceCode code = ServiceCode.of("ORT-001");
        assertThat(code.getValue()).isEqualTo("ORT-001");
    }

    @Test
    @DisplayName("Código con longitud incorrecta lanza excepción")
    void shouldThrowForInvalidLength() {
        assertThatThrownBy(() -> ServiceCode.of("A"))
                .isInstanceOf(ValueObjectValidationException.class);
        assertThatThrownBy(() -> ServiceCode.of("ABCDEFGHIJKLMNOPQRSTUVWXYZ"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Código con formato inválido lanza excepción")
    void shouldThrowForInvalidFormat() {
        assertThatThrownBy(() -> ServiceCode.of("ort@001"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("ensureUniqueCode lanza excepción si el código ya existe")
    void ensureUniqueCode_shouldThrowIfExists() {
        ServiceCode code = ServiceCode.of("ORT-001");
        assertThatThrownBy(() -> code.ensureUniqueCode(true))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
