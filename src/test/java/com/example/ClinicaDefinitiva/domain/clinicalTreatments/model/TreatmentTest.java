package com.example.ClinicaDefinitiva.domain.clinicalTreatments.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.PhaseStatus;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.TreatmentStatus;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentPhase;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TreatmentTest {

    private static final PatientId PATIENT_ID = PatientId.of(1L);
    private static final DentistId DENTIST_ID = DentistId.of(2L);
    private static final ServiceId SERVICE_ID = ServiceId.of(3L);
    private static final RateId RATE_ID = RateId.of(4L);
    private static final LocalDate START = LocalDate.now().minusDays(1);
    private static final LocalDate END = LocalDate.now().plusMonths(1);
    private static final List<TreatmentPhase> PHASES = List.of(
            TreatmentPhase.of(Name.of("Fase 1"), LocalDate.now().plusDays(1), PhaseStatus.PENDING, Notes.of("Nota válida"))
    );

    @Test
    @DisplayName("TRE-UNIT-001: Crear tratamiento con fecha inicio pasada (válido)")
    void create_shouldBeActive() {
        Treatment treatment = Treatment.createNew(
                PATIENT_ID, DENTIST_ID, SERVICE_ID, START, END, PHASES, "Notas", RATE_ID
        );

        assertThat(treatment.getStatus()).isEqualTo(TreatmentStatus.ACTIVE);
        assertThat(treatment.getStartDate()).isEqualTo(START);
        assertThat(treatment.getPhases()).hasSize(1);
    }

    @Test
    @DisplayName("TRE-UNIT-001: Fecha inicio futura lanza excepción")
    void create_futureStartDate_shouldThrow() {
        LocalDate futureStart = LocalDate.now().plusDays(1);

        assertThatThrownBy(() -> Treatment.createNew(
                PATIENT_ID, DENTIST_ID, SERVICE_ID, futureStart, END, PHASES, "Notas", RATE_ID
        )).isInstanceOf(BusinessRuleViolationException.class)
          .hasMessageContaining("La fecha de inicio del tratamiento no puede ser futura");
    }

    @Test
    @DisplayName("TRE-UNIT-002: Fecha fin esperada antes del inicio lanza excepción")
    void create_expectedEndBeforeStart_shouldThrow() {
        LocalDate invalidEnd = START.minusDays(1);

        assertThatThrownBy(() -> Treatment.createNew(
                PATIENT_ID, DENTIST_ID, SERVICE_ID, START, invalidEnd, PHASES, "Notas", RATE_ID
        )).isInstanceOf(BusinessRuleViolationException.class)
          .hasMessageContaining("La fecha de fin esperada debe ser posterior a la fecha de inicio");
    }

    @Test
    @DisplayName("TRE-UNIT-005: Tratamiento sin fases lanza excepción")
    void create_withoutPhases_shouldThrow() {
        assertThatThrownBy(() -> Treatment.createNew(
                PATIENT_ID, DENTIST_ID, SERVICE_ID, START, END, List.of(), "Notas", RATE_ID
        )).isInstanceOf(BusinessRuleViolationException.class)
          .hasMessageContaining("El tratamiento debe tener al menos una fase definida");
    }

    @Test
    @DisplayName("TRE-UNIT-003: Completar tratamiento activo")
    void complete_whenActive_shouldChangeStatus() {
        Treatment treatment = createActiveTreatment();
        LocalDate completionDate = LocalDate.now();

        treatment.complete(completionDate);

        assertThat(treatment.getStatus()).isEqualTo(TreatmentStatus.COMPLETED);
        assertThat(treatment.getActualEndDate()).isEqualTo(completionDate);
    }

    @Test
    @DisplayName("TRE-UNIT-003: Completar tratamiento no activo lanza excepción")
    void complete_whenNotActive_shouldThrow() {
        Treatment treatment = createActiveTreatment();
        treatment.complete(LocalDate.now()); // ahora COMPLETED

        assertThatThrownBy(() -> treatment.complete(LocalDate.now()))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("El tratamiento debe estar activo para esta operación");
    }

    @Test
    @DisplayName("TRE-UNIT-003: Fecha de finalización antes del inicio lanza excepción")
    void complete_completionDateBeforeStart_shouldThrow() {
        Treatment treatment = createActiveTreatment();

        assertThatThrownBy(() -> treatment.complete(START.minusDays(1)))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("La fecha de finalización no puede ser anterior a la fecha de inicio");
    }

    @Test
    @DisplayName("TRE-UNIT-004: Cancelar tratamiento activo con razón válida")
    void cancel_withValidReason_shouldChangeStatus() {
        Treatment treatment = createActiveTreatment();

        treatment.cancel("Razón válida con más de diez caracteres");

        assertThat(treatment.getStatus()).isEqualTo(TreatmentStatus.CANCELLED);
    }

    @Test
    @DisplayName("TRE-UNIT-004: Cancelar sin razón o con razón corta lanza excepción")
    void cancel_withInvalidReason_shouldThrow() {
        Treatment treatment = createActiveTreatment();

        assertThatThrownBy(() -> treatment.cancel(null))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("La cancelación requiere un motivo detallado (mínimo 10 caracteres)");

        assertThatThrownBy(() -> treatment.cancel("corta"))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    private Treatment createActiveTreatment() {
        return Treatment.createNew(
                PATIENT_ID, DENTIST_ID, SERVICE_ID, START, END, PHASES, "Notas", RATE_ID
        );
    }
}
