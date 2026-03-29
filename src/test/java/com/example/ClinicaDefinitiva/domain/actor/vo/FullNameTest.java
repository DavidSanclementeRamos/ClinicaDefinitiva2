package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FullNameTest {

    @Test
    @DisplayName("Crear FullName válido")
    void shouldCreateValidFullName() {
        FullName name = FullName.of("Juan", "Pérez");
        assertThat(name.getFirstName()).isEqualTo("Juan");
        assertThat(name.getLastName()).isEqualTo("Pérez");
        assertThat(name.asText()).isEqualTo("Juan Pérez");
        assertThat(name.initials()).isEqualTo("JP");
    }

    @Test
    @DisplayName("Nombre nulo lanza excepción")
    void shouldThrowForNullFirstName() {
        assertThatThrownBy(() -> FullName.of(null, "Pérez"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Apellido nulo lanza excepción")
    void shouldThrowForNullLastName() {
        assertThatThrownBy(() -> FullName.of("Juan", null))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Nombre en blanco lanza excepción")
    void shouldThrowForBlankFirstName() {
        assertThatThrownBy(() -> FullName.of(" ", "Pérez"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Apellido en blanco lanza excepción")
    void shouldThrowForBlankLastName() {
        assertThatThrownBy(() -> FullName.of("Juan", " "))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("matches() ignora mayúsculas/minúsculas")
    void testMatches() {
        FullName name = FullName.of("Juan", "Pérez");
        assertThat(name.matches("JUAN PÉREZ")).isTrue();
        assertThat(name.matches("Juan Pérez")).isTrue();
        assertThat(name.matches("Juan Perez")).isFalse(); // acento
    }

    @Test
    @DisplayName("startsWith() funciona correctamente")
    void testStartsWith() {
        FullName name = FullName.of("Juan", "Pérez");
        assertThat(name.startsWith("Juan")).isTrue();
        assertThat(name.startsWith("ju")).isTrue();
        assertThat(name.startsWith("Pedro")).isFalse();
    }
}