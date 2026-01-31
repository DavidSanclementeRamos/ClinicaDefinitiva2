package com.example.ClinicaDefinitiva.domain.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import java.util.*;

/**
 * Representa el resultado de una operación de dominio que puede tener éxito o fallar.
 * Puede contener un valor en caso de éxito, o detalles de error en caso de fallo.
 *
 * @param <T> Tipo del valor en caso de éxito (usar Void si no hay valor)
 */
public class Outcome<T> {

    private final List<OutcomeDetail> details;
    private final T value;


    private Outcome(T value) {
        this.value = value;
        this.details = List.of(); // inmutable y vacío
    }

    private Outcome(List<OutcomeDetail> details) {
        this.value = null;
        this.details = List.copyOf(details); // inmutable
    }


    public static <T> Outcome<T> ok(T value) {
        return new Outcome<>(value);
    }

    public static Outcome<Void> ok() {
        return new Outcome<>((Void) null);
    }

    public static <T> Outcome<T> fail(OutcomeDetail detail) {
        Objects.requireNonNull(detail, "Detail cannot be null");
        return new Outcome<>(List.of(detail));
    }

    public static <T> Outcome<T> fail(List<OutcomeDetail> details) {
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("Details cannot be null or empty for failed outcome");
        }
        return new Outcome<>(details);
    }


    public boolean isSuccess() {
        return details.isEmpty();
    }

    public boolean isFailure() {
        return !details.isEmpty();
    }

    public Optional<T> getValue() {
        return isSuccess() ? Optional.ofNullable(value) : Optional.empty();
    }

    public List<OutcomeDetail> getDetalles() {
        return details;
    }


    /**
     * Combina este Outcome con otro, acumulando errores si existen.
     */
    public Outcome<T> merge(Outcome<?> other) {
        if (this.isSuccess() && other.isSuccess()) {
            return Outcome.ok(this.value);
        }
        List<OutcomeDetail> merged = new ArrayList<>(this.details);
        merged.addAll(other.getDetalles());
        return Outcome.fail(merged);
    }

    /**
     * Devuelve un nuevo Outcome con un detalle adicional.
     */
    public Outcome<T> addDetail(OutcomeDetail detail) {
        Objects.requireNonNull(detail, "Detail cannot be null");
        List<OutcomeDetail> newDetails = new ArrayList<>(this.details);
        newDetails.add(detail);
        return Outcome.fail(newDetails);
    }

    @Override
    public String toString() {
        if (isSuccess()) {
            return "Outcome: SUCCESS, value=" + value;
        }
        return "Outcome: FAILURE, details=" + details;
    }
}
