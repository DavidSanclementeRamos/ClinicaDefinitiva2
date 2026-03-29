package com.example.ClinicaDefinitiva.domain.payment.output;

import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentStatus;
import com.example.ClinicaDefinitiva.domain.vo.Price;

/**
 * Port: PaymentGateway
 * 
 * Abstracción de un gateway de pagos externo.
 * 
 * El dominio NO conoce implementaciones concretas (Stripe, Mercado Pago, etc.).
 * Solo define QUÉ puede hacer un gateway, NO CÓMO lo hace.
 * 
 * Implementaciones (Adapters):
 * - StripePaymentGateway: Procesa pagos con Stripe
 * - EPSPaymentGateway: Procesa pagos con EPS
 * - CashPaymentGateway: Registra pagos en efectivo (sin API)
 * 
 * Beneficios de esta abstracción:
 * - Cambiar de Stripe a Mercado Pago = cambiar 1 archivo (el adapter)
 * - Testing sin llamar APIs reales = mockear este interface
 * - Agregar nuevos gateways sin tocar el dominio
 */
public interface PaymentGateway {
    
    /**
     * Procesa un pago a través del gateway.
     * 
     * @param request Datos del pago a procesar
     * @return Resultado con referencia de transacción y estado
     */
    PaymentGatewayResult processPayment(PaymentRequest request);
    
    /**
     * Reembolsa un pago previamente procesado.
     * 
     * @param transactionRef Referencia de la transacción original
     * @param amount Monto a reembolsar
     * @return Resultado del reembolso
     */
    PaymentGatewayResult refundPayment(String transactionRef, Price amount);
    
    /**
     * Consulta el estado de un pago en el gateway.
     * 
     * @param transactionRef Referencia de la transacción
     * @return Estado actual del pago
     */
    PaymentStatus getPaymentStatus(String transactionRef);
    
    /**
     * Verifica si este gateway está disponible para procesar pagos.
     * 
     * @return true si el gateway está configurado y disponible
     */
    boolean isAvailable();
}
