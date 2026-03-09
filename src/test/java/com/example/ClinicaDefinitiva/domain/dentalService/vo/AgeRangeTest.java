
package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AgeRangeTest {

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear rango válido")
        void create_valid() {
            AgeRange range = AgeRange.of(5, 12);

            assertThat(range.getMinAge()).isEqualTo(5);
            assertThat(range.getMaxAge()).isEqualTo(12);
            assertThat(range.toString()).isEqualTo("5-12 años");
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class ValidationTests {

        @Test
        @DisplayName("edad mínima negativa -> excepción con catálogo correcto")
        void minAge_negative_throws() {
            assertThatThrownBy(() -> AgeRange.of(-1, 10))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_INVALID_MIN_AGE));
        }

        @Test
        @DisplayName("edad máxima menor o igual a mínima -> excepción con catálogo correcto")
        void maxAge_invalidRange_throws() {
            assertThatThrownBy(() -> AgeRange.of(5, 5))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_INVALID_MIN_AGE));

            assertThatThrownBy(() -> AgeRange.of(10, 8))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_INVALID_MIN_AGE));
        }
    }

    @Nested
    @DisplayName("Igualdad y hashCode")
    class EqualityTests {

        @Test
        @DisplayName("dos rangos con mismos valores son iguales")
        void equals_sameValues() {
            AgeRange r1 = AgeRange.of(5, 12);
            AgeRange r2 = AgeRange.of(5, 12);

            assertThat(r1).isEqualTo(r2);
            assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
        }

        @Test
        @DisplayName("rangos con valores distintos no son iguales")
        void equals_differentValues() {
            AgeRange r1 = AgeRange.of(5, 12);
            AgeRange r2 = AgeRange.of(6, 15);

            assertThat(r1).isNotEqualTo(r2);
        }
    }
}