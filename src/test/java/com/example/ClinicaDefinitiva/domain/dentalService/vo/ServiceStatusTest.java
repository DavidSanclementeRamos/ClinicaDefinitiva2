package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ServiceStatusTest {

    @Test
    @DisplayName("Crear estado ACTIVE")
    void shouldCreateActive() {
        ServiceStatus status = ServiceStatus.of(ServiceStatus.State.ACTIVE);
        assertThat(status.isActive()).isTrue();
        assertThat(status.getValue()).isEqualTo(ServiceStatus.State.ACTIVE);
        assertThat(status.getDescription()).isEqualTo("Activo");
    }

    @Test
    @DisplayName("Crear estado INACTIVE")
    void shouldCreateInactive() {
        ServiceStatus status = ServiceStatus.of(ServiceStatus.State.INACTIVE);
        assertThat(status.isActive()).isFalse();
    }
}
