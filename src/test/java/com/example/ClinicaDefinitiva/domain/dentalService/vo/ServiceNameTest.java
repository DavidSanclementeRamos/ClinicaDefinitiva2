package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ServiceNameTest {

    @Test
    @DisplayName("Crear nombre a partir de enum")
    void shouldCreateFromEnum() {
        ServiceName name = ServiceName.of(ServiceName.DentalServiceName.CLEANING);
        assertThat(name.getValue()).isEqualTo("CLEANING");
    }

    @Test
    @DisplayName("Crear nombre personalizado válido")
    void shouldCreateCustomName() {
        ServiceName name = ServiceName.custom("Ortodoncia Invisible");
        assertThat(name.getValue()).isEqualTo("Ortodoncia Invisible");
    }

    @Test
    @DisplayName("Crear nombre personalizado muy corto lanza excepción")
    void shouldThrowForShortCustomName() {
        assertThatThrownBy(() -> ServiceName.custom("A"))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
