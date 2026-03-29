package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OrthodonticDetailsTest {

    @Test
    @DisplayName("Crear detalles de ortodoncia válidos")
    void shouldCreateValid() {
        OrthodonticDetails details = new OrthodonticDetails(
                "METAL_BRACKETS", 24, true
        );
        assertThat(details.getApplianceType()).isEqualTo("METAL_BRACKETS");
        assertThat(details.getTreatmentDurationMonths()).isEqualTo(24);
        assertThat(details.getRequiresFollowup()).isTrue();
    }

    @Test
    @DisplayName("Tipo de aparato obligatorio")
    void shouldRequireApplianceType() {
        assertThatThrownBy(() -> new OrthodonticDetails(null, 24, true))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Tipo de aparato debe ser válido")
    void shouldValidateApplianceType() {
        assertThatThrownBy(() -> new OrthodonticDetails("INVALID", 24, true))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Duración debe estar entre 6 y 48 meses")
    void shouldValidateDurationRange() {
        assertThatThrownBy(() -> new OrthodonticDetails("METAL_BRACKETS", 5, true))
                .isInstanceOf(ValueObjectValidationException.class);
        assertThatThrownBy(() -> new OrthodonticDetails("METAL_BRACKETS", 49, true))
                .isInstanceOf(ValueObjectValidationException.class);
        new OrthodonticDetails("METAL_BRACKETS", 6, true);  // ok
        new OrthodonticDetails("METAL_BRACKETS", 48, true); // ok
    }

    @Test
    @DisplayName("Duración negativa lanza excepción")
    void shouldRejectNegativeDuration() {
        assertThatThrownBy(() -> new OrthodonticDetails("METAL_BRACKETS", -1, true))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
