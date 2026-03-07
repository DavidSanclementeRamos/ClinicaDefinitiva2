
package com.example.ClinicaDefinitiva.domain.schudele.vo;

import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.AppointmentError;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentCompletion;


import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AppointmentCompletionTest {

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear con duración y notas válidas")
        void create_valid() {
            ServiceDuration duration = ServiceDuration.of(30);
            AppointmentCompletion completion = new AppointmentCompletion(duration, "Paciente toleró bien el procedimiento");

            assertThat(completion.getActualDuration()).isEqualTo(duration);
            assertThat(completion.getClinicalNotes()).isEqualTo("Paciente toleró bien el procedimiento");
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class ValidationTests {

        @Test
        @DisplayName("duración nula -> excepción con catálogo correcto")
        void duration_null_throws() {
            assertThatThrownBy(() -> new AppointmentCompletion(null, "Notas válidas"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(AppointmentError.ERR_APPT_INCOMPLETE_COMPLETION));
        }

        @Test
        @DisplayName("notas clínicas nulas o vacías -> excepción con catálogo correcto")
        void notes_nullOrBlank_throws() {
            ServiceDuration duration = ServiceDuration.of(30);

            assertThatThrownBy(() -> new AppointmentCompletion(duration, null))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(AppointmentError.ERR_APPT_INCOMPLETE_COMPLETION));

            assertThatThrownBy(() -> new AppointmentCompletion(duration, "   "))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(AppointmentError.ERR_APPT_INCOMPLETE_COMPLETION));
        }
    }
}