package com.example.ClinicaDefinitiva.infrastructure.rest.payment.dto;

import com.example.ClinicaDefinitiva.application.payment.dto.PayerRequestDto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreatePaymentRequest(
    @NotNull Long invoiceId,
    @NotNull @DecimalMin("0.01") BigDecimal amount,
    @NotBlank String currency,
    @NotBlank String paymentMethod,
    String customerEmail,
    @NotBlank String customerName,
    @NotNull PayerRequestDto payer
) {}
