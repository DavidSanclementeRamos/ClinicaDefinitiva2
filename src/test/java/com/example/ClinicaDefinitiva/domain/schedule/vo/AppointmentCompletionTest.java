package com.example.ClinicaDefinitiva.domain.schedule.vo;

import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AppointmentCompletionTest {

    @Test
    @DisplayName("Crear completion válido")
    void shouldCreateValid() {
        ServiceDuration duration = ServiceDuration.of(45);
        AppointmentCompletion completion = new AppointmentCompletion(duration, "Notas clínicas detalladas");

        assertThat(completion.getActualDuration()).isEqualTo(duration);
        assertThat(completion.getClinicalNotes()).isEqualTo("Notas clínicas detalladas");
    }

    @Test
    @DisplayName("Duración nula o cero lanza excepción")
    void shouldRequireValidDuration() {
        assertThatThrownBy(() -> new AppointmentCompletion(null, "Notas"))
                .isInstanceOf(ValueObjectValidationException.class);
        assertThatThrownBy(() -> new AppointmentCompletion(ServiceDuration.of(0), "Notas"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Notas clínicas obligatorias")
    void shouldRequireClinicalNotes() {
        ServiceDuration duration = ServiceDuration.of(30);
        assertThatThrownBy(() -> new AppointmentCompletion(duration, null))
                .isInstanceOf(ValueObjectValidationException.class);
        assertThatThrownBy(() -> new AppointmentCompletion(duration, ""))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
