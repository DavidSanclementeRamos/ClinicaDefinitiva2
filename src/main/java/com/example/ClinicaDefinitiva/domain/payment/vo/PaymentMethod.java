package com.example.ClinicaDefinitiva.domain.payment.vo;




import com.example.ClinicaDefinitiva.domain.errors.catalog.payment.PaymentVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

/**
 * Value Object: PaymentMethod
 * 
 * Métodos de pago soportados:
 * - CASH: Efectivo
 * - CARD: Tarjeta (débito/crédito)
 * - STRIPE: Pago online con Stripe
 * - EPS: Entidad Promotora de Salud (Colombia)
 * - CONTRACT: Pago mediante contrato/convenio institucional
 * - BANK_TRANSFER: Transferencia bancaria
 */
public enum PaymentMethod {
    
    CASH("Efectivo"),
    CARD("Tarjeta"),
    STRIPE("Stripe"),
    EPS("EPS"),
    CONTRACT("Convenio Institucional"),
    BANK_TRANSFER("Transferencia Bancaria");
    
    private final String displayName;
    
    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Determina si este método de pago requiere un gateway externo.
     * 
     * @return true si requiere integración con API externa
     */
    public boolean requiresGateway() {
        return this == STRIPE || this == EPS;
    }
    
    /**
     * Determina si es un pago institucional que requiere contrato.
     * 
     * @return true si requiere ContractId
     */
    public boolean requiresContract() {
        return this == EPS || this == CONTRACT;
    }
    
    /**
     * Determina si el pago es inmediato (sin procesamiento externo).
     * 
     * @return true si se confirma inmediatamente
     */
    public boolean isImmediate() {
        return this == CASH;
    }
    
    public static PaymentMethod fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new ValueObjectValidationException(
                PaymentVoError.ERR_PAYMENT_METHOD_NULL,
                VOContext.PAYMENT
            );
        }
        
        try {
            return PaymentMethod.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValueObjectValidationException(
                PaymentVoError.ERR_PAYMENT_METHOD_INVALID,
                VOContext.PAYMENT
            );
        }
    }
}
