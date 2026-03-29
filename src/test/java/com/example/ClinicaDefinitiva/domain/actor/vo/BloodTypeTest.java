package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class BloodTypeTest {

    @ParameterizedTest
    @ValueSource(strings = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"})
    @DisplayName("Crear BloodType con tipos válidos")
    void shouldCreateValidBloodType(String type) {
        BloodType bloodType = BloodType.fromLabel(type);
        assertThat(bloodType.getValue()).isEqualTo(type.toUpperCase());
    }

    @Test
    @DisplayName("Crear BloodType con tipo inválido lanza excepción")
    void shouldThrowForInvalidBloodType() {
        assertThatThrownBy(() -> BloodType.fromLabel("Z+"))
                .isInstanceOf(ValueObjectValidationException.class)
                .hasMessageContaining("El tipo de sangre especificado no es válido");
    }
}
