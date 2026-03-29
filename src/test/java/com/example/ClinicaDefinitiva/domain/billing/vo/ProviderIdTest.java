package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ProviderIdTest {

    @Test
    @DisplayName("Crear ProviderId válido")
    void shouldCreateValidId() {
        ProviderId id = ProviderId.of(1L);
        assertThat(id.getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Crear ProviderId con null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> ProviderId.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
