package com.example.ClinicaDefinitiva.application.payment.dto;

import java.math.BigDecimal;

/**
 * DTO de aplicación para solicitar la creación de un pago.
 */
public record CreatePaymentDto(
    Long invoiceId,
    BigDecimal amount,
    String currency,
    String paymentMethod,
    String customerEmail,
    String customerName,
    PayerRequestDto payer
) {
    
}
