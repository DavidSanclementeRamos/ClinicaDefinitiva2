package com.example.ClinicaDefinitiva.domain.payment.model;

import com.example.ClinicaDefinitiva.domain.payment.vo.*;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.payment.PaymentError;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agregado: Payment (Pago)
 * 
 * Representa un pago realizado contra una factura.
 * 
 * Reglas de negocio:
 * - RN-PAYMENT-001: El monto debe ser mayor a cero
 * - RN-PAYMENT-002: Solo pagos PENDING pueden confirmarse
 * - RN-PAYMENT-003: Solo pagos CONFIRMED pueden reembolsarse
 * - RN-PAYMENT-004: Pagos en efectivo se confirman inmediatamente
 * - RN-PAYMENT-005: Pagos con gateway requieren transactionRef
 * - RN-PAYMENT-006: El reembolso no puede exceder el monto original
 * - RN-PAYMENT-007: Pagos institucionales requieren identificador del pagador
 * 
 * Eventos de dominio publicados:
 * - PaymentConfirmedEvent → escuchado por PaymentConfirmedEventHandler para marcar Invoice como pagada
 * - PaymentFailedEvent    → para notificaciones / reintentos
 * - PaymentRefundedEvent  → para trazabilidad contable
 */
public final class Payment {
    
    private final PaymentId id;
    private final InvoiceId invoiceId;
    private final Price amount;
    private final PaymentMethod paymentMethod;
    private final Payer payer;
    
    private PaymentStatus status;
    private TransactionReference transactionReference;
    private String errorMessage;
    private Price refundedAmount; // Monto reembolsado (para reembolsos parciales)
    
    private final LocalDateTime paymentDate;
    private  LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String refundReason;
    
    
    /**
     * Eventos pendientes de publicación.
     * El Application Service los extrae con pullDomainEvents() tras persistir el agregado.
     */
    private final List<Object> pendingEvents = new ArrayList<>();

    
    private Payment(Builder builder) {
        this.id = builder.id;
        this.invoiceId = builder.invoiceId;
        this.amount = builder.amount;
        this.paymentMethod = builder.paymentMethod;
        this.payer = builder.payer;
        this.paymentDate = builder.paymentDate;
        this.status = PaymentStatus.pending();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.refundedAmount = Price.zero(amount.getCurrency());
        
        validateBusinessRules();
    }
    
    /**
     * Crea un pago en estado PENDING (para procesar con gateway).
     */
    public static Payment createPending(
            InvoiceId invoiceId,
            Price amount,
            PaymentMethod paymentMethod,
            Payer payer) {
        
        return new Builder()
                .invoiceId(invoiceId)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .payer(payer)
                .paymentDate(LocalDateTime.now())
                .build();
    }
    
    /**
     * Confirma el pago tras procesamiento exitoso con gateway.
     * 
     * @param transactionRef Referencia de transacción del gateway
     * @param gatewayPaymentId ID del gateway (ej. Stripe Payment Intent ID)
     */
    public void confirm(String transactionRef, String gatewayPaymentId) {
        ensurePending();
        
        if (transactionRef == null || transactionRef.isBlank()) {
            throw new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_TRANSACTION_REF_REQUIRED,
                EntityContext.PAYMENT
            );
        }
        
        this.status = status.transitionTo(PaymentStatus.Status.CONFIRMED);
        this.transactionReference = TransactionReference.of(transactionRef, gatewayPaymentId);
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Confirma pago en efectivo (sin gateway).
     */
    public void confirmCashPayment() {
        ensurePending();
        
        if (paymentMethod != PaymentMethod.CASH) {
            throw new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_NOT_CASH,
                EntityContext.PAYMENT
            );
        }
        
        this.status = status.transitionTo(PaymentStatus.Status.CONFIRMED);
        this.transactionReference = TransactionReference.generateCashReceipt();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Marca el pago como fallido.
     * 
     * @param errorMessage Razón del fallo
     */
    public void fail(String errorMessage) {
        ensurePending();
        
        this.status = status.transitionTo(PaymentStatus.Status.FAILED);
        this.errorMessage = errorMessage;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Cancela un pago pendiente (antes de procesarlo).
     */
    public void cancel(String reason) {
        ensurePending();
        
        if (reason == null || reason.isBlank()) {
            throw new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_CANCELLATION_REQUIRES_REASON,
                EntityContext.PAYMENT
            );
        }
        
        this.status = status.transitionTo(PaymentStatus.Status.CANCELLED);
        this.errorMessage = reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Reembolsa el pago (total o parcial).
     * 
     * @param refundAmount Monto a reembolsar
     */
    public void refund(Price refundAmount, String reason) {
        ensureConfirmed();
        
        if (refundAmount.isNegativeOrZero()) {
            throw new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_REFUND_INVALID_AMOUNT,
                EntityContext.PAYMENT
            );
        }
        
        // Validar que no exceda el monto pagado
        Price totalRefunded = this.refundedAmount.add(refundAmount);
        if (totalRefunded.isGreaterThan(this.amount)) {
            throw new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_REFUND_EXCEEDS_AMOUNT,
                EntityContext.PAYMENT
            );
        }
        
