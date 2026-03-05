
package com.example.ClinicaDefinitiva.domain.payment.vo;


import com.example.ClinicaDefinitiva.domain.errors.catalog.payment.PaymentError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.payment.PaymentVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;

import java.util.EnumSet;
import java.util.Set;

/**
 * Value Object: PaymentStatus
 * 
 * Estados de un pago:
 * - PENDING: Pendiente de confirmación
 * - CONFIRMED: Confirmado exitosamente
 * - FAILED: Falló el procesamiento
 * - REFUNDED: Reembolsado (total o parcial)
 * - CANCELLED: Cancelado antes de procesar
 */
public final class PaymentStatus {
    
    public enum Status {
        PENDING("Pendiente"),
        CONFIRMED("Confirmado"),
        FAILED("Fallido"),
        REFUNDED("Reembolsado"),
        CANCELLED("Cancelado");
        
        private final String displayName;
        
        Status(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    private final Status current;
    
    // Transiciones válidas
    private static final EnumSet<Status> VALID_TRANSITIONS_FROM_PENDING = 
        EnumSet.of(Status.CONFIRMED, Status.FAILED, Status.CANCELLED);
    
    private static final EnumSet<Status> VALID_TRANSITIONS_FROM_CONFIRMED = 
        EnumSet.of(Status.REFUNDED);
    
    private PaymentStatus(Status status) {
        this.current = status;
    }
    
    public static PaymentStatus pending() {
        return new PaymentStatus(Status.PENDING);
    }
    
    public static PaymentStatus confirmed() {
        return new PaymentStatus(Status.CONFIRMED);
    }
    
    public static PaymentStatus failed() {
        return new PaymentStatus(Status.FAILED);
    }
    
    public static PaymentStatus refunded() {
        return new PaymentStatus(Status.REFUNDED);
    }
    
    public static PaymentStatus cancelled() {
        return new PaymentStatus(Status.CANCELLED);
    }
    
    public static PaymentStatus of(Status status) {
        return new PaymentStatus(status);
    }
    
    /**
     * Transiciona a un nuevo estado validando reglas de negocio.
     */
    public PaymentStatus transitionTo(Status next) {
        if (!canTransitionTo(next)) {
            throw new BusinessRuleViolationException(
                PaymentVoError.ERR_PAYMENT_INVALID_TRANSITION,
                EntityContext.PAYMENT
            );
        }
        return new PaymentStatus(next);
    }
    
    public boolean canTransitionTo(Status next) {
        return getValidTransitions().contains(next);
    }
    
    private Set<Status> getValidTransitions() {
        return switch (current) {
            case PENDING -> VALID_TRANSITIONS_FROM_PENDING;
            case CONFIRMED -> VALID_TRANSITIONS_FROM_CONFIRMED;
            case FAILED, REFUNDED, CANCELLED -> EnumSet.noneOf(Status.class);
        };
    }
    
    // Consultas semánticas
    public boolean isPending() { return current == Status.PENDING; }
    public boolean isConfirmed() { return current == Status.CONFIRMED; }
    public boolean isFailed() { return current == Status.FAILED; }
    public boolean isRefunded() { return current == Status.REFUNDED; }
    public boolean isCancelled() { return current == Status.CANCELLED; }
    
    public boolean isSuccessful() { return isConfirmed(); }
    public boolean isFinal() { return isFailed() || isRefunded() || isCancelled(); }
    
    public Status getCurrent() {
        return current;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentStatus)) return false;
        PaymentStatus that = (PaymentStatus) o;
        return current == that.current;
    }
    
    @Override
    public int hashCode() {
        return current.hashCode();
    }
    
    @Override
    public String toString() {
        return current.name();
    }
}
