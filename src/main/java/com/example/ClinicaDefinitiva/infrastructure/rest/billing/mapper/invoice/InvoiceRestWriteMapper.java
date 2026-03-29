package com.example.ClinicaDefinitiva.infrastructure.rest.billing.mapper.invoice;

import com.example.ClinicaDefinitiva.application.billing.dto.invoice.AddInvoiceItemDto;
import com.example.ClinicaDefinitiva.application.billing.dto.invoice.CreateInstitutionalInvoiceDto;
import com.example.ClinicaDefinitiva.application.billing.dto.invoice.CreateParticularInvoiceDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.billing.dto.invoice.AddInvoiceItemRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.billing.dto.invoice.CreateInstitutionalInvoiceRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.billing.dto.invoice.CreateParticularInvoiceRequest;
import org.springframework.stereotype.Component;

@Component
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
       
       return new AddInvoiceItemDto(
        request.item(),
        request.serviceId(),
        request.serviceCode(),
        request.serviceDescription(),
        request.rateId(),
        request.unitPrice(),
        request.currency(),
        request.quantity(),
        request.performedAt()
);    }
}
