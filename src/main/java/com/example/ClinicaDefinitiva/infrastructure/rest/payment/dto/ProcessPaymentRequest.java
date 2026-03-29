
package com.example.ClinicaDefinitiva.infrastructure.rest.payment.dto;

import com.example.ClinicaDefinitiva.application.payment.dto.PayerRequestDto;
import java.math.BigDecimal;

/**
 * DTO: ProcessPaymentRequest
 * 
 * Request para procesar un pago.
 */
public record ProcessPaymentRequest(
    Long invoiceId,
    BigDecimal amount,
    String currency,
    String paymentMethod,
    String customerEmail,
    String customerName,
    PayerRequestDto payer
) {
    
    public ProcessPaymentRequest {
        // Validaciones básicas
        if (invoiceId == null) {
            throw new IllegalArgumentException("Invoice ID is required");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException("Payment method is required");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (payer == null) {
            throw new IllegalArgumentException("Payer information is required");
        }
    }
}
 
