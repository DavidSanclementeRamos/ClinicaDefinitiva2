package com.example.ClinicaDefinitiva.application.mapper.billing.invoice;




import com.example.ClinicaDefinitiva.application.dto.billing.invoice.PageInvoiceDto;
import com.example.ClinicaDefinitiva.application.dto.billing.invoice.ReadInvoiceDto;
import com.example.ClinicaDefinitiva.application.dto.billing.item.InvoiceItemDto;
import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.model.InvoiceItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper para conversión de Invoice domain a DTOs de aplicación (lectura)
 */
@Component
public class InvoiceReadMapper {

    /**
     * Convierte Invoice (domain) a ReadInvoiceDto (application)
     */
    public ReadInvoiceDto toDto(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        return new ReadInvoiceDto(
                invoice.getId().getValue(),
                invoice.getNumber() != null ? invoice.getNumber().getValue() : null,

                invoice.getPatientId() != null ? invoice.getPatientId().getValue() : null,

                invoice.getDentistId() != null ? invoice.getDentistId().getValue() : null,

                invoice.getProviderId() != null ? invoice.getProviderId().getValue() : null,

                invoice.getContractId() != null ? invoice.getContractId().getValue() : null,

                invoice.getStatus().getValue().name(),
                invoice.getCurrency().getCode(),

                invoice.getSubtotal().asBigDecimal(),
                invoice.getTax().asBigDecimal(),
                invoice.getTotal().asBigDecimal(),

                invoice.getDueDate(),
                invoice.getUpdatedAt(),
                invoice.getUpdatedAt(),

                invoice.getNotes() != null ? String.valueOf(invoice.getNotes()) : null,

                invoice.getItems().stream()
                        .map(this::toItemDto)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Convierte Invoice (domain) a PageInvoiceDto (application)
     */
    public PageInvoiceDto toPageDto(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        return new PageInvoiceDto(
                invoice.getId().getValue(),
                invoice.getNumber() != null ? invoice.getNumber().getValue() : null,
                invoice.getPatientId() != null ? invoice.getPatientId().getValue() : null,
                invoice.getDentistId() != null ? invoice.getDentistId().getValue() : null,
                invoice.getStatus().getValue().name(),
                invoice.getTotal().asBigDecimal(),
                invoice.getCurrency().getCode(),
                invoice.getDueDate(),
                invoice.getUpdatedAt()
        );
    }

    /**
     * Convierte InvoiceItem (domain) a InvoiceItemDto (application)
     */
    private InvoiceItemDto toItemDto(InvoiceItem item) {
        if (item == null) {
            return null;
        }

        return new InvoiceItemDto(
                item.getId().getValue(),
                item.getServiceId().getId(),
                item.getServiceCode(),
                item.getServiceDescription(),
                item.getUnitPrice().asBigDecimal(),
                item.getQuantity().getValue(),
                item.getTotalPrice().asBigDecimal(),
                item.getRateId().getValue(),
                item.getPerformedAt()
        );
    }
}