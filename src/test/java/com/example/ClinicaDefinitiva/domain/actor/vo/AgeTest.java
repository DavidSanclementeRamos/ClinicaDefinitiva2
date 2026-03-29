package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class AgeTest {

    @Test
    @DisplayName("Crear Age a partir de DateOfBirth válido")
    void shouldCreateAge() {
        DateOfBirth dob = DateOfBirth.of(LocalDate.of(1990, 5, 15));
        Age age = Age.of(dob);
        assertThat(age.Value()).isPositive();
        assertThat(age.isAdult()).isTrue();
        assertThat(age.isElderly()).isFalse();
        assertThat(age.isEligibleForRegistration()).isTrue();
        assertThat(age.ageCategory()).isEqualTo("Adult");
    }

    @Test
    @DisplayName("Edad negativa (fecha futura) lanza excepción en DateOfBirth")
    void shouldThrowForFutureDate() {
        assertThatThrownBy(() -> DateOfBirth.of(LocalDate.now().plusDays(1)))
                .isInstanceOf(ValueObjectValidationException.class)
                .hasMessageContaining("La fecha de nacimiento no puede ser futura");
    }

    @Test
    @DisplayName("Edad > 130 lanza excepción en DateOfBirth")
    void shouldThrowForAgeOver130() {
        assertThatThrownBy(() -> DateOfBirth.of(LocalDate.now().minusYears(131)))
                .isInstanceOf(ValueObjectValidationException.class)
                .hasMessageContaining("La fecha de nacimiento excede el rango válido (edad > 130 años)");
    }

    @Test
    @DisplayName("isBetween() funciona correctamente")
    void testIsBetween() {
        DateOfBirth dob = DateOfBirth.of(LocalDate.of(1990, 5, 15));
        Age age = Age.of(dob);
        assertThat(age.isBetween(25, 40)).isTrue();
        assertThat(age.isBetween(18, 30)).isFalse();
    }

    @Test
    @DisplayName("isAdult() funciona correctamente")
    void testIsAdult() {
        DateOfBirth dob = DateOfBirth.of(LocalDate.of(2010, 5, 15));
        Age age = Age.of(dob);
        assertThat(age.isAdult()).isFalse();
    }

    @Test
    @DisplayName("ageCategory() para menores de 13")
    void testAgeCategoryChild() {
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(10));
        Age age = Age.of(dob);
        assertThat(age.ageCategory()).isEqualTo("Child");
    }
}