package com.example.ClinicaDefinitiva.application.mapper.billing.invoice;


import com.example.ClinicaDefinitiva.application.dto.billing.invoice.AddInvoiceItemDto;
import com.example.ClinicaDefinitiva.application.dto.billing.invoice.CreateInstitutionalInvoiceDto;
import com.example.ClinicaDefinitiva.application.dto.billing.invoice.CreateParticularInvoiceDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.model.InvoiceItem;
import com.example.ClinicaDefinitiva.domain.billing.service.InvoiceItemFactoryService;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.*;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversión de DTOs de aplicación a Invoice domain (escritura)
 */
@Component
public class InvoiceWriteMapper {

    /**
     * Convierte CreateParticularInvoiceDto a Invoice (domain)
     */
    public Invoice fromCreateParticularDto(CreateParticularInvoiceDto dto) {
        if (dto == null) {
            return null;
        }

        return Invoice.createParticular(
                PatientId.of(dto.patientId()),
                ProviderId.of(dto.providerId()),
                DentistId.of(dto.dentistId()),
                CurrencyCode.of(dto.currency() != null ? dto.currency() : "COP"),
                dto.notes() != null ? Notes.of(dto.notes()) : null,
                dto.dueDate()
        );
    }

    /**
     * Convierte CreateInstitutionalInvoiceDto a Invoice (domain)
     */
    public Invoice fromCreateInstitutionalDto(CreateInstitutionalInvoiceDto dto) {
        if (dto == null) {
            return null;
        }

        return Invoice.createInstitutional(
                ContractId.of(dto.contractId()),
                ProviderId.of(dto.providerId()),
                DentistId.of(dto.dentistId()),
                CurrencyCode.of(dto.currency() != null ? dto.currency() : "COP"),
                dto.notes() != null ? Notes.of(dto.notes()) : null,
                dto.dueDate()
        );
    }

    /**
     * Convierte AddInvoiceItemDto a InvoiceItem usando el factory service
     */
    public InvoiceItem toInvoiceItem(AddInvoiceItemDto dto,
                                     InvoiceItemFactoryService factoryService) {
        if (dto == null) {
            return null;
        }

        // Por ahora retornamos la estructura básica.
        // El ApplicationService completará con el factory.
        return InvoiceItem.builder()
                .id(InvoiceItemId.of(dto.item()))
                .serviceId(ServiceId.of(dto.serviceId()))
                .rateId(RateId.of(dto.rateId()))
                .quantity(Quantity.of(dto.quantity()))
                .performedAt(dto.performedAt())
                .build();
    }
}


