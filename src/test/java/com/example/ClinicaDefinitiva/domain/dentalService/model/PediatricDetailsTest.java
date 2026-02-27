
package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.dental.care.service.model.PediatricDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.service.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.PediatricError;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PediatricDetailsTest {

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear con datos válidos")
        void create_valid() {
            PediatricDetails details = new PediatricDetails(
                    "5-12 años",
                    "Tell-Show-Do",
                    "Resin-based sealants"
            );

            assertThat(details.getAgeRange()).isEqualTo("5-12 años");
            assertThat(details.getBehaviorManagement()).isEqualTo("Tell-Show-Do");
            assertThat(details.getPediatricMaterials()).isEqualTo("Resin-based sealants");
            assertThat(details.serviceType()).isEqualTo(ServiceType.PEDIATRICS);
            assertThat(details.toString()).contains("5-12 años").contains("Resin-based sealants");
        }
    }

    @Nested
    @DisplayName("Validaciones de rango de edad")
    class AgeRangeTests {

        @Test
        @DisplayName("rango demasiado corto -> excepción con catálogo correcto")
        void ageRange_tooShort_throws() {
            assertThatThrownBy(() -> new PediatricDetails("12", "Tell-Show-Do", "Sealants"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(PediatricError.ERR_PEDIATRIC_AGE_RANGE_TOO_SHORT));
        }

        @Test
        @DisplayName("rango inválido (adultos) -> excepción con catálogo correcto")
        void ageRange_invalidAdults_throws() {
            assertThatThrownBy(() -> new PediatricDetails("19-25 años", "Tell-Show-Do", "Sealants"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(PediatricError.ERR_PEDIATRIC_INVALID_AGE_RANGE));

            assertThatThrownBy(() -> new PediatricDetails("20+", "Tell-Show-Do", "Sealants"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(PediatricError.ERR_PEDIATRIC_INVALID_AGE_RANGE));
        }

        @Test
        @DisplayName("rango sin números -> excepción con catálogo correcto")
        void ageRange_noNumbers_throws() {
            assertThatThrownBy(() -> new PediatricDetails("infantes", "Tell-Show-Do", "Sealants"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(PediatricError.ERR_PEDIATRIC_INVALID_AGE_RANGE));
        }
    }

    @Nested
    @DisplayName("Validaciones de materiales")
    class MaterialsTests {

        @Test
        @DisplayName("materiales demasiado cortos -> excepción con catálogo correcto")
        void materials_tooShort_throws() {
            assertThatThrownBy(() -> new PediatricDetails("5-12 años", "Tell-Show-Do", "Res"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(PediatricError.ERR_PEDIATRIC_MATERIALS_TOO_SHORT));
        }
    }

    @Nested
    @DisplayName("Igualdad y hashCode")
    class EqualityTests {

        @Test
        @DisplayName("dos instancias con mismos atributos son iguales")
        void equals_sameAttributes() {
            PediatricDetails d1 = new PediatricDetails("5-12 años", "Tell-Show-Do", "Sealants");
            PediatricDetails d2 = new PediatricDetails("5-12 años", "Tell-Show-Do", "Sealants");

            assertThat(d1).isEqualTo(d2);
            assertThat(d1.hashCode()).isEqualTo(d2.hashCode());
        }

        @Test
        @DisplayName("instancias con atributos distintos no son iguales")
        void equals_differentAttributes() {
            PediatricDetails d1 = new PediatricDetails("5-12 años", "Tell-Show-Do", "Sealants");
            PediatricDetails d2 = new PediatricDetails("6-15 años", "Positive reinforcement", "Fluoride varnish");

            assertThat(d1).isNotEqualTo(d2);
        }
    }
}
