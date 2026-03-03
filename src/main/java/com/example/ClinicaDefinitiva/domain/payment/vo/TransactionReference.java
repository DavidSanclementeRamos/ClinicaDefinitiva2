
package com.example.ClinicaDefinitiva.domain.payment.vo;




import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;


import com.example.ClinicaDefinitiva.domain.errors.catalog.payment.PaymentVoError;
import java.util.Objects;

import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

/**
 * Value Object: TransactionReference
 * 
 * Referencia de transacción externa:
 * - Para pagos con Stripe: Payment Intent ID (pi_xxxx)
 * - Para pagos con EPS: Código de autorización
 * - Para efectivo: Número de recibo interno
 * - Para transferencias: Número de comprobante
 */
public final class TransactionReference {
    
    private final String value;
    private final String gatewayPaymentId; // ID específico del gateway (opcional)
    
    private TransactionReference(String value, String gatewayPaymentId) {
        if (value == null || value.isBlank()) {
            throw new ValueObjectValidationException(
                PaymentVoError.ERR_PAYMENT_TRANSACTION_REF_NULL,
                VOContext.PAYMENT
            );
        }
        
        if (value.length() > 255) {
            throw new ValueObjectValidationException(
                PaymentVoError.ERR_PAYMENT_TRANSACTION_REF_TOO_LONG,
                VOContext.PAYMENT
            );
        }
        
        this.value = value.trim();
        this.gatewayPaymentId = gatewayPaymentId != null ? gatewayPaymentId.trim() : null;
    }
    
    public static TransactionReference of(String value) {
        return new TransactionReference(value, null);
    }
    
    public static TransactionReference of(String value, String gatewayPaymentId) {
        return new TransactionReference(value, gatewayPaymentId);
    }
    
    /**
     * Genera una referencia automática para pagos en efectivo.
     */
    public static TransactionReference generateCashReceipt() {
        String receipt = "CASH-" + System.currentTimeMillis();
        return new TransactionReference(receipt, null);
    }
    
    public String value() {
        return value;
    }
    
    public String getGatewayPaymentId() {
        return gatewayPaymentId;
    }
    
    public boolean hasGatewayId() {
        return gatewayPaymentId != null && !gatewayPaymentId.isBlank();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionReference)) return false;
        TransactionReference that = (TransactionReference) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
