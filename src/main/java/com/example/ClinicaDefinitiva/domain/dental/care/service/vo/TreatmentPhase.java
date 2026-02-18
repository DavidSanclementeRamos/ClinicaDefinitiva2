package com.example.ClinicaDefinitiva.domain.dental.care.service.vo;


import com.example.ClinicaDefinitiva.domain.dental.care.service.num.PhaseStatus;

import java.time.LocalDate;
import java.util.Objects;

public class TreatmentPhase {
    private final String name;
    private final LocalDate scheduledDate;
    private final PhaseStatus status;
    private final String notes;

    private TreatmentPhase(String name, LocalDate scheduledDate, PhaseStatus status, String notes) {
        Objects.requireNonNull(name, "Phase name cannot be null");
        Objects.requireNonNull(status, "Phase status cannot be null");

        this.name = name;
        this.scheduledDate = scheduledDate;
        this.status = status;
        this.notes = notes;
    }
    public static TreatmentPhase of(String name, LocalDate scheduledDate,PhaseStatus status, String notes){
        return new TreatmentPhase(name,scheduledDate,status,notes);
    }

    public String getName() { return name; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public String getNotes() { return notes; }

    public PhaseStatus getStatus() {
        return status;
    }
}

