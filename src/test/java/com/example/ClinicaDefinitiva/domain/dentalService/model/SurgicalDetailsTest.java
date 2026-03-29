package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SurgicalDetailsTest {

    @Test
    @DisplayName("Crear cirugía de baja complejidad sin anestesia ni quirófano")
    void shouldCreateLowComplexity() {
        SurgicalDetails details = new SurgicalDetails(
                "Extracción simple", "LOW", false, false
        );
        assertThat(details.getComplexityLevel()).isEqualTo("LOW");
        assertThat(details.getRequiresAnesthesia()).isFalse();
        assertThat(details.getOperatingRoomNeeded()).isFalse();
    }

    @Test
    @DisplayName("Cirugía de baja complejidad NO debe requerir anestesia")
    void lowComplexityShouldNotRequireAnesthesia() {
        assertThatThrownBy(() -> new SurgicalDetails("Extracción", "LOW", true, false))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Cirugía de baja complejidad NO debe requerir quirófano")
    void lowComplexityShouldNotRequireOperatingRoom() {
        assertThatThrownBy(() -> new SurgicalDetails("Extracción", "LOW", false, true))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Cirugía crítica debe requerir anestesia y quirófano")
    void criticalRequiresBoth() {
        assertThatThrownBy(() -> new SurgicalDetails("Cirugía compleja", "CRITICAL", false, true))
                .isInstanceOf(ValueObjectValidationException.class);
        assertThatThrownBy(() -> new SurgicalDetails("Cirugía compleja", "CRITICAL", true, false))
                .isInstanceOf(ValueObjectValidationException.class);
        new SurgicalDetails("Cirugía compleja", "CRITICAL", true, true); // ok
    }

    @Test
    @DisplayName("Nivel de complejidad debe ser válido")
    void shouldValidateComplexityLevel() {
        assertThatThrownBy(() -> new SurgicalDetails("Cirugía", "EXTREME", false, false))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
