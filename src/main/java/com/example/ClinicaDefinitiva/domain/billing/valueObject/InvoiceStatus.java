package com.example.ClinicaDefinitiva.domain.billing.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.BillingVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Value Object: InvoiceStatus (Estado de Factura con Máquina de Estados)
 * Estados posibles:
 * - DRAFT: Borrador (puede modificarse)
 * - PENDING: Emitida pendiente de pago (inmutable, solo puede pagarse o cancelarse)
 * - PAID: Pagada (estado final, inmutable)
 * - CANCELLED: Cancelada (estado final, inmutable)
 */
public final class InvoiceStatus {



    public enum Status {

        DRAFT,
        PENDING,
        PAID,
        CANCELLED
    }

    private static final EnumMap<Status, Set<Status>> VALID_TRANSITIONS = new EnumMap<>(Status.class);

    static {
        VALID_TRANSITIONS.put(Status.DRAFT, EnumSet.of(Status.PENDING, Status.CANCELLED));
        VALID_TRANSITIONS.put(Status.PENDING, EnumSet.of(Status.PAID, Status.CANCELLED));
        VALID_TRANSITIONS.put(Status.PAID, EnumSet.noneOf(Status.class)); // Estado final
        VALID_TRANSITIONS.put(Status.CANCELLED, EnumSet.noneOf(Status.class)); // Estado final
    }

    private final Status value;

    private InvoiceStatus(Status value) {
        Objects.requireNonNull(value, "Status cannot be null");
        this.value = value;
    }

    public static InvoiceStatus draft() {
        return new InvoiceStatus(Status.DRAFT);
    }

    public static InvoiceStatus pending() {
        return new InvoiceStatus(Status.PENDING);
    }

    public static InvoiceStatus paid() {
        return new InvoiceStatus(Status.PAID);
    }


    public static InvoiceStatus cancelled() {
        return new InvoiceStatus(Status.CANCELLED);
    }

    public static InvoiceStatus of(Status status) {
        return new InvoiceStatus(status);
    }


    public boolean canTransitionTo(Status next) {
        Set<Status> allowedTransitions = VALID_TRANSITIONS.getOrDefault(
                this.value,
                EnumSet.noneOf(Status.class)
        );
        return allowedTransitions.contains(next);
    }

    public InvoiceStatus transitionTo(Status next) {
        if (!canTransitionTo(next)) {
            throw new ValueObjectValidationException(BillingVOError.ERR_INVOICE_INVALID_STATUS_TRANSITION, VOContext.BILLING


            );
        }
        return new InvoiceStatus(next);
    }

    public boolean isDraft() {
        return this.value == Status.DRAFT;
    }

    public boolean isPending() {
        return this.value == Status.PENDING;
    }

    public boolean isPaid() {
        return this.value == Status.PAID;
    }

    public boolean isCancelled() {
        return this.value == Status.CANCELLED;
    }

    public boolean isEditable() {
        return isDraft() || isPending();
    }

    public Status getValue() {
        return value;
    }

}