package com.example.ClinicaDefinitiva.domain.payment.model;

import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.payment.vo.*;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Currency;

import static org.assertj.core.api.Assertions.*;

class PaymentTest {

    private static final Currency COP = Currency.getInstance("COP");
    private Price amount;
    private InvoiceId invoiceId;
    private Payer payer;

    @BeforeEach
    void setUp() {
        amount = Price.of(100_000, COP);
        invoiceId = InvoiceId.of(1L);
        payer = Payer.patient("Juan Pérez");
    }

    @Test
    @DisplayName("PAY-UNIT-001: Crear pago pendiente")
    void createPending() {
        Payment payment = Payment.createPending(invoiceId, amount, PaymentMethod.CASH, payer);
        assertThat(payment.isPending()).isTrue();
        assertThat(payment.getAmount()).isEqualTo(amount);
        assertThat(payment.getInvoiceId()).isEqualTo(invoiceId);
        assertThat(payment.getRefundedAmount().isZero()).isTrue();
    }

    @Test
    @DisplayName("PAY-UNIT-002: Confirmar pago en efectivo")
    void confirmCashPayment() {
        Payment payment = Payment.createPending(invoiceId, amount, PaymentMethod.CASH, payer);
        payment.confirmCashPayment();
        assertThat(payment.isConfirmed()).isTrue();
        assertThat(payment.getTransactionReference()).isNotNull();
        assertThat(payment.getTransactionReference().value()).startsWith("CASH-");
    }

    @Test
    @DisplayName("PAY-UNIT-003: Confirmar pago con gateway")
    void confirmWithGateway() {
        Payment payment = Payment.createPending(invoiceId, amount, PaymentMethod.STRIPE, payer);
        payment.confirm("tx_123", "pi_123");
        assertThat(payment.isConfirmed()).isTrue();
        assertThat(payment.getTransactionReference().value()).isEqualTo("tx_123");
        assertThat(payment.getTransactionReference().getGatewayPaymentId()).isEqualTo("pi_123");
    }

    @Test
    @DisplayName("PAY-UNIT-004: Fallar pago")
    void failPayment() {
        Payment payment = Payment.createPending(invoiceId, amount, PaymentMethod.STRIPE, payer);
        payment.fail("Error de conexión");
        assertThat(payment.isFailed()).isTrue();
        assertThat(payment.getErrorMessage()).isEqualTo("Error de conexión");
    }

    @Test
    @DisplayName("PAY-UNIT-005: Cancelar pago pendiente")
    void cancelPayment() {
        Payment payment = Payment.createPending(invoiceId, amount, PaymentMethod.STRIPE, payer);
        payment.cancel("Cliente canceló");
        assertThat(payment.isCancelled()).isTrue();
    }

    @Test
    @DisplayName("PAY-UNIT-006: Reembolso parcial")
    void refundPartial() {
        Payment payment = createConfirmedPayment(PaymentMethod.CASH);
        Price refundAmount = Price.of(30_000, COP);
        payment.refund(refundAmount, "Sobrepago");
        assertThat(payment.isConfirmed()).isTrue(); // aún confirmado
        assertThat(payment.getRefundedAmount()).isEqualTo(refundAmount);
        assertThat(payment.getRemainingAmount()).isEqualTo(Price.of(70_000, COP));
        assertThat(payment.isPartiallyRefunded()).isTrue();
        assertThat(payment.isFullyRefunded()).isFalse();
    }

    @Test
    @DisplayName("PAY-UNIT-007: Reembolso total")
    void refundFull() {
        Payment payment = createConfirmedPayment(PaymentMethod.CASH);
        payment.refund(amount, "Devolución total");
        assertThat(payment.isRefunded()).isTrue();
        assertThat(payment.isFullyRefunded()).isTrue();
        assertThat(payment.getRefundedAmount()).isEqualTo(amount);
    }

    @Test
    @DisplayName("PAY-UNIT-008: Reembolso excede monto")
    void refundExceedsAmount_throws() {
        Payment payment = createConfirmedPayment(PaymentMethod.CASH);
        Price excess = Price.of(150_000, COP);
        assertThatThrownBy(() -> payment.refund(excess, "Excede"))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("El reembolso excede el monto original");
    }

    @Test
    @DisplayName("PAY-UNIT-009: Confirmar pago no pendiente lanza excepción")
    void confirmWhenNotPending_throws() {
        Payment payment = createConfirmedPayment(PaymentMethod.CASH);
        assertThatThrownBy(() -> payment.confirm("tx", "id"))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("PAY-UNIT-010: Reembolsar pago no confirmado lanza excepción")
    void refundWhenNotConfirmed_throws() {
        Payment payment = Payment.createPending(invoiceId, amount, PaymentMethod.CASH, payer);
        assertThatThrownBy(() -> payment.refund(Price.of(10_000, COP), "Razón"))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    private Payment createConfirmedPayment(PaymentMethod method) {
        Payment payment = Payment.createPending(invoiceId, amount, method, payer);
        if (method == PaymentMethod.CASH) {
            payment.confirmCashPayment();
        } else {
            payment.confirm("tx", "gw");
        }
        return payment;
    }
}