        this.refundedAmount = totalRefunded;
        
        // Si se reembolsa todo, cambiar estado
        if (this.refundedAmount.equals(this.amount)) {
            this.status = status.transitionTo(PaymentStatus.Status.REFUNDED);
        }
        
        this.updatedAt = LocalDateTime.now();
    }
    
    
    
    /**
     * Extrae y limpia los eventos pendientes.
     * El Application Service llama a este método tras persistir el agregado,
     * y publica cada evento al EventPublisher de la infraestructura.
     *
     * Ejemplo en el Application Service:
     * <pre>
     *   paymentRepository.save(payment);
     *   payment.pullDomainEvents().forEach(eventPublisher::publish);
     * </pre>
     */
    public List<Object> pullDomainEvents() {
        List<Object> events = Collections.unmodifiableList(new ArrayList<>(pendingEvents));
        pendingEvents.clear();
        return events;
    }
    
    
    /**
     * Calcula el monto pendiente de reembolso.
     */
    public Price getRemainingAmount() {
        return amount.subtract(refundedAmount);
    }
    
    /**
     * Verifica si el pago fue reembolsado completamente.
     */
    public boolean isFullyRefunded() {
        return refundedAmount.equals(amount);
    }
    
    /**
     * Verifica si el pago fue reembolsado parcialmente.
     */
    public boolean isPartiallyRefunded() {
        return refundedAmount.isGreaterThan(Price.zero(amount.getCurrency())) 
            && !isFullyRefunded();
    }
    
    // Validaciones privadas
    
    private void validateBusinessRules() {
        if (amount.isNegativeOrZero()) {
            throw new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_AMOUNT_INVALID,
                EntityContext.PAYMENT
            );
        }
        
        if (paymentMethod.requiresContract() && payer.isPatient()) {
            throw new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_INSTITUTIONAL_REQUIRES_PAYER,
                EntityContext.PAYMENT
            );
        }
    }
    
    private void ensurePending() {
        if (!status.isPending()) {
            throw new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_NOT_PENDING,
                EntityContext.PAYMENT
            );
        }
    }
    
    private void ensureConfirmed() {
        if (!status.isConfirmed()) {
            throw new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_NOT_CONFIRMED,
                EntityContext.PAYMENT
            );
        }
    }
    
    // Consultas semánticas
    
    public boolean isPending() { return status.isPending(); }
    public boolean isConfirmed() { return status.isConfirmed(); }
    public boolean isFailed() { return status.isFailed(); }
    public boolean isCancelled() { return status.isCancelled(); }
    public boolean isRefunded() { return status.isRefunded(); }
    public boolean isSuccessful() { return status.isSuccessful(); }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }
    
    
    public PaymentId getId() { return id; }
    public InvoiceId getInvoiceId() { return invoiceId; }
    public Price getAmount() { return amount; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public Payer getPayer() { return payer; }
    public PaymentStatus getStatus() { return status; }
    public TransactionReference getTransactionReference() { return transactionReference; }
    public String getErrorMessage() { return errorMessage; }
    public Price getRefundedAmount() { return refundedAmount; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    // Builder
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private PaymentId id;
        private InvoiceId invoiceId;
        private Price amount;
        private PaymentMethod paymentMethod;
        private Payer payer;
        private LocalDateTime paymentDate;
        
        public Builder id(PaymentId id) { 
            this.id = id; 
            return this; 
        }
        
        public Builder invoiceId(InvoiceId invoiceId) { 
            this.invoiceId = invoiceId; 
            return this; 
        }
        
        public Builder amount(Price amount) { 
            this.amount = amount; 
            return this; 
        }
        
        public Builder paymentMethod(PaymentMethod paymentMethod) { 
            this.paymentMethod = paymentMethod; 
            return this; 
        }
        
        public Builder payer(Payer payer) { 
            this.payer = payer; 
            return this; 
        }
        
        public Builder paymentDate(LocalDateTime paymentDate) { 
            this.paymentDate = paymentDate; 
            return this; 
        }
        
        public Payment build() { 
            return new Payment(this); 
        }
    }
    
    //Para persistencia 
public static Payment reconstruct(
        PaymentId id,
        InvoiceId invoiceId,
        Price amount,
        PaymentMethod paymentMethod,
        Payer payer,
        PaymentStatus status,
        TransactionReference transactionReference,
        String errorMessage,
        Price refundedAmount,
        LocalDateTime paymentDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String refundReason
) {

    Payment payment = Payment.builder()
        .id(id)
        .invoiceId(invoiceId)
        .amount(amount)
        .paymentMethod(paymentMethod)
        .payer(payer)
        .paymentDate(paymentDate)
        .build();

    
    payment.status = status;
    payment.transactionReference = transactionReference;
    payment.errorMessage = errorMessage;
    payment.refundedAmount = refundedAmount;
    payment.createdAt = createdAt;
    payment.updatedAt = updatedAt;
    payment.refundReason = refundReason;
    
    
    return payment;
}
}
