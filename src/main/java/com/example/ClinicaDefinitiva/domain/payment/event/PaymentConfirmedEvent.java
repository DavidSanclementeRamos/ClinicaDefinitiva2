
package com.example.ClinicaDefinitiva.domain.payment.event;


import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.PaymentMethod;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.InvoiceId;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentId;
import com.example.ClinicaDefinitiva.domain.vo.Price;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento: PaymentConfirmedEvent
 * 
 * Se publica cuando un pago es confirmado exitosamente.
 */
public class PaymentConfirmedEvent {
    
    private final String eventId;
    private final Instant occurredOn;
    private final PaymentId paymentId;
    private final InvoiceId invoiceId;
    private final Price amount;
    private final PaymentMethod paymentMethod;
    
    public PaymentConfirmedEvent(
            PaymentId paymentId,
            InvoiceId invoiceId,
            Price amount,
            PaymentMethod paymentMethod) {
        
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
        this.paymentId = paymentId;
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
    
    public String getEventId() { return eventId; }
    public Instant getOccurredOn() { return occurredOn; }
    public PaymentId getPaymentId() { return paymentId; }
    public InvoiceId getInvoiceId() { return invoiceId; }
    public Price getAmount() { return amount; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
}
