package com.example.ClinicaDefinitiva.infrastructure.rest.payment.dto;



import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record RefundPaymentRequest(
    @NotNull Long paymentId,
    @NotNull @DecimalMin("0.01") BigDecimal refundAmount,
    @NotBlank String currency,
    @NotBlank String reason
) {}
