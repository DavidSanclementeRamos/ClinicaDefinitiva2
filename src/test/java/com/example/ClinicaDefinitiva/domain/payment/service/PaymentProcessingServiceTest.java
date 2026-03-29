package com.example.ClinicaDefinitiva.domain.payment.service;

import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.output.InvoiceRepository;
import com.example.ClinicaDefinitiva.domain.billing.vo.CurrencyCode;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceNumber;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceStatus;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.payment.model.Payment;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentGateway;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentGatewayResult;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentRepository;
import com.example.ClinicaDefinitiva.domain.payment.vo.*;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentProcessingServiceTest {

    @Mock
    private Map<PaymentMethod, PaymentGateway> gateways;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private PaymentGateway stripeGateway;
    @Mock
    private PaymentGateway epsGateway;

    @InjectMocks
    private PaymentProcessingService service;

    private static final Currency COP = Currency.getInstance("COP");
    private static final InvoiceId INVOICE_ID = InvoiceId.of(1L);
    private static final Price AMOUNT = Price.of(100_000, COP);
    private static final CurrencyCode CURRENCY_CODE = CurrencyCode.of("COP");

    @BeforeEach
    void setUp() {
        // No configurar stubs globales
    }

    @Test
    @DisplayName("PAY-INT-001: Procesar pago en efectivo exitosamente")
    void processPayment_cash_success() {
        // Configurar invoice con TODOS los métodos necesarios para el flujo completo
        Invoice invoice = mock(Invoice.class);
        when(invoice.getId()).thenReturn(INVOICE_ID);
        when(invoice.getTotal()).thenReturn(AMOUNT);
        when(invoice.getStatus()).thenReturn(InvoiceStatus.pending());
        when(invoice.getCurrency()).thenReturn(CURRENCY_CODE);  // ← NECESARIO para calculateRemainingAmount
        
        // Nota: getNumber() NO es necesario para efectivo, pero calculateRemainingAmount no lo usa
        
        when(invoiceRepository.findById(INVOICE_ID)).thenReturn(Optional.of(invoice));
        when(paymentRepository.findByInvoiceId(INVOICE_ID)).thenReturn(List.of());
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payer payer = Payer.patient("Juan Pérez");

        Payment result = service.processPayment(
                INVOICE_ID, AMOUNT, PaymentMethod.CASH, "email@test.com", "Juan", payer
        );

        assertThat(result).isNotNull();
        assertThat(result.isConfirmed()).isTrue();
        assertThat(result.getAmount()).isEqualTo(AMOUNT);
        
        verify(invoiceRepository).findById(INVOICE_ID);
        verify(paymentRepository, atLeastOnce()).save(any(Payment.class));
    }

    @Test
    @DisplayName("PAY-INT-002: Procesar pago con Stripe exitoso")
    void processPayment_stripe_success() {
        // Configurar invoice con TODOS los métodos necesarios
        Invoice invoice = mock(Invoice.class);
        when(invoice.getId()).thenReturn(INVOICE_ID);
        when(invoice.getTotal()).thenReturn(AMOUNT);
        when(invoice.getStatus()).thenReturn(InvoiceStatus.pending());
        when(invoice.getNumber()).thenReturn(InvoiceNumber.of("FAC-0001"));
        when(invoice.getCurrency()).thenReturn(CURRENCY_CODE);
        
        when(invoiceRepository.findById(INVOICE_ID)).thenReturn(Optional.of(invoice));
        when(paymentRepository.findByInvoiceId(INVOICE_ID)).thenReturn(List.of());

        when(gateways.get(PaymentMethod.STRIPE)).thenReturn(stripeGateway);
        when(stripeGateway.isAvailable()).thenReturn(true);
        
        PaymentGatewayResult successResult = mock(PaymentGatewayResult.class);
        when(successResult.success()).thenReturn(true);
        when(successResult.transactionRef()).thenReturn("tx_123");
        when(successResult.gatewayPaymentId()).thenReturn("pi_123");
        when(stripeGateway.processPayment(any())).thenReturn(successResult);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payer payer = Payer.patient("Juan Pérez");

        Payment result = service.processPayment(
                INVOICE_ID, AMOUNT, PaymentMethod.STRIPE, "email@test.com", "Juan", payer
        );

        assertThat(result).isNotNull();
        assertThat(result.isConfirmed()).isTrue();
        assertThat(result.getTransactionReference().value()).isEqualTo("tx_123");
        verify(stripeGateway).processPayment(any());
    }

    @Test
    @DisplayName("PAY-INT-003: Procesar pago con Stripe fallido")
    void processPayment_stripe_failure() {
        // Configurar invoice con TODOS los métodos necesarios
        Invoice invoice = mock(Invoice.class);
        when(invoice.getId()).thenReturn(INVOICE_ID);
        when(invoice.getTotal()).thenReturn(AMOUNT);
        when(invoice.getStatus()).thenReturn(InvoiceStatus.pending());
        when(invoice.getNumber()).thenReturn(InvoiceNumber.of("FAC-0001"));
        when(invoice.getCurrency()).thenReturn(CURRENCY_CODE);
        
        when(invoiceRepository.findById(INVOICE_ID)).thenReturn(Optional.of(invoice));
        when(paymentRepository.findByInvoiceId(INVOICE_ID)).thenReturn(List.of());

        when(gateways.get(PaymentMethod.STRIPE)).thenReturn(stripeGateway);
        when(stripeGateway.isAvailable()).thenReturn(true);
        
        PaymentGatewayResult failureResult = mock(PaymentGatewayResult.class);
        when(failureResult.success()).thenReturn(false);
        when(failureResult.errorMessage()).thenReturn("Declined");
        when(stripeGateway.processPayment(any())).thenReturn(failureResult);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payer payer = Payer.patient("Juan Pérez");

        Payment result = service.processPayment(
                INVOICE_ID, AMOUNT, PaymentMethod.STRIPE, "email@test.com", "Juan", payer
        );

        assertThat(result).isNotNull();
        assertThat(result.isFailed()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("Declined");
    }
}