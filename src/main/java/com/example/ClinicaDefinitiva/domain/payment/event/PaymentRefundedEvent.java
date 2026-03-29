
package com.example.ClinicaDefinitiva.domain.payment.event;

import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import java.time.Instant;
import java.util.UUID;

/**
 * Evento: PaymentRefundedEvent
 */
public class PaymentRefundedEvent {
    
    private final String eventId;
    private final Instant occurredOn;
    private final PaymentId paymentId;
    private final InvoiceId invoiceId;
    private final Price refundedAmount;
    
    public PaymentRefundedEvent(PaymentId paymentId, InvoiceId invoiceId, Price refundedAmount) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
        this.paymentId = paymentId;
        this.invoiceId = invoiceId;
        this.refundedAmount = refundedAmount;
    }
    
    public String getEventId() { return eventId; }
    public Instant getOccurredOn() { return occurredOn; }
    public PaymentId getPaymentId() { return paymentId; }
    public InvoiceId getInvoiceId() { return invoiceId; }
    public Price getRefundedAmount() { return refundedAmount; }
}
