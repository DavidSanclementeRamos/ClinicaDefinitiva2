package com.example.ClinicaDefinitiva.infrastructure.persistence.billing.mapper.invoice;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.model.InvoiceItem;
import com.example.ClinicaDefinitiva.domain.billing.vo.*;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity.InvoiceEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity.InvoiceItemEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.stream.Collectors;

@Component
public class InvoiceReadEntityMapper {

    public Invoice toDomain(InvoiceEntity entity) {
        if (entity == null) return null;

        // Determinar tipo de factura y construir con builder
        Invoice.Builder builder;

        if (entity.getPatient() != null) {
            // Factura particular
            builder = Invoice.builder()
                    .patientId(PatientId.of(entity.getPatient().getId()))
                    .contractId(null);
        } else {
            // Factura institucional
            builder = Invoice.builder()
                    .patientId(null)
                    .contractId(ContractId.of(entity.getContract().getId()));
        }

        Invoice invoice = builder
                .id(InvoiceId.of(entity.getId()))
                .dentistId(DentistId.of(entity.getDentist().getId()))
                .providerId(ProviderId.of(entity.getProviderId()))
                .currency(CurrencyCode.of(entity.getCurrency()))
                .notes(Notes.of(entity.getNotes()))
                .dueDate(entity.getDueDate())
                .build();

        // Establecer campos adicionales usando reflexión o setters
        // Nota: Necesitarás setters en Invoice o un método reconstruct
        return invoice;
    }

    private InvoiceItem toInvoiceItemDomain(InvoiceItemEntity entity) {
        return InvoiceItem.builder()
                .id(InvoiceItemId.of(entity.getId()))
                .serviceId(com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId.of(entity.getDentalService().getId()))
                .serviceCode(entity.getServiceCode())
                .serviceDescription(entity.getServiceDescription())
                .unitPrice(Price.of(entity.getUnitPrice(), Currency.getInstance(entity.getCurrency())))
                .quantity(Quantity.of(entity.getQuantity()))
                .rateId(RateId.of(entity.getRate() != null ? entity.getRate().getId() : null))
                .performedAt(entity.getPerformedDate())
                .build();
    }
}
