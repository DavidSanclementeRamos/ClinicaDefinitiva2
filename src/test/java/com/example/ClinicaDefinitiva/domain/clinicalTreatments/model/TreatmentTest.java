
package com.example.ClinicaDefinitiva.domain.clinicalTreatments.model;


import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.PhaseStatus;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.TreatmentStatus;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentPhase;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.clinicalTreatments.TreatmentError;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Treatment aggregate tests")
class TreatmentTest {

    private PatientId patientId = PatientId.of(1L);
    private DentistId dentistId = DentistId.of(2L);
    private ServiceId serviceId = ServiceId.of(3L);
    private RateId rateId = RateId.of(4L);

    private TreatmentPhase samplePhase() {
        return  TreatmentPhase.of(Name.of( "Diagnóstico"),LocalDate.now(),PhaseStatus.IN_PROGRESS,Notes.of("nota"));
    }

    @Nested
    @DisplayName("Creation rules")
    class CreationTests {

        @Test
        @DisplayName("createNew - success")
        void createNew_success() {
            Treatment treatment = Treatment.createNew(
                    patientId,
                    dentistId,
                    serviceId,
                    LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(30),
                    List.of(samplePhase()),
                    "Notas iniciales",
                    rateId
            );

            assertThat(treatment).isNotNull();
            assertThat(treatment.isActive()).isTrue();
            assertThat(treatment.getPhases()).hasSize(1);
            assertThat(treatment.getExpectedEndDate()).isAfter(treatment.getStartDate());
        }

        @Test
        @DisplayName("createNew - start date in future -> exception")
        void createNew_futureStartDate_throws() {
            assertThatThrownBy(() -> Treatment.createNew(
                    patientId,
                    dentistId,
                    serviceId,
                    LocalDate.now().plusDays(5),
                    LocalDate.now().plusDays(10),
                    List.of(samplePhase()),
                    "Notas",
                    rateId
            ))
            .isInstanceOf(BusinessRuleViolationException.class)
            .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
                    .isEqualTo(TreatmentError.ERR_TREATMENT_FUTURE_START_DATE));
        }

        @Test
        @DisplayName("createNew - expected end before start -> exception")
        void createNew_invalidEndDate_throws() {
            LocalDate start = LocalDate.now().minusDays(1);
            LocalDate end = start.minusDays(4);

            assertThatThrownBy(() -> Treatment.createNew(
                    patientId,
                    dentistId,
                    serviceId,
                    start,
                    end,
                    List.of(samplePhase()),
                    "Notas",
                    rateId
            ))
            .isInstanceOf(BusinessRuleViolationException.class)
            .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
                    .isEqualTo(TreatmentError.ERR_TREATMENT_INVALID_END_DATE));
        }

        @Test
        @DisplayName("createNew - no phases -> exception")
        void createNew_noPhases_throws() {
            assertThatThrownBy(() -> Treatment.createNew(
                    patientId,
                    dentistId,
                    serviceId,
                    LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(10),
                    List.of(),
                    "Notas",
                    rateId
            ))
            .isInstanceOf(BusinessRuleViolationException.class)
            .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
                    .isEqualTo(TreatmentError.ERR_TREATMENT_PHASES_REQUIRED));
        }
    }

    @Nested
    @DisplayName("Business operations")
    class BusinessOperationsTests {

        private Treatment activeTreatment() {
            return Treatment.createNew(
                    patientId,
                    dentistId,
                    serviceId,
                    LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(10),
                    List.of(samplePhase()),
                    "Notas",
                    rateId
            );
        }

        @Test
        @DisplayName("complete - success")
        void complete_success() {
            Treatment treatment = activeTreatment();
            LocalDate completionDate = LocalDate.now();

            treatment.complete(completionDate);

            assertThat(treatment.getStatus()).isEqualTo(TreatmentStatus.COMPLETED);
            assertThat(treatment.getActualEndDate()).isEqualTo(completionDate);
        }

        @Test
        @DisplayName("complete - invalid date before start -> exception")
        void complete_invalidDate_throws() {
            Treatment treatment = activeTreatment();
            LocalDate invalidDate = treatment.getStartDate().minusDays(1);

            assertThatThrownBy(() -> treatment.complete(invalidDate))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
                            .isEqualTo(TreatmentError.ERR_TREATMENT_INVALID_COMPLETION_DATE));
        }

        @Test
        @DisplayName("cancel - success")
        void cancel_success() {
            Treatment treatment = activeTreatment();
            treatment.cancel("Paciente no asistió a citas programadas");

            assertThat(treatment.getStatus()).isEqualTo(TreatmentStatus.CANCELLED);
        }

        @Test
        @DisplayName("cancel - reason too short -> exception")
        void cancel_invalidReason_throws() {
            Treatment treatment = activeTreatment();

            assertThatThrownBy(() -> treatment.cancel("Muy corto"))
    .isInstanceOf(BusinessRuleViolationException.class)
    .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
        .isEqualTo(TreatmentError.ERR_TREATMENT_CANCELLATION_REASON_REQUIRED));
        }
    }
}

