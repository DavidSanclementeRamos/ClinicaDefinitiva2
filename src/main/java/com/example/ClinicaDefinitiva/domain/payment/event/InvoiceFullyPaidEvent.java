
package com.example.ClinicaDefinitiva.domain.payment.event;


import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;

import java.time.Instant;
import java.util.UUID;


/**
 * Evento de dominio: InvoiceFullyPaidEvent
 *
 * Publicado por Invoice.receivePayment() cuando los pagos acumulados
 * cubren el total de la factura y el estado transiciona a PAID.
 *
 * Consumidores potenciales:
 * - Módulo de notificaciones (email/SMS al paciente)
 * - Módulo contable (registro en libro mayor)
 * - Reportes de facturación
 */
public final class InvoiceFullyPaidEvent {

    private final String eventId;
    private final Instant occurredOn;
    private final InvoiceId invoiceId;

    public InvoiceFullyPaidEvent(InvoiceId invoiceId) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
        this.invoiceId = invoiceId;
    }

    public String getEventId()      { return eventId; }
    public Instant getOccurredOn()  { return occurredOn; }
    public InvoiceId getInvoiceId() { return invoiceId; }
}
