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

    public ReadInvoiceDto toDto(Invoice invoice) {
        return new ReadInvoiceDto(
            invoice.getId().getValue(),
            invoice.getNumber().getValue(),
            invoice.getPatientId().value(),
            invoice.getDentistId().value(),
            invoice.getProviderId().getValue(),
            invoice.getContractId().getValue(),
            invoice.getStatus().getValue().name(),
            invoice.getCurrency().getCode(),
            invoice.getSubtotal().asBigDecimal(),
            invoice.getTax().asBigDecimal(),
            invoice.getTotal().asBigDecimal(),
            invoice.getDueDate(),
            invoice.getUpdatedAt(),
            invoice.getUpdatedAt(),
            invoice.getNotes().toString(),
            invoice.getItems().stream()
                   .map(this::toItemDto)
                   .collect(Collectors.toList())
        );
    }

    public PageInvoiceDto toPageDto(Invoice invoice) {
        return new PageInvoiceDto(
            invoice.getId().getValue(),
            invoice.getNumber().getValue(),
            invoice.getPatientId().value(),
            invoice.getDentistId().value(),
            invoice.getStatus().getValue().name(),
            invoice.getTotal().asBigDecimal(),
            invoice.getCurrency().getCode(),
            invoice.getDueDate(),
            invoice.getUpdatedAt()
        );
    }

    private InvoiceItemDto toItemDto(InvoiceItem item) {
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