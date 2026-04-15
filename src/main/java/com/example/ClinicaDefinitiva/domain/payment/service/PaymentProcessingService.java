
package com.example.ClinicaDefinitiva.domain.payment.service;


import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.output.InvoiceRepository;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.payment.PaymentError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.payment.event.InvoiceFullyPaidEvent;
import com.example.ClinicaDefinitiva.domain.payment.event.PaymentConfirmedEvent;
import com.example.ClinicaDefinitiva.domain.payment.event.PaymentFailedEvent;
import com.example.ClinicaDefinitiva.domain.payment.model.Payment;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentGateway;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentGatewayResult;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentRepository;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentRequest;
import com.example.ClinicaDefinitiva.domain.payment.vo.Payer;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentMethod;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Domain Service: PaymentProcessingService
 * 
 * Responsabilidades:
 * - Procesar pagos usando gateways según método de pago
 * - Validar reglas de negocio (factura válida, monto correcto)
 * - Coordinar Payment + Invoice
 * - Publicar eventos de dominio
 */
@Service
public class PaymentProcessingService {
    
    private final Map<PaymentMethod, PaymentGateway> gateways;
    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    public PaymentProcessingService(
            Map<PaymentMethod, PaymentGateway> gateways,
            PaymentRepository paymentRepository,
            InvoiceRepository invoiceRepository,
            ApplicationEventPublisher eventPublisher) {
        
        this.gateways = gateways;
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.eventPublisher = eventPublisher;
    }
    
    /**
     * Procesa un pago contra una factura.
     * 
     * @param invoiceId ID de la factura
     * @param amount Monto a pagar
     * @param paymentMethod Método de pago (CASH, STRIPE, etc.)
     * @param customerEmail Email del cliente
     * @param customerName Nombre del cliente
     * @param payer Quién realiza el pago
     * @return Payment creado y procesado
     */
    @Transactional
    public Payment processPayment(
            InvoiceId invoiceId,
            Price amount,
            PaymentMethod paymentMethod,
            String customerEmail,
            String customerName,
            Payer payer) {
        
        // 1. Validar que la factura existe y puede recibir pagos
        Invoice invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_INVOICE_NOT_FOUND,
                EntityContext.PAYMENT
            ));
        
        validateInvoiceCanReceivePayment(invoice);
        
        // 2. Validar que el monto no excede el pendiente
        Price remainingAmount = calculateRemainingAmount(invoice);
        if (amount.isGreaterThan(remainingAmount)) {
            throw new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_EXCEEDS_INVOICE,
                EntityContext.PAYMENT
            );
        }
        
        // 3. Crear Payment en estado PENDING
        Payment payment = Payment.createPending(
            invoiceId,
            amount,
            paymentMethod,
            payer
        );
        
        paymentRepository.save(payment);
        
        // 4. Procesar según método de pago
        if (paymentMethod.isImmediate()) {
            // Efectivo: confirmar inmediatamente
            payment.confirmCashPayment();
            updateInvoiceIfFullyPaid(invoice, payment);
            
        } else if (paymentMethod.requiresGateway()) {
            // Gateway externo (Stripe, EPS)
            processWithGateway(payment, invoice, customerEmail, customerName);
        }
        // Otros métodos (transferencia) quedan PENDING para confirmar manualmente
        
        // 5. Persistir cambios
        paymentRepository.save(payment);
        invoiceRepository.save(invoice);
        
        // 6. Publicar eventos
        publishPaymentEvents(payment, invoice);
        
        return payment;
    }
    
    /**
     * Procesa pago con gateway externo.
     */
    private void processWithGateway(
            Payment payment,
            Invoice invoice,
            String customerEmail,
            String customerName) {
        
        // Seleccionar gateway según método de pago
        PaymentGateway gateway = gateways.get(payment.getPaymentMethod());
        
        if (gateway == null || !gateway.isAvailable()) {
            payment.fail("Gateway not available for: " + payment.getPaymentMethod());
            return;
        }
        
        // Construir request
        PaymentRequest request = new PaymentRequest(
            payment.getAmount(),
            payment.getAmount().getCurrency().getCurrencyCode(),
            customerEmail,
            customerName,
            "Payment for invoice " + invoice.getNumber().getValue(),
            invoice.getNumber().getValue()
        );
        
        // Llamar gateway
        PaymentGatewayResult result = gateway.processPayment(request);
        
        if (result.success()) {
            payment.confirm(result.transactionRef(), result.gatewayPaymentId());
            updateInvoiceIfFullyPaid(invoice, payment);
        } else {
            payment.fail(result.errorMessage());
        }
    }
    
    /**
     * Valida que la factura puede recibir pagos.
     */
    private void validateInvoiceCanReceivePayment(Invoice invoice) {
        if (invoice.getStatus().isCancelled()) {
            throw new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_INVOICE_CANCELLED,
                EntityContext.PAYMENT
            );
        }
        
        if (invoice.getStatus().isPaid()) {
            throw new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_INVOICE_ALREADY_PAID,
                EntityContext.PAYMENT
            );
        }
    }
    
    /**
     * Calcula el monto pendiente de la factura.
     */
    private Price calculateRemainingAmount(Invoice invoice) {
        // Sumar todos los pagos confirmados
        Price totalPaid = paymentRepository.findByInvoiceId(invoice.getId())
            .stream()
            .filter(Payment::isConfirmed)
            .map(Payment::getAmount)
            .reduce(
                Price.zero(invoice.getCurrency().toJavaCurrency()),
                Price::add
            );
        
        return invoice.getTotal().subtract(totalPaid);
    }
    
    /**
     * Actualiza el estado de la factura si se pagó completamente.
     */
    private void updateInvoiceIfFullyPaid(Invoice invoice, Payment payment) {
        Price remaining = calculateRemainingAmount(invoice);
        
        if (remaining.isNegativeOrZero()) {
            // TODO: Agregar método markAsPaid() en Invoice si no existe
            // invoice.markAsPaid();
        }
    }
    
    /**
     * Publica eventos de dominio según resultado del pago.
     */
    private void publishPaymentEvents(Payment payment, Invoice invoice) {
        if (payment.isConfirmed()) {
            eventPublisher.publishEvent(new PaymentConfirmedEvent(
                payment.getId(),            
                invoice.getId(),
                payment.getAmount(),
                payment.getPaymentMethod()
            ));
            
            Price remaining = calculateRemainingAmount(invoice);
            if (remaining.isNegativeOrZero()) {
                eventPublisher.publishEvent(new InvoiceFullyPaidEvent(
                    invoice.getId()
                ));
            }
            
        } else if (payment.isFailed()) {
            eventPublisher.publishEvent(new PaymentFailedEvent(
                payment.getId(),
                invoice.getId(),
                payment.getErrorMessage()
            ));
        }
    }
}
