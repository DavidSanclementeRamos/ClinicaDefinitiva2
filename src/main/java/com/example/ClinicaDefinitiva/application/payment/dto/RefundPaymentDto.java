package com.example.ClinicaDefinitiva.application.payment.dto;

import java.math.BigDecimal;

/**
 * DTO de aplicación para solicitar un reembolso.
 */
public record RefundPaymentDto(
        
    Long payment,    
    BigDecimal refundAmount,
    String currency,
    String reason
) {

}
