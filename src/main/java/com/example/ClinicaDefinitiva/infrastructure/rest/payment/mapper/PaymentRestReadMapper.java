package com.example.ClinicaDefinitiva.infrastructure.rest.payment.mapper;

import com.example.ClinicaDefinitiva.application.payment.dto.PaymentDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.payment.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentRestReadMapper {

    public PaymentResponse toResponse(PaymentDto dto) {
        if (dto == null) return null;
        return new PaymentResponse(
            dto.id(),
            dto.invoiceId(),
            dto.amount(),
            dto.currency(),
            dto.paymentMethod(),
            dto.payer(),
            dto.status(),
            dto.transactionRef(),
            //dto.gatewayPaymentId(),
            dto.errorMessage(),
            dto.refundedAmount(),
            dto.paymentDate(),
            dto.createdAt(),
            dto.updatedAt()
        );
    }
}