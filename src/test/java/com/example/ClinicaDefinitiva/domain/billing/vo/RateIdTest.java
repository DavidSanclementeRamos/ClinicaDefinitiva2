package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RateIdTest {

    @Test
    @DisplayName("Crear RateId con valor válido")
    void of_withValidValue_shouldCreate() {
        RateId id = RateId.of(1L);
        assertThat(id.getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Crear RateId con null lanza excepción")
    void of_withNull_shouldThrow() {
        assertThatThrownBy(() -> RateId.of(null))
                .isInstanceOf(ValueObjectValidationException.class)
                .hasMessageContaining("El identificador de la tarifa (RateId) no puede ser nulo");
    }

    @Test
    @DisplayName("Igualdad en records funciona por valor")
    void equals_shouldWork() {
        RateId id1 = RateId.of(1L);
        RateId id2 = RateId.of(1L);
        RateId id3 = RateId.of(2L);
        assertThat(id1).isEqualTo(id2);
        assertThat(id1).isNotEqualTo(id3);
    }
}
