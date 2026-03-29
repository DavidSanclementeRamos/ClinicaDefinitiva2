package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ServiceIdTest {

    @Test
    @DisplayName("Crear ServiceId válido")
    void shouldCreateValidId() {
        ServiceId id = ServiceId.of(1L);
        assertThat(id.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Crear ServiceId con null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> ServiceId.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
