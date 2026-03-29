package com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TreatmentIdTest {

    @Test
    @DisplayName("Crear TreatmentId válido")
    void shouldCreateValidId() {
        TreatmentId id = TreatmentId.of(1L);
        assertThat(id.getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Crear TreatmentId con null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> TreatmentId.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
