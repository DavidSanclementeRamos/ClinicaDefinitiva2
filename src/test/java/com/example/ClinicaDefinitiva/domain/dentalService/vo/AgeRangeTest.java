package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AgeRangeTest {

    @Test
    @DisplayName("Crear rango válido")
    void shouldCreateValidRange() {
        AgeRange range = AgeRange.of(3, 12);
        assertThat(range.getMinAge()).isEqualTo(3);
        assertThat(range.getMaxAge()).isEqualTo(12);
    }

    @Test
    @DisplayName("Edad mínima negativa lanza excepción")
    void shouldThrowForNegativeMin() {
        assertThatThrownBy(() -> AgeRange.of(-1, 10))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Rango con max <= min lanza excepción")
    void shouldThrowForInvalidRange() {
        assertThatThrownBy(() -> AgeRange.of(5, 5))
                .isInstanceOf(ValueObjectValidationException.class);
        assertThatThrownBy(() -> AgeRange.of(10, 5))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}