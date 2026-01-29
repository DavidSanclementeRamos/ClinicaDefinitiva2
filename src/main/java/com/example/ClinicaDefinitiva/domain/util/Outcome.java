package com.example.ClinicaDefinitiva.domain.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * Representa el resultado de una operación de dominio que puede tener éxito o fallar.
 * Puede contener un valor en caso de éxito, o detalles de error en caso de fallo.
 *
 * @param <T> Tipo del valor en caso de éxito (usar Void si no hay valor)
 */
public class Outcome<T> {
    private final List<OutcomeDetail> details = new ArrayList<>();
    private final T value;


    private Outcome(T value) {
        this.value = value;
    }

    private Outcome(OutcomeDetail detail) {
        this.value = null;
        this.details.add(detail);
    }


    private Outcome(List<OutcomeDetail> details) {
        this.value = null;
        this.details.addAll(details);
    }

    // ==================== FACTORY METHODS ====================


    public static <T> Outcome<T> ok(T value) {
        return new Outcome<>(value);
    }

    public static Outcome<Void> ok() {
        return new Outcome<>((Void) null);
    }

    public static <T> Outcome<T> fail(OutcomeDetail detail) {
        return new Outcome<>(detail);
    }

    public static <T> Outcome<T> fail(List<OutcomeDetail> details) {
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("Detalles cannot be null or empty for failed outcome");
        }
        return new Outcome<>(details);
    }

    // ==================== QUERY METHODS ====================


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
        return Collections.unmodifiableList(details);
    }


}