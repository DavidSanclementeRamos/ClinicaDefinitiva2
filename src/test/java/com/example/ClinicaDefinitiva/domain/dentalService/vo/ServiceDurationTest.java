
package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class ServiceDurationTest {

    @Nested
    @DisplayName("Creación")
    class CreationTests {

        @Test
        @DisplayName("crear con minutos válidos")
        void create_validMinutes() {
            ServiceDuration duration = ServiceDuration.of(60);
            assertThat(duration.getMinutes()).isEqualTo(60);
            assertThat(duration.getHours()).isEqualTo(1);
            assertThat(duration.getRemainingMinutes()).isEqualTo(0);
            assertThat(duration.toReadableFormat()).isEqualTo("1h");
        }

        @Test
        @DisplayName("crear con minutos inválidos -> excepción con catálogo correcto")
        void create_invalidMinutes_throws() {
            assertThatThrownBy(() -> ServiceDuration.of(0))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_DURATION_POSITIVE));

            assertThatThrownBy(() -> ServiceDuration.of(10))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_DURATION_MINIMUM));

            assertThatThrownBy(() -> ServiceDuration.of(1000))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_DURATION_MAXIMUM));
        }

        @Test
        @DisplayName("crear desde horas")
        void create_fromHours() {
            ServiceDuration duration = ServiceDuration.ofHours(2);
            assertThat(duration.getMinutes()).isEqualTo(120);
            assertThat(duration.toReadableFormat()).isEqualTo("2h");
        }

        @Test
        @DisplayName("crear entre dos fechas válidas")
        void create_betweenDates() {
            LocalDateTime start = LocalDateTime.of(2026, 2, 25, 10, 0);
            LocalDateTime end = LocalDateTime.of(2026, 2, 25, 11, 30);

            ServiceDuration duration = ServiceDuration.between(start, end);
            assertThat(duration.getMinutes()).isEqualTo(90);
            assertThat(duration.toReadableFormat()).isEqualTo("1h 30m");
        }

        @Test
        @DisplayName("crear entre fechas inválidas -> excepción con catálogo correcto")
        void create_betweenDates_invalid() {
            LocalDateTime start = LocalDateTime.of(2026, 2, 25, 11, 0);
            LocalDateTime end = LocalDateTime.of(2026, 2, 25, 10, 0);

            assertThatThrownBy(() -> ServiceDuration.between(start, end))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_DURATION_START_BEFORE_END));
        }

        @Test
        @DisplayName("crear desde Duration nulo -> excepción con catálogo correcto")
        void create_fromDuration_null() {
            assertThatThrownBy(() -> ServiceDuration.from(null))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_DURATION_REQUIRED));
        }
    }

    @Nested
    @DisplayName("Operaciones")
    class OperationTests {

        @Test
        @DisplayName("sumar duraciones")
        void plus_valid() {
            ServiceDuration d1 = ServiceDuration.of(60);
            ServiceDuration d2 = ServiceDuration.of(30);

            ServiceDuration result = d1.plus(d2);
            assertThat(result.getMinutes()).isEqualTo(90);
        }

        @Test
        @DisplayName("restar duraciones válidas")
        void minus_valid() {
            ServiceDuration d1 = ServiceDuration.of(90);
            ServiceDuration d2 = ServiceDuration.of(30);

            ServiceDuration result = d1.minus(d2);
            assertThat(result.getMinutes()).isEqualTo(60);
        }

        @Test
        @DisplayName("restar duraciones inválidas -> excepción con catálogo correcto")
        void minus_invalid() {
            ServiceDuration d1 = ServiceDuration.of(30);
            ServiceDuration d2 = ServiceDuration.of(60);

            assertThatThrownBy(() -> d1.minus(d2))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_DURATION_RESULT_POSITIVE));
        }

        @Test
        @DisplayName("multiplicar duración válida")
        void multiply_valid() {
            ServiceDuration d1 = ServiceDuration.of(30);
            ServiceDuration result = d1.multiply(2);

            assertThat(result.getMinutes()).isEqualTo(60);
        }

        @Test
        @DisplayName("multiplicar con factor inválido -> excepción con catálogo correcto")
        void multiply_invalid() {
            ServiceDuration d1 = ServiceDuration.of(30);

            assertThatThrownBy(() -> d1.multiply(0))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_DURATION_FACTOR_POSITIVE));
        }
    }

    @Nested
    @DisplayName("Queries")
    class QueryTests {

        @Test
        @DisplayName("isShort, isLong, isStandardSlot")
        void queries() {
            ServiceDuration shortDuration = ServiceDuration.of(20);
            ServiceDuration longDuration = ServiceDuration.of(180);

            assertThat(shortDuration.isShort()).isTrue();
            assertThat(longDuration.isLong()).isTrue();
            assertThat(ServiceDuration.of(45).isStandardSlot()).isTrue();
        }

        @Test
        @DisplayName("comparaciones entre duraciones")
        void comparisons() {
            ServiceDuration d1 = ServiceDuration.of(60);
            ServiceDuration d2 = ServiceDuration.of(90);

            assertThat(d2.isLongerThan(d1)).isTrue();
            assertThat(d1.isShorterThan(d2)).isTrue();
            assertThat(d1.isEqualTo(ServiceDuration.of(60))).isTrue();
        }
    }
}
