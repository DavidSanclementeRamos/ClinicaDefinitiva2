package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DentistIdTest {

    @Test
    @DisplayName("Crear DentistId válido")
    void shouldCreateValidId() {
        DentistId id = DentistId.of(1L);
        assertThat(id.value()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Crear DentistId con null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> DentistId.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}