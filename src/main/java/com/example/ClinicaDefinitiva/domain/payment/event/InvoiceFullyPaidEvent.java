
package com.example.ClinicaDefinitiva.domain.payment.event;


import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentId;
import com.example.ClinicaDefinitiva.domain.vo.Price;

import java.time.Instant;
import java.util.UUID;


/**
 * Evento: InvoiceFullyPaidEvent
 */
class InvoiceFullyPaidEvent {
    
    private final String eventId;
    private final Instant occurredOn;
    private final InvoiceId invoiceId;
    
    public InvoiceFullyPaidEvent(InvoiceId invoiceId) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
        this.invoiceId = invoiceId;
    }
    
    public String getEventId() { return eventId; }
    public Instant getOccurredOn() { return occurredOn; }
    public InvoiceId getInvoiceId() { return invoiceId; }
}
