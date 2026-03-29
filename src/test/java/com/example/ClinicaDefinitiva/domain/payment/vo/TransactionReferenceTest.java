package com.example.ClinicaDefinitiva.domain.payment.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TransactionReferenceTest {

    @Test
    @DisplayName("Crear referencia válida")
    void createValid() {
        TransactionReference ref = TransactionReference.of("pi_123456");
        assertThat(ref.value()).isEqualTo("pi_123456");
        assertThat(ref.hasGatewayId()).isFalse();
    }

    @Test
    @DisplayName("Crear referencia con gatewayPaymentId")
    void createWithGatewayId() {
        TransactionReference ref = TransactionReference.of("tx_123", "pi_123");
        assertThat(ref.value()).isEqualTo("tx_123");
        assertThat(ref.getGatewayPaymentId()).isEqualTo("pi_123");
        assertThat(ref.hasGatewayId()).isTrue();
    }

    @Test
    @DisplayName("Valor nulo o vacío lanza excepción")
    void nullOrEmpty_throws() {
        assertThatThrownBy(() -> TransactionReference.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
        assertThatThrownBy(() -> TransactionReference.of(""))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Valor muy largo lanza excepción")
    void tooLong_throws() {
        String longString = "a".repeat(256);
        assertThatThrownBy(() -> TransactionReference.of(longString))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Generar referencia para efectivo")
    void generateCashReceipt() {
        TransactionReference ref = TransactionReference.generateCashReceipt();
        assertThat(ref.value()).startsWith("CASH-");
    }
}
