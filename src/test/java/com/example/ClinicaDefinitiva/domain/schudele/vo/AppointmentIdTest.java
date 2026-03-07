
package com.example.ClinicaDefinitiva.domain.schudele.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.ScheduleVOError;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AppointmentIdTest {

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear con valor válido")
        void create_valid() {
            AppointmentId id = AppointmentId.of(123L);

            assertThat(id.getValue()).isEqualTo(123L);
            assertThat(id.toString()).contains("123");
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class ValidationTests {

        @Test
        @DisplayName("valor nulo -> excepción con catálogo correcto")
        void value_null_throws() {
            assertThatThrownBy(() -> AppointmentId.of(null))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ScheduleVOError.ERR_APPOINTMENT_ID_REQUIRED));
        }
    }

    @Nested
    @DisplayName("Igualdad y hashCode")
    class EqualityTests {

        @Test
        @DisplayName("dos instancias con mismo valor son iguales")
        void equals_sameValue() {
            AppointmentId id1 = AppointmentId.of(123L);
            AppointmentId id2 = AppointmentId.of(123L);

            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("instancias con valores distintos no son iguales")
        void equals_differentValue() {
            AppointmentId id1 = AppointmentId.of(123L);
            AppointmentId id2 = AppointmentId.of(456L);

            assertThat(id1).isNotEqualTo(id2);
        }
    }
}
