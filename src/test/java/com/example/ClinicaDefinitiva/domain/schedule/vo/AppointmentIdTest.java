package com.example.ClinicaDefinitiva.domain.schedule.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AppointmentIdTest {

    @Test
    @DisplayName("Crear AppointmentId válido")
    void shouldCreateValidId() {
        AppointmentId id = AppointmentId.of(1L);
        assertThat(id.getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Crear AppointmentId con null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> AppointmentId.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
