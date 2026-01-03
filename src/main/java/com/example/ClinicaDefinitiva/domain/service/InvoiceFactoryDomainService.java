package com.example.ClinicaDefinitiva.domain.service;

import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ServiceRendered;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.InvoiceItem;
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.InvoiceItemId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceFactoryDomainService {
    public static Invoice createDraft(InvoiceId id, PatientId patientId,
                                      DentistId providerId,
                                      List<ServiceRendered> services,
                                      List<Rate> rates,
                                      LocalDateTime issuedAt,
                                      LocalDateTime dueDate,
                                      ContractId contractId) {

        if (services == null || services.isEmpty()) throw new IllegalArgumentException("Invoice must have at least one item");
        if (rates == null || rates.isEmpty()) throw new DomainException("Rates required to price services");
        String currency = rates.get(0).getCurrency();
        String payer = rates.get(0).getPayer_type();

        Invoice invoice = new Invoice(id,
                patientId,
                providerId,
                issuedAt,dueDate
                ,currency
                ,payer,
                contractId,
                "" );

        for (ServiceRendered s : services) {
            Rate rate = rates.stream().filter(r -> r.getServiceId().getCode().equals(s.getServiceCode())).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Rate not found for service " + s.getServiceCode()));
            if (!rate.isValidAt(s.getPerformedAt())) throw new IllegalArgumentException("Rate not valid at performedAt for service " + s.getServiceCode());
            if (!rate.getCurrency().equals(currency)) throw new IllegalArgumentException("Inconsistent rate currency for service " + s.getServiceCode());
            InvoiceItemId itemId = InvoiceItemId.generate();
            InvoiceItem item = new InvoiceItem(
                    itemId,
                    s.getServiceCode(),
                    s.getDescription(),
                    s.getQuantity(),
                    rate.getAmount(),
                    rate.getCurrency(),
                    rate.getId(),
                    s.getPerformedAt()
                    , s.getProviderId(),
                    invoice.getId());
            invoice.addItem(item);
        }
        invoice.validateBeforeEmit();
        return invoice;
    }

    // recalcula totales y devuelve la misma instancia con totales actualizados
    public static Invoice calce(Invoice invoice) {
        if (invoice == null) throw new DomainException("Invoice required");
        invoice.recalcTotals();
        return invoice;
    }

    // Actualiza la factura reemplazando items a partir de servicios y rates.
    // Mantiene otras propiedades de invoice (issuedAt, dueDate, payer, etc).
    public static Invoice update(Invoice invoice,
                                 List<ServiceRendered> services,
                                 List<Rate> rates,
                                 LocalDateTime newIssuedAt,
                                 LocalDateTime newDueDate,
                                 String notes) {

        if (invoice == null) throw new DomainException("Invoice required");
        if (services == null || services.isEmpty()) throw new DomainException("Invoice must have at least one item");
        if (rates == null || rates.isEmpty()) throw new DomainException("Rates required to price services");

        String currency = rates.get(0).getCurrency();
        List<InvoiceItem> newItems = new ArrayList<>();
        for (ServiceRendered s : services) {
            Rate rate = rates.stream()
                    .filter(r -> r.getServiceId().getCode().equals(s.getServiceCode()))
                    .findFirst()
                    .orElseThrow(() -> new DomainException("Rate not found for service " + s.getServiceCode()));
            if (!rate.isValidAt(s.getPerformedAt())) throw new DomainException("Rate not valid at performedAt for service " + s.getServiceCode());
            if (!rate.getCurrency().equals(currency)) throw new DomainException("Inconsistent rate currency for service " + s.getServiceCode());

            InvoiceItemId itemId = InvoiceItemId.generate();
            InvoiceItem item = new InvoiceItem(
                    itemId,
                    s.getServiceCode(),
                    s.getDescription(),
                    s.getQuantity(),
                    rate.getAmount(),
                    rate.getCurrency(),
                    rate.getId(),
                    s.getPerformedAt(),
                    s.getProviderId(),
                    invoice.getId()
            );
            newItems.add(item);
        }

        // Reemplazar items dentro del aggregate (aggregate mantiene invariantes)
        invoice.replaceAllItems(newItems);

        // Actualizar fechas y notas permitidas
        if (newIssuedAt != null) { /* si necesita reglas de negocio sobre cambiar issuedAt, añadir aquí */ }
        if (newDueDate != null) { /* validaciones de periodo */ }

        // Asumiendo setters o
        // Ejemplo simple si Invoice tuviera métodos para actualizar:
        // invoice.updateDates(newIssuedAt, newDueDate);
        // invoice.updateNotes(newNotes);

        // Para este ejemplo mínimo, validamos y recalc
        invoice.recalcTotals();
        invoice.validateBeforeEmit();
        return invoice;
    }







}
