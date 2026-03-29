package com.example.ClinicaDefinitiva.domain.payment.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PaymentIdTest {

    @Test
    @DisplayName("Crear PaymentId válido")
    void shouldCreateValidId() {
        PaymentId id = PaymentId.of(1L);
        assertThat(id.value()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Crear PaymentId con null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> PaymentId.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
