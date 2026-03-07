
package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.dental.care.service.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.AgeRange;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.PediatricError;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;


import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


class PediatricDetailsTest {

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear con datos válidos")
        void create_valid() {
            PediatricDetails details = new PediatricDetails(
                    AgeRange.of(5, 12),
                    "Tell-Show-Do",
                    "Resin-based sealants"
            );

            assertThat(details.getAgeRange()).isEqualTo(AgeRange.of(5, 12));
            assertThat(details.getBehaviorManagement()).isEqualTo("Tell-Show-Do");
            assertThat(details.getPediatricMaterials()).isEqualTo("Resin-based sealants");
            assertThat(details.serviceType()).isEqualTo(ServiceType.PEDIATRICS);
            assertThat(details.toString()).contains("5-12").contains("Resin-based sealants");
        }
    }

    @Nested
    @DisplayName("Validaciones de materiales")
    class MaterialsTests {

        @Test
        @DisplayName("materiales demasiado cortos -> excepción con catálogo correcto")
        void materials_tooShort_throws() {
            assertThatThrownBy(() -> new PediatricDetails(
                    AgeRange.of(5, 12),
                    "Tell-Show-Do",
                    "Res"
            ))
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
            PediatricDetails d1 = new PediatricDetails(AgeRange.of(5, 12), "Tell-Show-Do", "Sealants");
            PediatricDetails d2 = new PediatricDetails(AgeRange.of(5, 12), "Tell-Show-Do", "Sealants");

            assertThat(d1).isEqualTo(d2);
            assertThat(d1.hashCode()).isEqualTo(d2.hashCode());
        }

        @Test
        @DisplayName("instancias con atributos distintos no son iguales")
        void equals_differentAttributes() {
            PediatricDetails d1 = new PediatricDetails(AgeRange.of(5, 12), "Tell-Show-Do", "Sealants");
            PediatricDetails d2 = new PediatricDetails(AgeRange.of(6, 15), "Positive reinforcement", "Fluoride varnish");

            assertThat(d1).isNotEqualTo(d2);
        }
    }
}