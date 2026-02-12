package com.example.ClinicaDefinitiva.domain.dental.care.service.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.RateId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.num.TreatmentStatus;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.TreatmentPhase;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Treatment {
    private final TreatmentId id;
    private final PatientId patientId;
    private final DentistId dentistId;
    private final ServiceId servicioId; // referencia al servicio clínico
    private TreatmentStatus status;
    private final LocalDate startDate;
    private LocalDate expectedEndDate;
    private LocalDate actualEndDate;
    private final List<TreatmentPhase> phases;
    private final String notes;
    private final RateId tarifaId; // referencia a tarifa en facturación

    public Treatment(TreatmentId id,
                     PatientId patientId,
                     DentistId dentistId,
                     ServiceId servicioId,
                     TreatmentStatus status,
                     LocalDate startDate,
                     LocalDate expectedEndDate,
                     List<TreatmentPhase> phases,
                     String notes,
                     RateId tarifaId) {

        Objects.requireNonNull(id, "TreatmentId cannot be null");
        Objects.requireNonNull(patientId, "PatientId cannot be null");
        Objects.requireNonNull(dentistId, "DentistId cannot be null");
        Objects.requireNonNull(servicioId, "ServicioOdontologicoId cannot be null");
        Objects.requireNonNull(status, "TreatmentStatus cannot be null");
        Objects.requireNonNull(startDate, "StartDate cannot be null");

        this.id = id;
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.servicioId = servicioId;
        this.status = status;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.phases = phases != null ? List.copyOf(phases) : List.of();
        this.notes = notes;
        this.tarifaId = tarifaId;
    }

    public boolean isActive() {
        return status == TreatmentStatus.ACTIVE;
    }

    public void complete(LocalDate actualEndDate) {
        this.status = TreatmentStatus.COMPLETED;
        this.actualEndDate = actualEndDate;
    }

    public void cancel() {
        this.status = TreatmentStatus.CANCELLED;
    }

    // Getters
    public ServiceId getServicioId() { return servicioId; }
    public RateId getTarifaId() { return tarifaId; }
}

