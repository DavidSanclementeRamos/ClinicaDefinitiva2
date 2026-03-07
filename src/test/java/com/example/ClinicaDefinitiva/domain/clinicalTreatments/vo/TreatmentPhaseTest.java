
package com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo;

import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.PhaseStatus;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TreatmentPhaseTest {

    @Test
    void shouldCreateTreatmentPhaseSuccessfully() {
        Name name = Name.of("Fase inicial");
        Notes notes = Notes.of("Notas de la fase");
        LocalDate scheduledDate = LocalDate.now().plusDays(5);
        PhaseStatus status = PhaseStatus.COMPLETED;

        TreatmentPhase phase = TreatmentPhase.of(name, scheduledDate, status, notes);

        assertEquals(name, phase.getName());
        assertEquals(scheduledDate, phase.getScheduledDate());
        assertEquals(status, phase.getStatus());
        assertEquals(notes, phase.getNotes());
    }

    @Test
    void shouldThrowExceptionWhenScheduledDateIsPast() {
        Name name = Name.of("Fase inválida");
        Notes notes = Notes.of("Notas");
        LocalDate scheduledDate = LocalDate.now().minusDays(1);
        PhaseStatus status = PhaseStatus.IN_PROGRESS;

        assertThrows(ValueObjectValidationException.class,
                () -> TreatmentPhase.of(name, scheduledDate, status, notes));
    }

    @Test
    void shouldReturnTrueWhenScheduledInFuture() {
        TreatmentPhase phase = TreatmentPhase.of(
                Name.of("Fase futura"),
                LocalDate.now().plusDays(10),
                PhaseStatus.IN_PROGRESS,
                Notes.of("Notas")
        );

        assertTrue(phase.getScheduledDate().isAfter(LocalDate.now()));
    }

    @Test
    void shouldReturnFalseWhenScheduledDateIsToday() {
        TreatmentPhase phase = TreatmentPhase.of(
                Name.of("Fase hoy"),
                LocalDate.now(),
                PhaseStatus.COMPLETED,
                Notes.of("Notas")
        );

        assertFalse(phase.getScheduledDate().isAfter(LocalDate.now()));
    }
}
