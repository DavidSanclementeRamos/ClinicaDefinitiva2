package com.example.ClinicaDefinitiva.application.mapper;

import com.example.ClinicaDefinitiva.application.dto.billing.InvoiceDto;
import com.example.ClinicaDefinitiva.application.dto.billing.InvoiceItemDto;
import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ServiceRendered;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.InvoiceItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class InvoiceMapper {
    public ServiceRendered toServiceRenderedDomain(ServiceRenderedDto dto) {
        return new ServiceRendered(dto.serviceId, dto.serviceCode, dto.description, dto.quantity, dto.performedAt, toProviderId(dto.providerId));
    }

    public InvoiceDto toInvoiceDto(Invoice invoice) {
        InvoiceDto dto = new InvoiceDto();
        // Asumimos que invoice.getId() es un VO con toString/valor
        dto.id = invoice.getId() == null ? null : invoice.getId().toString();
        dto.patientId = invoice.getPatientId() == null ? null : invoice.getPatientId().toString();
        dto.providerId = invoice.getProviderId() == null ? null : invoice.getProviderId().toString();
        dto.currency = invoice.getCurrency() == null ? null : invoice.getCurrency();
        dto.payerType = invoice.getPayer();
        dto.issuedAt = invoice.getIssuedAt();
        dto.dueDate = invoice.getDueDate();
        dto.items = invoice.getItems().stream().map(this::toItemDto).collect(Collectors.toList());
        dto.total = invoice.getTotal() == null ? null : invoice.getTotal().asBigDecimal();
        dto.notes = invoice.getNotes();
        dto.status = invoice.getStatus() == null ? null : invoice.getStatus().name();
        return dto;
    }

    private InvoiceItemDto toItemDto(InvoiceItem item) {
        InvoiceItemDto dto = new InvoiceItemDto();
        dto.id = item.getId() == null ? null : item.getId().toString();
        dto.serviceCode = item.getServiceCode();
        dto.description = item.getDescription();
        dto.quantity = item.getQuantity();
        dto.unitPrice = item.getUnitPrice() == null ? null : item.getUnitPrice().asBigDecimal();
        dto.currency = item.getCurrency();
        dto.rateId = String.valueOf(item.getRateId());
        dto.totalPrice = item.getTotalPrice() == null ? null : item.getTotalPrice().asBigDecimal();
        return dto;

    }
    // Helpers: convertir id string -> InvoiceId VO y generar un id nuevo
    public InvoiceId invoiceIdFromString(String id) {
    if (id == null) return null;
    // adapta según tu VO real; aquí se asume que InvoiceId tiene método estático fromString
    return InvoiceId.fromString(id);
    }

    public String invoiceIdToString(InvoiceId id) {
    return id == null ? null : id.toString();
    }

    // Si necesitas generar un nuevo id para la factura
    public InvoiceId nextInvoiceId() {
    return InvoiceId.generate(); // adapta a tu fábrica de VOs
    }

   // Map patient/provider ids si tus VOs lo requieren
   public PatientId toPatientId(String patientId) {
    return patientId == null ? null : PatientId.fromString(patientId);
  }

  public DentistId toProviderId(String providerId) {
      return providerId == null ? null : DentistId.fromString(providerId);
  }
  /** // Helper: convierte String -> DentistId (adapta según tu VO)
   public DentistId toProviderId(String providerId) {
   if (providerId == null) return null; // o lanzar excepción si es requerido
   return DentistId.fromString(providerId); // implementa fromString en tu VO
   }

   */
}




