package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.billing.invoice;

import com.example.ClinicaDefinitiva.application.dto.billing.invoice.PageInvoiceDto;
import com.example.ClinicaDefinitiva.application.dto.billing.invoice.ReadInvoiceDto;
import com.example.ClinicaDefinitiva.application.dto.billing.item.InvoiceItemDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.InvoiceItemResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.invoice.PageInvoiceResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.invoice.ReadInvoiceResponse;

import java.util.stream.Collectors;

public class InvoiceRestReadMapper {
    /**
     * Convierte ReadInvoiceDto (application) a ReadInvoiceResponse (REST)
     */
    public ReadInvoiceResponse toRest(ReadInvoiceDto dto) {
        if (dto == null) {
            return null;
        }

        return new ReadInvoiceResponse(
                dto.contractId(),
                dto.status(),
                dto.currency(),
                dto.subtotal(),
                dto.tax(),
                dto.total(),
                dto.dueDate(),
                dto.createdAt(),
                dto.updatedAt(),
                dto.notes(),
                //dto.items() != null ?
                dto.items().stream()
                        .map(this::toItemRest).collect(
                        Collectors.toList())

        );
    }

    /**
     * Convierte PageInvoiceDto (application) a PageInvoiceResponse (REST)
     */
    public PageInvoiceResponse toPageRest(PageInvoiceDto dto) {
        if (dto == null) {
            return null;
        }

        return new PageInvoiceResponse(
                dto.id(),
                dto.invoiceNumber(),
                dto.dentistId(),
                dto.providerId(),
                dto.status(),
                dto.total(),
                dto.currency(),
                dto.dueDate(),
                dto.createdAt()
        );

    }

    /**
     * Convierte InvoiceItemDto (application) a InvoiceItemResponse (REST)
     */
    private InvoiceItemResponse toItemRest(InvoiceItemDto dto) {
        if (dto == null) {
            return null;
        }

        return new InvoiceItemResponse(
                dto.id(),
                dto.serviceId(),
                dto.serviceCode(),
                dto.serviceDescription(),
                dto.unitPrice(),
                dto.quantity(),
                dto.totalPrice(),
                dto.rateId(),
                dto.performedAt()
        );
    }
}
