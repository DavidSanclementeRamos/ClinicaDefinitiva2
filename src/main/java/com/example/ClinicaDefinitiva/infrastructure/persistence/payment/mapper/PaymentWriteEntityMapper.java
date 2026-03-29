package com.example.ClinicaDefinitiva.infrastructure.persistence.payment.mapper;

import com.example.ClinicaDefinitiva.domain.payment.model.Payment;
import com.example.ClinicaDefinitiva.infrastructure.persistence.payment.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentWriteEntityMapper {

    public PaymentEntity toEntity(Payment payment) {
        if (payment == null) return null;

        PaymentEntity entity = new PaymentEntity();
        if (payment.getId() != null) {
            entity.setId(payment.getId().value());
        }
        // La relación con Invoice se establece en el adaptador
        entity.setAmount(payment.getAmount().asBigDecimal());
        entity.setCurrency(payment.getAmount().getCurrency().getCurrencyCode());
        entity.setPaymentMethod(payment.getPaymentMethod().name());
        entity.setPayerType(payment.getPayer().getType().name());
        entity.setPayerReferenceId(payment.getPayer().getIdentifier());
        entity.setPayerName(payment.getPayer().getName());
        entity.setStatus(payment.getStatus().getCurrent().name());
        if (payment.getTransactionReference() != null) {
            entity.setTransactionReference(payment.getTransactionReference().value());
            entity.setPaymentGatewayId(payment.getTransactionReference().getGatewayPaymentId());
        }
        entity.setErrorMessage(payment.getErrorMessage());
        entity.setRefundedAmount(payment.getRefundedAmount().asBigDecimal());
        entity.setPaymentDate(payment.getPaymentDate());
        entity.setCreatedAt(payment.getCreatedAt());
        entity.setUpdatedAt(payment.getUpdatedAt());

        return entity;
    }
}
