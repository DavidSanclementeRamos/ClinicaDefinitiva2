package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserRolAssignmentIdTest {

    @Test
    @DisplayName("Crear UserRolAssignmentId válido")
    void shouldCreateValidId() {
        UserRolAssignmentId id = UserRolAssignmentId.of(1L);
        assertThat(id.getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Crear UserRolAssignmentId con null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> UserRolAssignmentId.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
