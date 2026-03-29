package com.example.ClinicaDefinitiva.domain.payment.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PaymentStatusTest {

    @Test
    @DisplayName("Crear estados")
    void createStatuses() {
        assertThat(PaymentStatus.pending().isPending()).isTrue();
        assertThat(PaymentStatus.confirmed().isConfirmed()).isTrue();
        assertThat(PaymentStatus.failed().isFailed()).isTrue();
        assertThat(PaymentStatus.refunded().isRefunded()).isTrue();
        assertThat(PaymentStatus.cancelled().isCancelled()).isTrue();
    }

    @Test
    @DisplayName("Transiciones válidas desde PENDING")
    void transitionsFromPending() {
        PaymentStatus pending = PaymentStatus.pending();
        assertThat(pending.canTransitionTo(PaymentStatus.Status.CONFIRMED)).isTrue();
        assertThat(pending.canTransitionTo(PaymentStatus.Status.FAILED)).isTrue();
        assertThat(pending.canTransitionTo(PaymentStatus.Status.CANCELLED)).isTrue();
        assertThat(pending.canTransitionTo(PaymentStatus.Status.REFUNDED)).isFalse();
    }

    @Test
    @DisplayName("Transiciones válidas desde CONFIRMED")
    void transitionsFromConfirmed() {
        PaymentStatus confirmed = PaymentStatus.confirmed();
        assertThat(confirmed.canTransitionTo(PaymentStatus.Status.REFUNDED)).isTrue();
        assertThat(confirmed.canTransitionTo(PaymentStatus.Status.PENDING)).isFalse();
        assertThat(confirmed.canTransitionTo(PaymentStatus.Status.FAILED)).isFalse();
    }

    @Test
    @DisplayName("Transiciones inválidas lanzan excepción")
    void transitionToInvalid_throws() {
        PaymentStatus pending = PaymentStatus.pending();
        assertThatThrownBy(() -> pending.transitionTo(PaymentStatus.Status.REFUNDED))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("Consultas semánticas")
    void semanticQueries() {
        PaymentStatus confirmed = PaymentStatus.confirmed();
        assertThat(confirmed.isSuccessful()).isTrue();
        assertThat(confirmed.isFinal()).isFalse();

        PaymentStatus failed = PaymentStatus.failed();
        assertThat(failed.isFinal()).isTrue();
    }
}
