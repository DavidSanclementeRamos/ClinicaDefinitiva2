package com.example.ClinicaDefinitiva.domain.billing.doiman.enu;

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

    public static InvoiceStatus fromString(String statusString) {
        try {
            Status status = Status.valueOf(statusString.toUpperCase());
            return new InvoiceStatus(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    String.format("Invalid status: %s. Valid values: DRAFT, PENDING, PAID, CANCELLED", statusString)
            );
        }
    }

    public boolean canTransitionTo(Status next) {
        Set<Status> allowedTransitions = VALID_TRANSITIONS.getOrDefault(
                this.value,
                EnumSet.noneOf(Status.class)
        );
        return allowedTransitions.contains(next);
    }

    /**
     * Transiciona a un nuevo estado con validación.
     *
     * Este es el método PRINCIPAL para cambiar estados.
     * Lanza excepción si la transición no es válida.
     */
    public InvoiceStatus transitionTo(Status next) {
        if (!canTransitionTo(next)) {
            throw new ValueObjectValidationException(
                    this.value.name(),
                    next.name(),
                    String.format(
                            "Transición inválida: %s → %s. Transiciones válidas desde %s: %s",
                            this.value,
                            next,
                            this.value,
                            VALID_TRANSITIONS.get(this.value)
                    )
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