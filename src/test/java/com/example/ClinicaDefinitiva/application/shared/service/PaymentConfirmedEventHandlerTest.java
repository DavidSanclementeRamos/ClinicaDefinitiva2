package com.example.ClinicaDefinitiva.application.shared.service;

import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.output.InvoiceRepository;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.payment.event.PaymentConfirmedEvent;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Currency;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentConfirmedEventHandlerTest {

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private ApplicationEventPublisherPort eventPublisher;

    @InjectMocks
    private PaymentConfirmedEventHandler handler;

    private static final InvoiceId INVOICE_ID = InvoiceId.of(1L);
    private static final Price AMOUNT = Price.of(100_000, Currency.getInstance("COP"));

    @Test
    @DisplayName("Handler recibe evento y actualiza factura")
    void handle_success() {
        PaymentConfirmedEvent event = new PaymentConfirmedEvent(
                com.example.ClinicaDefinitiva.domain.payment.vo.PaymentId.of(1L),
                INVOICE_ID,
                AMOUNT,
                com.example.ClinicaDefinitiva.domain.payment.vo.PaymentMethod.CASH
        );

        Invoice invoice = mock(Invoice.class);
        when(invoiceRepository.findById(INVOICE_ID)).thenReturn(Optional.of(invoice));

        handler.handle(event);

        verify(invoice).receivePayment(AMOUNT);
        verify(invoiceRepository).save(invoice);
        verify(invoice).pullDomainEvents();
    }

    @Test
    @DisplayName("Handler lanza excepción si factura no encontrada")
    void handle_invoiceNotFound_throws() {
        PaymentConfirmedEvent event = new PaymentConfirmedEvent(
                com.example.ClinicaDefinitiva.domain.payment.vo.PaymentId.of(1L),
                INVOICE_ID,
                AMOUNT,
                com.example.ClinicaDefinitiva.domain.payment.vo.PaymentMethod.CASH
        );

        when(invoiceRepository.findById(INVOICE_ID)).thenReturn(Optional.empty());

        try {
            handler.handle(event);
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).contains("Invoice not found");
        }

        verify(invoiceRepository, never()).save(any());
    }
}
