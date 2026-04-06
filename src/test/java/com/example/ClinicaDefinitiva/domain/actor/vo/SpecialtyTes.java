package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class SpecialtyTest {

    @ParameterizedTest
    @ValueSource(strings = {"ORTHODONTICS", "Endodontics", "periodontics", "Prosthodontics",
            "PEDIATRIC_DENTISTRY", "Oral Surgery", "GENERAL_DENTISTRY"})
    @DisplayName("Crear Specialty válida desde string (case-insensitive)")
    void shouldCreateValidSpecialty(String value) {
        Specialty specialty = Specialty.fromString(value);
        assertThat(specialty).isNotNull();
        assertThat(specialty.getCode()).isEqualTo(value.toUpperCase().replace(" ", "_"));
    }

    @Test
    @DisplayName("Specialty inválida lanza excepción")
    void shouldThrowForInvalidSpecialty() {
        assertThatThrownBy(() -> Specialty.fromString("Invalid"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Specialty null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> Specialty.fromString(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Specialty vacía lanza excepción")
    void shouldThrowForBlank() {
        assertThatThrownBy(() -> Specialty.fromString("   "))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}