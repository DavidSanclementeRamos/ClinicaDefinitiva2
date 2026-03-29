package com.example.ClinicaDefinitiva.application.payment.mapper;

import com.example.ClinicaDefinitiva.application.payment.dto.PayerDto;
import com.example.ClinicaDefinitiva.application.payment.dto.PaymentDto;
import com.example.ClinicaDefinitiva.domain.payment.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentReadMapper {

    public PaymentDto toDto(Payment payment) {
        return new PaymentDto(
                payment.getId().value(),
                payment.getInvoiceId().getValue(),
                payment.getAmount().asBigDecimal(),
                payment.getAmount().getCurrency().getCurrencyCode(),
                payment.getPaymentMethod().getDisplayName(),
                payment.getStatus().toString(),
               new PayerDto( payment.getPayer().getType().getDisplayName(),payment.getPayer().getIdentifier(),payment.getPayer().getName()),
                payment.getTransactionReference().value(),
                payment.getErrorMessage(),
                payment.getRefundedAmount().asBigDecimal(),
                payment.getPaymentDate(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
                
        );
    }
}
