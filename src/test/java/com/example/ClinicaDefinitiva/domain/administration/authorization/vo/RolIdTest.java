package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RolIdTest {

    @Test
    @DisplayName("Crear RolId válido")
    void shouldCreateValidId() {
        RolId id = RolId.of(1L);
        assertThat(id.getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Crear RolId con null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> RolId.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
