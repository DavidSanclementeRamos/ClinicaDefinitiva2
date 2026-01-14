package com.example.ClinicaDefinitiva.domain.billing.doiman.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ServiceRendered;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.Price;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.InvoiceItemId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.RateId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class InvoiceItem {
    // Representa un servicio facturado.

    private final InvoiceItemId id;                   // Identificador único
    private final ServiceRendered service;         // Referencia al servicio prestado
    private final String description;          // Descripción del ítem (ej. "Extracción cordal")
    private final int quantity;             // Cantidad (ej. 1, 2 unidades)
    private final Price unitPrice;         // Precio unitario aplicado
    private final Price totalPrice;        // Total (quantity * unit_price)
    private final RateId rateId;  //Referencia a la tarifa usada
    private final LocalDateTime performedAt;

    public InvoiceItem(
            InvoiceItemId id,
            ServiceRendered service,
            String description,
            int quantity,
            Price unitPrice,
            RateId rateId,
            LocalDateTime performedAt,
            InvoiceId invoiceId) {

        // Validaciones esenciales
        validateRequiredFields( invoiceId, performedAt);
        validateQuantity(quantity);


        // Asignaciones
        this.id = id;
        this.service= service;
        this.description = description != null ? description : "";
        this.quantity = quantity;
       this.unitPrice = unitPrice;
        this.rateId = rateId;
        this.performedAt = performedAt;

        // Cálculo automático del total (inmutable)
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    private void validateRequiredFields(
            InvoiceId invoiceId,
            LocalDateTime performedAt) {

        Objects.requireNonNull(invoiceId, "InvoiceId is required");
        Objects.requireNonNull(performedAt, "PerformedAt is required");

    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    String.format("Quantity must be positive. Provided: %d", quantity)
            );
        }
    }


    /**
     * Factory method: Crea InvoiceItem desde una tarifa vigente.
     *
     * Este método encapsula la lógica de snapshot:
     * - Captura Rate.amount como unitPriceSnapshot
     * - Valida que Rate esté vigente (delegado a caller)
     * - Crea referencia trazable (rateIdSnapshot)
     *
     * Uso típico en Domain Service:
     * ```java
     * Rate rate = rateRepository.findActiveRateFor(service, payer);
     * rate.ensureValidAt(invoiceDate); // Validación crítica
     *
     */
    public static InvoiceItem fromRate(
            InvoiceItemId id,
            ServiceRendered service,
            String description,
            int quantity,
            Price unitPrice,
            RateId rateId,
            LocalDateTime performedAt,
            InvoiceId invoiceId
             )
             {

        Objects.requireNonNull(rateId, "Rate is required");

        return new InvoiceItem(
                id,
                 service,
                description,
                quantity,
                unitPrice,
                rateId,
                performedAt,
                invoiceId




        );
    }

    public InvoiceItemId getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    public int getQuantity() {
        return quantity;
    }

    public Price getTotalPrice() {
        return totalPrice;
    }

    public RateId getRateId() {
        return rateId;
    }

    public LocalDateTime getPerformedAt() {
        return performedAt;
    }

}
