package com.example.ClinicaDefinitiva.domain.dental.care.service.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.RateId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.num.TreatmentStatus;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.TreatmentPhase;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.TreatmentError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;

import java.time.LocalDate;
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

    private Treatment(TreatmentId id,
                      PatientId patientId,
                      DentistId dentistId,
                      ServiceId servicioId,
                      TreatmentStatus status,
                      LocalDate startDate,
                      LocalDate expectedEndDate,
                      List<TreatmentPhase> phases,
                      String notes,
                      RateId tarifaId) {

        Objects.requireNonNull(id, "TreatmentId no puede ser nulo");
        Objects.requireNonNull(patientId, "PatientId no puede ser nulo");
        Objects.requireNonNull(dentistId, "DentistId no puede ser nulo");
        Objects.requireNonNull(servicioId, "ServiceId no puede ser nulo");
        Objects.requireNonNull(status, "TreatmentStatus no puede ser nulo");
        Objects.requireNonNull(startDate, "StartDate no puede ser nulo");

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

        this.id = id;
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.servicioId = servicioId;
        this.status = status;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.phases = List.copyOf(phases);
        this.notes = notes;
        this.tarifaId = tarifaId;
    }

    public static Treatment createNew(TreatmentId id,
                                      PatientId patientId,
                                      DentistId dentistId,
                                      ServiceId servicioId,
                                      LocalDate startDate,
                                      LocalDate expectedEndDate,
                                      List<TreatmentPhase> phases,
                                      String notes,
                                      RateId tarifaId) {
        return new Treatment(id, patientId, dentistId, servicioId,
                TreatmentStatus.ACTIVE, startDate, expectedEndDate, phases, notes, tarifaId);
    }

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

    public ServiceId getServicioId() { return servicioId; }
    public RateId getTarifaId() { return tarifaId; }
    public TreatmentStatus getStatus() { return status; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getExpectedEndDate() { return expectedEndDate; }
    public LocalDate getActualEndDate() { return actualEndDate; }
    public List<TreatmentPhase> getPhases() { return phases; }
    public String getNotes() { return notes; }
}


