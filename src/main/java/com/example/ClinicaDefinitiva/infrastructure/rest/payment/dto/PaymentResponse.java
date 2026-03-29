package com.example.ClinicaDefinitiva.infrastructure.rest.payment.dto;

import com.example.ClinicaDefinitiva.application.payment.dto.PayerDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
    Long id,
    Long invoiceId,
    BigDecimal amount,
    String currency,
    String paymentMethod,
    PayerDto payer,
    String status,
    String transactionReference,
   // String gatewayPaymentId,
    String errorMessage,
    BigDecimal refundedAmount,
    LocalDateTime paymentDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
