package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.billing.invoice;

import com.example.ClinicaDefinitiva.application.dto.billing.invoice.AddInvoiceItemDto;
import com.example.ClinicaDefinitiva.application.dto.billing.invoice.CreateInstitutionalInvoiceDto;
import com.example.ClinicaDefinitiva.application.dto.billing.invoice.CreateParticularInvoiceDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.invoice.AddInvoiceItemRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.invoice.CreateInstitutionalInvoiceRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.invoice.CreateParticularInvoiceRequest;

public class InvoiceRestWriteMapper {
    /**
     * Convierte CreateParticularInvoiceRequest (REST) a CreateParticularInvoiceDto (application)
     */
    public CreateParticularInvoiceDto toServiceCreateParticular(CreateParticularInvoiceRequest request) {
        if (request == null) {
            return null;
        }

        return new CreateParticularInvoiceDto(
                request.patientId(),
                request.dentistId(),
                request.providerId(),
                request.currency(),
                request.dueDate(),
                request.notes()
        );
    }


    /**
     * Convierte CreateInstitutionalInvoiceRequest (REST) a CreateInstitutionalInvoiceDto (application)
     */
    public CreateInstitutionalInvoiceDto toServiceCreateInstitutional(CreateInstitutionalInvoiceRequest request) {
        if (request == null) {
            return null;
        }

        return new CreateInstitutionalInvoiceDto(
                request.patientId(),
                request.dentistId(),
                request.providerId(),
                request.contractId(),
                request.currency(),
                request.dueDate(),
                request.notes()
        );
    }

    /**
     * Convierte AddInvoiceItemRequest (REST) a AddInvoiceItemDto (application)
     */
    public AddInvoiceItemDto toServiceAddItem(AddInvoiceItemRequest request) {
        if (request == null) {
            return null;
        }

        return new AddInvoiceItemDto(
                request.item(),
                request.serviceId(),
                request.rateId(),
                request.quantity(),
                request.performedAt()
        );
    }
}
