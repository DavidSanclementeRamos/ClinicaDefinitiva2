package com.example.ClinicaDefinitiva.infrastructure.persistence.payment.mapper;

import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.payment.model.Payment;
import com.example.ClinicaDefinitiva.domain.payment.vo.*;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.infrastructure.persistence.payment.entity.PaymentEntity;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class PaymentReadEntityMapper {

    public Payment toDomain(PaymentEntity entity) {
        if (entity == null) return null;

        // Reconstruir el pago usando el builder
        Payment payment = Payment.builder()
            .id(PaymentId.of(entity.getId()))
            .invoiceId(InvoiceId.of(entity.getInvoice().getId()))
            .amount(Price.of(entity.getAmount(), Currency.getInstance(entity.getCurrency())))
            .paymentMethod(PaymentMethod.fromString(entity.getPaymentMethod()))
            .payer(mapPayer(entity))
            .paymentDate(entity.getPaymentDate())
            .build();

        // Ajustar el estado y demás campos que no están en el builder
        // (necesitarás un método reconstruct o setters en Payment)
        // Por simplicidad, podemos usar reflection o modificar Payment para permitir reconstrucción.
        // Asumimos que Payment tiene un método reconstruct o que podemos usar setters públicos.
        // Como es un agregado, se recomienda un método estático reconstruct.
        return reconstructFromEntity(entity, payment);
    }

    private Payer mapPayer(PaymentEntity entity) {
        Payer.PayerType type = Payer.PayerType.valueOf(entity.getPayerType());
        return Payer.of(type, entity.getPayerReferenceId(), entity.getPayerName());
    }

    private Payment reconstructFromEntity(PaymentEntity entity, Payment payment) {
        // Usar reflexión o agregar un método estático en Payment
        // Por ahora, devolvemos el payment básico; los campos adicionales se settean en el servicio de aplicación.
        // Es mejor tener un método reconstruct en Payment.
        return Payment.reconstruct(
            payment.getId(),
            payment.getInvoiceId(),
            payment.getAmount(),
            payment.getPaymentMethod(),
            payment.getPayer(),
            PaymentStatus.of(PaymentStatus.Status.valueOf(entity.getStatus())),
            entity.getTransactionReference() != null ? TransactionReference.of(entity.getTransactionReference(), entity.getPaymentGatewayId()) : null,
            entity.getErrorMessage(),
            Price.of(entity.getRefundedAmount(), Currency.getInstance(entity.getCurrency())),
            payment.getPaymentDate(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getRefundReason()
                
        );
    }
}
