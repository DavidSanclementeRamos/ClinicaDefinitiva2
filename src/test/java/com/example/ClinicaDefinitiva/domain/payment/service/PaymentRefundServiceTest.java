package com.example.ClinicaDefinitiva.domain.payment.service;

import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.payment.event.PaymentRefundedEvent;
import com.example.ClinicaDefinitiva.domain.payment.model.Payment;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentGateway;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentGatewayResult;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentRepository;
import com.example.ClinicaDefinitiva.domain.payment.vo.Payer;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentMethod;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentId;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentStatus;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Currency;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentRefundServiceTest {

    private static final Currency COP = Currency.getInstance("COP");
    private static final Price AMOUNT = Price.of(100_000, COP);
    private static final Price REFUND_AMOUNT = Price.of(50_000, COP);

    @Mock
    private Map<PaymentMethod, PaymentGateway> gateways;
    @Mock
    private PaymentGateway stripeGateway;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PaymentRefundService refundService;

    private Payment payment;  // ← OBJETO REAL, no mock
    private PaymentId paymentId;

    @BeforeEach
    void setUp() {
        // Crear objeto REAL de Payment
        payment = Payment.createPending(
                InvoiceId.of(1L), 
                AMOUNT, 
                PaymentMethod.STRIPE, 
                Payer.patient("Juan")
        );
        payment.confirm("txn_123", "pi_456");
        paymentId = payment.getId();
    }

    @Test
    @DisplayName("Reembolso exitoso con gateway (objeto real)")
    void refundPayment_success() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(gateways.get(PaymentMethod.STRIPE)).thenReturn(stripeGateway);
        when(stripeGateway.isAvailable()).thenReturn(true);
        when(stripeGateway.refundPayment(any(), any())).thenReturn(
                new PaymentGatewayResult(true, "refund_123","REFUNDED", null, null)
        );
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);  // ← devuelve el mismo objeto

        Payment result = refundService.refundPayment(paymentId, REFUND_AMOUNT, "Devolución");

        // Ahora result es el mismo objeto que payment
        assertThat(result).isSameAs(payment);
        assertThat(result.getRefundedAmount()).isEqualTo(REFUND_AMOUNT);
        verify(stripeGateway).refundPayment("txn_123", REFUND_AMOUNT);
    }

    @Test
    @DisplayName("Reembolso con gateway fallido lanza excepción")
    void refundPayment_gatewayFails_throws() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(gateways.get(PaymentMethod.STRIPE)).thenReturn(stripeGateway);
        when(stripeGateway.isAvailable()).thenReturn(true);
        when(stripeGateway.refundPayment(any(), any())).thenReturn(
                new PaymentGatewayResult(false, null, null,PaymentStatus.pending(), "Error en gateway")
        );

        assertThatThrownBy(() -> refundService.refundPayment(paymentId, REFUND_AMOUNT, "Devolución"))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("El reembolso del pago ha fallado");
    }

    @Test
    @DisplayName("Reembolso de pago en efectivo (sin gateway)")
    void refundPayment_cash_success() {
        Payment cashPayment = Payment.createPending(
                InvoiceId.of(1L), 
                AMOUNT, 
                PaymentMethod.CASH, 
                Payer.patient("Juan")
        );
        cashPayment.confirmCashPayment();
        PaymentId cashId = cashPayment.getId();

        when(paymentRepository.findById(cashId)).thenReturn(Optional.of(cashPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(cashPayment);

        Payment result = refundService.refundPayment(cashId, REFUND_AMOUNT, "Devolución");

        assertThat(result).isSameAs(cashPayment);
        assertThat(result.getRefundedAmount()).isEqualTo(REFUND_AMOUNT);
          verify(eventPublisher, times(1)).publishEvent(any(PaymentRefundedEvent.class));
    verify(gateways, never()).get(any());
    }
}
