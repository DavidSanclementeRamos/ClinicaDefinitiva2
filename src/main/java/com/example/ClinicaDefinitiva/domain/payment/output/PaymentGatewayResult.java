package com.example.ClinicaDefinitiva.domain.payment.output;

import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentStatus;

/**
 * DTO: PaymentGatewayResult
 * 
 * Resultado de una operación con el gateway de pagos.
 * 
 * Contiene:
 * - success: Si la operación fue exitosa
 * - transactionRef: Referencia de transacción del gateway
 * - gatewayPaymentId: ID interno del gateway (ej. Stripe Payment Intent ID)
 * - status: Estado del pago
 * - errorMessage: Mensaje de error si falló
 */
public record PaymentGatewayResult(
    boolean success,
    String transactionRef,
    String gatewayPaymentId,
    PaymentStatus status,
    String errorMessage
) {
    
    /**
     * Crea un resultado exitoso.
     * 
     * @param transactionRef Referencia de la transacción
     * @param gatewayPaymentId ID del gateway (ej. pi_xxxxx en Stripe)
     */
    public static PaymentGatewayResult success(
            String transactionRef, 
            String gatewayPaymentId) {
        
        return new PaymentGatewayResult(
            true,
            transactionRef,
            gatewayPaymentId,
            PaymentStatus.confirmed(),
            null
        );
    }
    
    /**
     * Crea un resultado pendiente (procesando).
     * 
     * @param transactionRef Referencia de la transacción
     * @param gatewayPaymentId ID del gateway
     */
    public static PaymentGatewayResult pending(
            String transactionRef, 
            String gatewayPaymentId) {
        
        return new PaymentGatewayResult(
            false,
            transactionRef,
            gatewayPaymentId,
            PaymentStatus.pending(),
            null
        );
    }
    
    /**
     * Crea un resultado de fallo.
     * 
     * @param errorMessage Razón del fallo
     */
    public static PaymentGatewayResult failure(String errorMessage) {
        return new PaymentGatewayResult(
            false,
            null,
            null,
            PaymentStatus.failed(),
            errorMessage
        );
    }
    
    /**
     * Verifica si el pago fue exitoso.
     */
    public boolean isSuccess() {
        return success;
    }
    
    /**
     * Verifica si el pago falló.
     */
    public boolean isFailed() {
        return !success && status.isFailed();
    }
    
    /**
     * Verifica si el pago está pendiente.
     */
    public boolean isPending() {
        return !success && status.isPending();
    }
}
