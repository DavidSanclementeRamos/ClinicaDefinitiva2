
package com.example.ClinicaDefinitiva.domain.payment.event;

import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentId;
import java.time.Instant;
import java.util.UUID;

/**
 * Evento: PaymentFailedEvent
 */
public class PaymentFailedEvent {
    
    private final String eventId;
    private final Instant occurredOn;
    private final PaymentId paymentId;
    private final InvoiceId invoiceId;
    private final String errorMessage;
    
    public PaymentFailedEvent(PaymentId paymentId, InvoiceId invoiceId, String errorMessage) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
        this.paymentId = paymentId;
        this.invoiceId = invoiceId;
        this.errorMessage = errorMessage;
    }
    
    public String getEventId() { return eventId; }
    public Instant getOccurredOn() { return occurredOn; }
    public PaymentId getPaymentId() { return paymentId; }
    public InvoiceId getInvoiceId() { return invoiceId; }
    public String getErrorMessage() { return errorMessage; }
}
