package com.example.ClinicaDefinitiva.domain.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Outcome {

    private final List<OutcomeDetail> detalles = new ArrayList<>();

    public static Outcome ok() {
        return new Outcome();
    }

    public static Outcome fail(OutcomeDetail detalle) {
        Outcome outcome = new Outcome();
        outcome.detalles.add(detalle);
        return outcome;
    }

    public Outcome combine(Outcome other) {
        Outcome combined = new Outcome();
        combined.detalles.addAll(this.detalles);
        combined.detalles.addAll(other.detalles);
        return combined;
    }

    public boolean isSuccess() {
        return detalles.isEmpty();
    }

    public List<OutcomeDetail> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }
}