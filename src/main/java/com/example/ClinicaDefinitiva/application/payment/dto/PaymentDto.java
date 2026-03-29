
package com.example.ClinicaDefinitiva.application.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO: PaymentDto
 * 
 * Representa un pago para respuestas de API.
 */
public record PaymentDto(
    Long id,
    Long invoiceId,
    BigDecimal amount,
    String currency,
    String paymentMethod,
    String status,
    PayerDto payer,
    String transactionRef,
    //String gatewayPaymentId,
    String errorMessage,
    BigDecimal refundedAmount,
    LocalDateTime paymentDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    
   
}
