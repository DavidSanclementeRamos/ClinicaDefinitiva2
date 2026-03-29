package com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo;

import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.PhaseStatus;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class TreatmentPhaseTest {

    @Test
    @DisplayName("Crear fase con fecha programada futura")
    void shouldCreateValidPhase() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        TreatmentPhase phase = TreatmentPhase.of(
                Name.of("Fase 1"),
                futureDate,
                PhaseStatus.PENDING,
                Notes.of("Notas")
        );

        assertThat(phase.getName().getValue()).isEqualTo("Fase 1");
        assertThat(phase.getScheduledDate()).isEqualTo(futureDate);
        assertThat(phase.getStatus()).isEqualTo(PhaseStatus.PENDING);
    }

    @Test
    @DisplayName("Crear fase con fecha pasada lanza excepción")
    void shouldThrowForPastDate() {
        LocalDate pastDate = LocalDate.now().minusDays(1);

        assertThatThrownBy(() -> TreatmentPhase.of(
                Name.of("Fase 1"),
                pastDate,
                PhaseStatus.PENDING,
                Notes.of("Notas")
        )).isInstanceOf(ValueObjectValidationException.class)
          .hasMessageContaining("La fecha de la fase del tratamiento es inválida");
    }
}
