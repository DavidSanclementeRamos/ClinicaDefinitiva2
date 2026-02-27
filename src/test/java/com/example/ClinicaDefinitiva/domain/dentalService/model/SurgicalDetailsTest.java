
package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.dental.care.service.model.SurgicalDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.service.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.SurgicalError;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SurgicalDetailsTest {

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear cirugía válida de complejidad media")
        void create_valid_medium() {
            SurgicalDetails details = new SurgicalDetails(
                    "Extraction",
                    "MEDIUM",
                    true,
                    false
            );

            assertThat(details.getSurgeryType()).isEqualTo("Extraction");
            assertThat(details.getComplexityLevel()).isEqualTo("MEDIUM");
            assertThat(details.getRequiresAnesthesia()).isTrue();
            assertThat(details.getOperatingRoomNeeded()).isFalse();
            assertThat(details.serviceType()).isEqualTo(ServiceType.SURGERY);
            assertThat(details.toString()).contains("Extraction").contains("MEDIUM");
        }
    }

    @Nested
    @DisplayName("Validaciones de tipo de cirugía")
    class SurgeryTypeTests {

        @Test
        @DisplayName("tipo demasiado corto -> excepción con catálogo correcto")
        void surgeryType_tooShort_throws() {
            assertThatThrownBy(() -> new SurgicalDetails("AB", "MEDIUM", true, false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(SurgicalError.ERR_SURGICAL_TYPE_TOO_SHORT));
        }
    }

    @Nested
    @DisplayName("Validaciones de complejidad")
    class ComplexityTests {

        @Test
        @DisplayName("nivel de complejidad inválido -> excepción con catálogo correcto")
        void complexity_invalid_throws() {
            assertThatThrownBy(() -> new SurgicalDetails("Extraction", "INVALID", true, false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(SurgicalError.ERR_SURGICAL_INVALID_COMPLEXITY));
        }

        @Test
        @DisplayName("baja complejidad con anestesia -> excepción con catálogo correcto")
        void lowComplexity_withAnesthesia_throws() {
            assertThatThrownBy(() -> new SurgicalDetails("Simple extraction", "LOW", true, false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(SurgicalError.ERR_SURGICAL_ANESTHESIA_COMPLEXITY_MISMATCH));
        }

        @Test
        @DisplayName("baja complejidad con quirófano -> excepción con catálogo correcto")
        void lowComplexity_withOperatingRoom_throws() {
            assertThatThrownBy(() -> new SurgicalDetails("Simple extraction", "LOW", false, true))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(SurgicalError.ERR_SURGICAL_OPERATING_ROOM_COMPLEXITY_MISMATCH));
        }

        @Test
        @DisplayName("crítica sin anestesia o quirófano -> excepción con catálogo correcto")
        void critical_missingRequirements_throws() {
            assertThatThrownBy(() -> new SurgicalDetails("Complex surgery", "CRITICAL", false, true))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(SurgicalError.ERR_SURGICAL_CRITICAL_MISSING_REQUIREMENTS));

            assertThatThrownBy(() -> new SurgicalDetails("Complex surgery", "CRITICAL", true, false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(SurgicalError.ERR_SURGICAL_CRITICAL_MISSING_REQUIREMENTS));
        }
    }

    @Nested
    @DisplayName("Igualdad y hashCode")
    class EqualityTests {

        @Test
        @DisplayName("dos instancias con mismos atributos son iguales")
        void equals_sameAttributes() {
            SurgicalDetails d1 = new SurgicalDetails("Extraction", "MEDIUM", true, false);
            SurgicalDetails d2 = new SurgicalDetails("Extraction", "MEDIUM", true, false);

            assertThat(d1).isEqualTo(d2);
            assertThat(d1.hashCode()).isEqualTo(d2.hashCode());
        }

        @Test
        @DisplayName("instancias con atributos distintos no son iguales")
        void equals_differentAttributes() {
            SurgicalDetails d1 = new SurgicalDetails("Extraction", "MEDIUM", true, false);
            SurgicalDetails d2 = new SurgicalDetails("Implant", "HIGH", true, true);

            assertThat(d1).isNotEqualTo(d2);
        }
    }
}
