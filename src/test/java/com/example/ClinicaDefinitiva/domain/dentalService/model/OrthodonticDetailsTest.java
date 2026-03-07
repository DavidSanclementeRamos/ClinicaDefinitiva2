
package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.dental.care.service.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.OrthodonticError;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OrthodonticDetailsTest {

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear con datos válidos")
        void create_valid() {
            OrthodonticDetails details = new OrthodonticDetails(
                    "METAL_BRACKETS",
                    24,
                    true
            );

            assertThat(details.getApplianceType()).isEqualTo("METAL_BRACKETS");
            assertThat(details.getTreatmentDurationMonths()).isEqualTo(24);
            assertThat(details.getRequiresFollowup()).isTrue();
            assertThat(details.serviceType()).isEqualTo(ServiceType.ORTHODONTIC);
            assertThat(details.toString()).contains("METAL_BRACKETS").contains("24 meses");
        }
    }

    @Nested
    @DisplayName("Validaciones de tipo de aparato")
    class ApplianceTypeTests {

        @Test
        @DisplayName("tipo nulo o vacío -> excepción con catálogo correcto")
        void applianceType_nullOrBlank_throws() {
            assertThatThrownBy(() -> new OrthodonticDetails(null, 12, false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(OrthodonticError.ERR_ORTHODONTIC_MISSING_APPLIANCE));

            assertThatThrownBy(() -> new OrthodonticDetails("   ", 12, false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(OrthodonticError.ERR_ORTHODONTIC_MISSING_APPLIANCE));
        }

        @Test
        @DisplayName("tipo inválido -> excepción con catálogo correcto")
        void applianceType_invalid_throws() {
            assertThatThrownBy(() -> new OrthodonticDetails("INVALID_TYPE", 12, false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(OrthodonticError.ERR_ORTHODONTIC_INVALID_APPLIANCE));
        }
    }

    @Nested
    @DisplayName("Validaciones de duración")
    class DurationTests {

        @Test
        @DisplayName("duración negativa o cero -> excepción con catálogo correcto")
        void duration_negativeOrZero_throws() {
            assertThatThrownBy(() -> new OrthodonticDetails("METAL_BRACKETS", 0, false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(OrthodonticError.ERR_ORTHODONTIC_NEGATIVE_DURATION));

            assertThatThrownBy(() -> new OrthodonticDetails("METAL_BRACKETS", -5, false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(OrthodonticError.ERR_ORTHODONTIC_NEGATIVE_DURATION));
        }

        @Test
        @DisplayName("duración fuera de rango -> excepción con catálogo correcto")
        void duration_outOfRange_throws() {
            assertThatThrownBy(() -> new OrthodonticDetails("METAL_BRACKETS", 3, false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(OrthodonticError.ERR_ORTHODONTIC_INVALID_DURATION));

            assertThatThrownBy(() -> new OrthodonticDetails("METAL_BRACKETS", 60, false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(OrthodonticError.ERR_ORTHODONTIC_INVALID_DURATION));
        }
    }

    @Nested
    @DisplayName("Igualdad y hashCode")
    class EqualityTests {

        @Test
        @DisplayName("dos instancias con mismos atributos son iguales")
        void equals_sameAttributes() {
            OrthodonticDetails d1 = new OrthodonticDetails("METAL_BRACKETS", 24, true);
            OrthodonticDetails d2 = new OrthodonticDetails("METAL_BRACKETS", 24, true);

            assertThat(d1).isEqualTo(d2);
            assertThat(d1.hashCode()).isEqualTo(d2.hashCode());
        }

        @Test
        @DisplayName("instancias con atributos distintos no son iguales")
        void equals_differentAttributes() {
            OrthodonticDetails d1 = new OrthodonticDetails("METAL_BRACKETS", 24, true);
            OrthodonticDetails d2 = new OrthodonticDetails("CERAMIC_BRACKETS", 18, false);

            assertThat(d1).isNotEqualTo(d2);
        }
    }
}
