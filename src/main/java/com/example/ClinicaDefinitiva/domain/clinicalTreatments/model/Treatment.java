package com.example.ClinicaDefinitiva.domain.clinicalTreatments.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentPhase;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.TreatmentStatus;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.clinicalTreatments.TreatmentError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Agregado raíz: Tratamiento clínico.
 *
 * Responsabilidades:
 * - Mantener coherencia entre paciente, odontólogo y servicio.
 * - Validar fechas de inicio y fin.
 * - Controlar estado del tratamiento (activo, completado, cancelado).
 * - Asegurar trazabilidad de fases y notas.
 *
 * Reglas de negocio:
 * - RN-TREATMENT-001: La fecha de inicio no puede ser futura.
 * - RN-TREATMENT-002: La fecha de fin esperada debe ser posterior a la fecha de inicio.
 * - RN-TREATMENT-003: Un tratamiento solo puede completarse si está activo.
 * - RN-TREATMENT-004: La cancelación solo aplica si el tratamiento está activo.
 * - RN-TREATMENT-005: El tratamiento debe tener al menos una fase definida.
 */
public class Treatment {

    private final TreatmentId id;
    private final PatientId patientId;
    private final DentistId dentistId;
    private final ServiceId servicioId;
    private TreatmentStatus status;
    private final LocalDate startDate;
    private LocalDate expectedEndDate;
    private LocalDate actualEndDate;
    private final List<TreatmentPhase> phases;
    private final String notes;
    private final RateId tarifaId;

    // Constructor privado: solo valida reglas de negocio
    private Treatment(Builder builder) {
        this.id = builder.id;
        this.patientId = builder.patientId;
        this.dentistId = builder.dentistId;
        this.servicioId = builder.servicioId;
        this.status = builder.status;
        this.startDate = builder.startDate;
        this.expectedEndDate = builder.expectedEndDate;
        this.phases = List.copyOf(builder.phases);
        this.notes = builder.notes;
        this.tarifaId = builder.tarifaId;

        validateBusinessRules();
    }

    private void validateBusinessRules() {
        if (startDate.isAfter(LocalDate.now())) {
            throw new BusinessRuleViolationException(
                    TreatmentError.ERR_TREATMENT_FUTURE_START_DATE,
                    EntityContext.TREATMENT
            );
        }
        if (expectedEndDate != null && expectedEndDate.isBefore(startDate)) {
            throw new BusinessRuleViolationException(
                    TreatmentError.ERR_TREATMENT_INVALID_END_DATE,
                    EntityContext.TREATMENT
            );
        }
        if (phases == null || phases.isEmpty()) {
            throw new BusinessRuleViolationException(
                    TreatmentError.ERR_TREATMENT_PHASES_REQUIRED,
                    EntityContext.TREATMENT
            );
        }
    }

    // Método de fábrica: intención de negocio
    public static Treatment createNew( 
                                      PatientId patientId,
                                      DentistId dentistId,
                                      ServiceId servicioId,
                                      LocalDate startDate,
                                      LocalDate expectedEndDate,
                                      List<TreatmentPhase> phases,
                                      String notes,
                                      RateId tarifaId) {
        return Treatment.builder()
                .withPatientId(patientId)
                .withDentistId(dentistId)
                .withServiceId(servicioId)
                .withStartDate(startDate)
                .withExpectedEndDate(expectedEndDate)
                .withPhases(phases)
                .withNotes(notes)
                .withRateId(tarifaId)
                .build();
    }

    // Métodos de negocio
    public boolean isActive() {
        return status == TreatmentStatus.ACTIVE;
    }

    public void complete(LocalDate actualEndDate) {
        if (!isActive()) {
            throw new BusinessRuleViolationException(
                    TreatmentError.ERR_TREATMENT_NOT_ACTIVE,
                    EntityContext.TREATMENT
            );
        }
        if (actualEndDate.isBefore(startDate)) {
            throw new BusinessRuleViolationException(
                    TreatmentError.ERR_TREATMENT_INVALID_COMPLETION_DATE,
                    EntityContext.TREATMENT
            );
        }
        this.status = TreatmentStatus.COMPLETED;
        this.actualEndDate = actualEndDate;
    }

    public void cancel(String reason) {
        if (!isActive()) {
            throw new BusinessRuleViolationException(
                    TreatmentError.ERR_TREATMENT_NOT_ACTIVE,
                    EntityContext.TREATMENT
            );
        }
        if (reason == null || reason.trim().length() < 10) {
            throw new BusinessRuleViolationException(
                    TreatmentError.ERR_TREATMENT_CANCELLATION_REASON_REQUIRED,
                    EntityContext.TREATMENT
            );
        }
        this.status = TreatmentStatus.CANCELLED;
    }

    // Getters
    public TreatmentId getId() { return id; }
    public PatientId getPatientId() { return patientId; }
    public DentistId getDentistId() { return dentistId; }
    public ServiceId getServicioId() { return servicioId; }
    public RateId getTarifaId() { return tarifaId; }
    public TreatmentStatus getStatus() { return status; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getExpectedEndDate() { return expectedEndDate; }
    public LocalDate getActualEndDate() { return actualEndDate; }
    public List<TreatmentPhase> getPhases() { return phases; }
    public String getNotes() { return notes; }

    // Builder debajo de los setters/métodos de negocio
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private TreatmentId id;
        private PatientId patientId;
        private DentistId dentistId;
        private ServiceId servicioId;
        private TreatmentStatus status = TreatmentStatus.ACTIVE;
        private LocalDate startDate;
        private LocalDate expectedEndDate;
        private List<TreatmentPhase> phases = new ArrayList<>();
        private String notes;
        private RateId tarifaId;

        public Builder withId(TreatmentId id) { this.id = id; return this; }
        public Builder withPatientId(PatientId patientId) { this.patientId = patientId; return this; }
        public Builder withDentistId(DentistId dentistId) { this.dentistId = dentistId; return this; }
        public Builder withServiceId(ServiceId servicioId) { this.servicioId = servicioId; return this; }
        public Builder withStartDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public Builder withExpectedEndDate(LocalDate expectedEndDate) { this.expectedEndDate = expectedEndDate; return this; }
        public Builder withPhases(List<TreatmentPhase> phases) { this.phases = phases; return this; }
        public Builder withNotes(String notes) { this.notes = notes; return this; }
        public Builder withRateId(RateId tarifaId) { this.tarifaId = tarifaId; return this; }

        public Treatment build() { return new Treatment(this); }
    }
}
