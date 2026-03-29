package com.example.ClinicaDefinitiva.infrastructure.persistence.billing.mapper.invoice;

import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.model.InvoiceItem;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity.InvoiceEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity.InvoiceItemEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class InvoiceWriteEntityMapper {

    public InvoiceEntity toEntity(Invoice invoice) {
        if (invoice == null) return null;

        InvoiceEntity entity = new InvoiceEntity();

       // if (invoice.getId() != null && invoice.getId().getValue() != null) {
          //  entity.setId(invoice.getId().getValue());
        //}

        entity.setInvoiceNumber(invoice.getNumber() != null ? invoice.getNumber().getValue() : null);
        entity.setStatus(invoice.getStatus().getValue().name());
        entity.setCurrency(invoice.getCurrency().getCode());
        entity.setSubtotal(invoice.getSubtotal().asBigDecimal());
        entity.setTax(invoice.getTax().asBigDecimal());
        entity.setTotal(invoice.getTotal().asBigDecimal());
        entity.setTotalPaid(invoice.getTotalPaid().asBigDecimal());
        entity.setDueDate(invoice.getDueDate());
        entity.setUpdatedAt(invoice.getUpdatedAt());
        entity.setNotes(invoice.getNotes() != null ? invoice.getNotes().toString() : null);
        entity.setProviderId(invoice.getProviderId() != null ? invoice.getProviderId().getValue() : null);

        // Mapear items
        entity.setItems(
                invoice.getItems().stream()
                        .map(item -> toInvoiceItemEntity(item, entity))
                        .collect(Collectors.toList())
        );

        return entity;
    }

    private InvoiceItemEntity toInvoiceItemEntity(InvoiceItem item, InvoiceEntity invoiceEntity) {
        InvoiceItemEntity entity = new InvoiceItemEntity();
        entity.setInvoice(invoiceEntity);
        entity.setServiceCode(item.getServiceCode());
        entity.setServiceDescription(item.getServiceDescription());
        entity.setUnitPrice(item.getUnitPrice().asBigDecimal());
        entity.setCurrency(item.getUnitPrice().getCurrency().getCurrencyCode());
        entity.setQuantity(item.getQuantity().getValue());
        entity.setPerformedDate(item.getPerformedAt());

        // Nota: Las relaciones con DentalServiceEntity y RateEntity
        // se establecen en el adapter usando los repositorios

        return entity;
    }
}