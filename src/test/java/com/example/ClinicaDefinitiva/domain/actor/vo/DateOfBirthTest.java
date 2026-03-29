package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class DateOfBirthTest {

    @Test
    @DisplayName("Crear DateOfBirth válida")
    void shouldCreateValidDate() {
        LocalDate date = LocalDate.of(1990, 5, 15);
        DateOfBirth dob = DateOfBirth.of(date);
        assertThat(dob.asDate()).isEqualTo(date);
    }

    @Test
    @DisplayName("Fecha futura lanza excepción")
    void shouldThrowForFutureDate() {
        LocalDate future = LocalDate.now().plusDays(1);
        assertThatThrownBy(() -> DateOfBirth.of(future))
                .isInstanceOf(ValueObjectValidationException.class)
                .hasMessageContaining("La fecha de nacimiento no puede ser futura");
    }

    @Test
    @DisplayName("Fecha con edad >130 años lanza excepción")
    void shouldThrowForTooOld() {
        LocalDate tooOld = LocalDate.now().minusYears(131);
        assertThatThrownBy(() -> DateOfBirth.of(tooOld))
                .isInstanceOf(ValueObjectValidationException.class)
                .hasMessageContaining("La fecha de nacimiento excede el rango válido (edad > 130 años)");
    }
}