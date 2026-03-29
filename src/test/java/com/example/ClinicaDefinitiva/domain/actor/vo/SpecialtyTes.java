package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class SpecialtyTest {

    @ParameterizedTest
    @ValueSource(strings = {"Orthodontics", "Endodontics", "Periodontics", "Prosthodontics",
            "Pediatric Dentistry", "Oral Surgery", "General Dentistry"})
    @DisplayName("Crear Specialty válida")
    void shouldCreateValidSpecialty(String value) {
        Specialty specialty = Specialty.of(value);
        assertThat(specialty.asText()).isEqualTo(value);
        assertThat(specialty.is(value)).isTrue();
    }

    @Test
    @DisplayName("Specialty inválida lanza excepción")
    void shouldThrowForInvalidSpecialty() {
        assertThatThrownBy(() -> Specialty.of("Invalid"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Specialty null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> Specialty.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Specialty vacía lanza excepción")
    void shouldThrowForBlank() {
        assertThatThrownBy(() -> Specialty.of("   "))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
