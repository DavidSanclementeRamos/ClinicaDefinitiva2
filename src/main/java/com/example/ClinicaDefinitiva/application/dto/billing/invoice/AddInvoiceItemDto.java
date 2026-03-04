package com.example.ClinicaDefinitiva.application.dto.billing.invoice;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public record AddInvoiceItemDto(
        Long item,
        Long serviceId,
        String serviceCode,
        String serviceDescription,
        Long rateId,
        BigDecimal unitPrice,
        Currency currency, 

        Integer quantity,
        LocalDateTime performedAt
) {}

