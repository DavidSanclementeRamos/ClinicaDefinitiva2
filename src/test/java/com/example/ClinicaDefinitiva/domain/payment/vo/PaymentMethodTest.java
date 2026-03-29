package com.example.ClinicaDefinitiva.domain.payment.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PaymentMethodTest {

    @Test
    @DisplayName("fromString devuelve enum correcto")
    void fromString_valid() {
        assertThat(PaymentMethod.fromString("CASH")).isEqualTo(PaymentMethod.CASH);
        assertThat(PaymentMethod.fromString("STRIPE")).isEqualTo(PaymentMethod.STRIPE);
    }

    @Test
    @DisplayName("fromString con null o vacío lanza excepción")
    void fromString_nullOrEmpty_throws() {
        assertThatThrownBy(() -> PaymentMethod.fromString(null))
                .isInstanceOf(ValueObjectValidationException.class);
        assertThatThrownBy(() -> PaymentMethod.fromString(""))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("fromString con valor inválido lanza excepción")
    void fromString_invalid_throws() {
        assertThatThrownBy(() -> PaymentMethod.fromString("INVALID"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("requiresGateway() correcto")
    void requiresGateway() {
        assertThat(PaymentMethod.STRIPE.requiresGateway()).isTrue();
        assertThat(PaymentMethod.EPS.requiresGateway()).isTrue();
        assertThat(PaymentMethod.CASH.requiresGateway()).isFalse();
        assertThat(PaymentMethod.CARD.requiresGateway()).isFalse();
    }

    @Test
    @DisplayName("requiresContract() correcto")
    void requiresContract() {
        assertThat(PaymentMethod.EPS.requiresContract()).isTrue();
        assertThat(PaymentMethod.CONTRACT.requiresContract()).isTrue();
        assertThat(PaymentMethod.CASH.requiresContract()).isFalse();
        assertThat(PaymentMethod.STRIPE.requiresContract()).isFalse();
    }

    @Test
    @DisplayName("isImmediate() correcto")
    void isImmediate() {
        assertThat(PaymentMethod.CASH.isImmediate()).isTrue();
        assertThat(PaymentMethod.STRIPE.isImmediate()).isFalse();
    }
}
