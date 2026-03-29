
package com.example.ClinicaDefinitiva.application.shared.service;


import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.output.InvoiceRepository;
import com.example.ClinicaDefinitiva.domain.payment.event.PaymentConfirmedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handler de aplicación: escucha PaymentConfirmedEvent y marca la factura como pagada.
 *
 * Flujo completo de pago:
 *
 *   1. PaymentApplicationService.confirmPayment()
 *        → payment.confirm(transactionRef, gatewayId)    [domain]
 *        → paymentRepository.save(payment)               [infraestructura]
 *        → payment.pullDomainEvents() → eventPublisher.publish(PaymentConfirmedEvent)
 *
 *   2. PaymentConfirmedEventHandler.handle(event)        [este handler]
 *        → invoice.receivePayment(amount)                [domain]
 *        → invoiceRepository.save(invoice)               [infraestructura]
 *        → invoice.pullDomainEvents() → eventPublisher.publish(InvoiceFullyPaidEvent) [si aplica]
 *
 * Decisiones de diseño:
 * - @Async: el handler corre en un thread separado para no bloquear la confirmación del pago.
 *   Si el sistema usa Outbox Pattern, reemplaza @Async + @EventListener por un job que
 *   procese la tabla outbox; el método handle() queda idéntico.
 * - @Transactional: la carga y guardado de Invoice ocurren en una sola transacción.
 *   Si falla, la factura no queda en estado inconsistente; el evento puede reintentarse.
 * - No acoplamiento entre agregados: este handler es el único punto donde Payment e Invoice
 *   se coordinan. Ninguno de los dos conoce al otro directamente.
 */
@Component
public class PaymentConfirmedEventHandler {

    private static final Logger log = LoggerFactory.getLogger(PaymentConfirmedEventHandler.class);

    private final InvoiceRepository invoiceRepository;
    private final ApplicationEventPublisherPort eventPublisher;

    public PaymentConfirmedEventHandler(
            InvoiceRepository invoiceRepository,
            ApplicationEventPublisherPort eventPublisher) {
        this.invoiceRepository = invoiceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @EventListener
    @Transactional
    public void handle(PaymentConfirmedEvent event) {
        log.info("PaymentConfirmedEvent received — paymentId={}, invoiceId={}, amount={}",
                event.getPaymentId(), event.getInvoiceId(), event.getAmount());

        Invoice invoice = invoiceRepository.findById(event.getInvoiceId())
                .orElseThrow(() -> new IllegalStateException(
                        "Invoice not found for confirmed payment: invoiceId=" + event.getInvoiceId()));

        // Regla RN-INVOICE-016 y RN-INVOICE-017 encapsuladas en el agregado
        invoice.receivePayment(event.getAmount());

        invoiceRepository.save(invoice);

        // Publica InvoiceFullyPaidEvent si el agregado lo generó
        invoice.pullDomainEvents().forEach(domainEvent -> {
            log.info("Publishing domain event from Invoice: {}", domainEvent.getClass().getSimpleName());
            eventPublisher.publish(domainEvent);
        });
    }
}
