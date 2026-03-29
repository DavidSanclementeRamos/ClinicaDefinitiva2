package com.example.ClinicaDefinitiva.application.billing.mapper.invoice;


import com.example.ClinicaDefinitiva.application.billing.dto.invoice.AddInvoiceItemDto;
import com.example.ClinicaDefinitiva.application.billing.dto.invoice.CreateInstitutionalInvoiceDto;
import com.example.ClinicaDefinitiva.application.billing.dto.invoice.CreateParticularInvoiceDto;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.vo.CurrencyCode;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceItemId;
import com.example.ClinicaDefinitiva.domain.billing.vo.ProviderId;
import com.example.ClinicaDefinitiva.domain.billing.vo.Quantity;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversión de DTOs de aplicación a Invoice domain (escritura)
 */
@Component
public class InvoiceWriteMapper {

    public PatientId toPatientId(CreateParticularInvoiceDto dto) {
        return PatientId.of(dto.patientId());
    }

    public ProviderId toProviderId(CreateParticularInvoiceDto dto) {
        return ProviderId.of(dto.providerId());
    }

    public DentistId toDentistId(CreateParticularInvoiceDto dto) {
        return DentistId.of(dto.dentistId());
    }

    public CurrencyCode toCurrency(CreateParticularInvoiceDto dto) {
        return CurrencyCode.of(dto.currency());
    }

    public Notes toNotes(CreateParticularInvoiceDto dto) {
        return Notes.of(dto.notes());
    }

    public LocalDateTime toDueDate(CreateParticularInvoiceDto dto) {
        return dto.dueDate();
    }

    public ContractId toContractId(CreateInstitutionalInvoiceDto dto) {
        return ContractId.of(dto.contractId());
    }

    public ProviderId toProviderId(CreateInstitutionalInvoiceDto dto) {
        return ProviderId.of(dto.providerId());
    }

    public DentistId toDentistId(CreateInstitutionalInvoiceDto dto) {
        return DentistId.of(dto.dentistId());
    }

    public CurrencyCode toCurrency(CreateInstitutionalInvoiceDto dto) {
        return CurrencyCode.of(dto.currency());
    }

    public Notes toNotes(CreateInstitutionalInvoiceDto dto) {
        return Notes.of(dto.notes());
    }

    public LocalDateTime toDueDate(CreateInstitutionalInvoiceDto dto) {
        return dto.dueDate();
    }

    public InvoiceItemId toInvoiceItemId(AddInvoiceItemDto dto) {
        return InvoiceItemId.of(dto.item());
    }

    public ServiceId toServiceId(AddInvoiceItemDto dto) {
        return ServiceId.of(dto.serviceId());
    }

    public RateId toRateId(AddInvoiceItemDto dto) {
        return RateId.of(dto.rateId());
    }

    public Quantity toQuantity(AddInvoiceItemDto dto) {
        return Quantity.of(dto.quantity());
    }

    public LocalDateTime toPerformedAt(AddInvoiceItemDto dto) {
        return dto.performedAt();
    }
   

    public String toServiceCode(AddInvoiceItemDto dto) {
        return dto.serviceCode();
    }

    public String toServiceDescription(AddInvoiceItemDto dto) {
        return dto.serviceDescription();
    }
    
    public Price toUnitPrice(AddInvoiceItemDto dto) {
        return Price.of(dto.unitPrice(), dto.currency());
    }


}