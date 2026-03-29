package com.example.ClinicaDefinitiva.infrastructure.rest.payment.mapper;

import com.example.ClinicaDefinitiva.application.payment.dto.CreatePaymentDto;
import com.example.ClinicaDefinitiva.application.payment.dto.RefundPaymentDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.payment.dto.CreatePaymentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.payment.dto.RefundPaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentRestWriteMapper {

    public CreatePaymentDto toCreateDto(CreatePaymentRequest request) {
        if (request == null) return null;
        return new CreatePaymentDto(
            request.invoiceId(),
            request.amount(),
            request.currency(),
            request.paymentMethod(),
            request.customerEmail(),
            request.customerName(),
            request.payer()
        );
    }

    public RefundPaymentDto toRefundDto(RefundPaymentRequest request) {
        if (request == null) return null;
        return new RefundPaymentDto(
            request.paymentId(),
            request.refundAmount(),
            request.currency(),
            request.reason()
        );
    }
}