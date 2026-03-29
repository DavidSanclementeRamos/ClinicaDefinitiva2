package com.example.ClinicaDefinitiva.domain.authentication.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserIdentityIdTest {

    @Test
    @DisplayName("Crear UserIdentityId válido")
    void shouldCreateValidId() {
        UserIdentityId id = UserIdentityId.from(1L);
        assertThat(id.value()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Crear con null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> UserIdentityId.from(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
