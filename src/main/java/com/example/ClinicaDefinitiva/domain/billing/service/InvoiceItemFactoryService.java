package com.example.ClinicaDefinitiva.domain.billing.service;


import com.example.ClinicaDefinitiva.domain.billing.model.InvoiceItem;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.*;
import com.example.ClinicaDefinitiva.domain.dental.care.service.model.ProvidedService;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain Service: InvoiceItemFactoryService
 *
 * Responsabilidad:
 * - Orquestar la creación de InvoiceItem tomando snapshots de Service y Rate.
 * - Delegar al módulo DentalService la validación de serviceCode y serviceDescription.
 * - Garantizar que los InvoiceItem se construyan con datos consistentes y auditables.
 * Decisiones de diseño:
 * - Este servicio evita que InvoiceItem dependa directamente de ProvidedService.
 * - InvoiceItem solo valida nulos y calcula totales; las reglas de contenido se delegan.
 */
public class InvoiceItemFactoryService {


    public InvoiceItem createFromRateSnapshot(
            InvoiceItemId id,
            ProvidedService service,
            Rate rate,
            Quantity quantity,
            LocalDateTime performedAt) {

        Objects.requireNonNull(service, "El servicio no puede ser nulo");
        Objects.requireNonNull(rate, "La tarifa no puede ser nula");

        return InvoiceItem.builder()
                .id(id)
                .serviceId(service.getId())
                .serviceCode(service.getCode().getValue())
                .serviceDescription(service.getName().getValue())
                .unitPrice(rate.getAmount())
                .quantity(quantity)
                .rateId(rate.getId())
                .performedAt(performedAt)
                .build();
    }



}

