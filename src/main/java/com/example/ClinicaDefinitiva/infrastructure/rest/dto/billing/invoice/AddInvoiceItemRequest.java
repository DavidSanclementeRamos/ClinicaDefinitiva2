package com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public record AddInvoiceItemRequest(
       
        Long item,
        Long serviceId,
        String serviceCode,
        String serviceDescription,
        Long rateId,
        BigDecimal unitPrice,
        Currency currency, 

        Integer quantity,
        LocalDateTime performedAt) {
}
