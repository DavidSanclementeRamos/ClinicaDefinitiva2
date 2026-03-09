
package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.ImplantologyError;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ImplantologyDetailsTest {

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear con datos válidos sin injerto")
        void create_valid_noGraft() {
            ImplantologyDetails details = new ImplantologyDetails(
                    6,
                    "Titanium",
                    "Upper jaw",
                    false
            );

            assertThat(details.getHealingTimeMonths()).isEqualTo(6);
            assertThat(details.getImplantType()).isEqualTo("Titanium");
            assertThat(details.getPlacementSite()).isEqualTo("Upper jaw");
            assertThat(details.getRequiresBoneGraft()).isFalse();
            assertThat(details.serviceType()).isEqualTo(ServiceType.IMPLANTOLOGY);
            assertThat(details.toString()).contains("Titanium").contains("Upper jaw");
        }

        @Test
        @DisplayName("crear con datos válidos con injerto")
        void create_valid_withGraft() {
            ImplantologyDetails details = new ImplantologyDetails(
                    6,
                    "Zirconia",
                    "Lower jaw",
                    true
            );

            assertThat(details.getRequiresBoneGraft()).isTrue();
            assertThat(details.getHealingTimeMonths()).isEqualTo(6);
        }
    }

    @Nested
    @DisplayName("Validaciones de cicatrización")
    class HealingTimeTests {

        @Test
        @DisplayName("tiempo negativo -> excepción con catálogo correcto")
        void healingTime_negative_throws() {
            assertThatThrownBy(() -> new ImplantologyDetails(-1, "Titanium", "Upper jaw", false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ImplantologyError.ERR_IMPLANTOLOGY_NEGATIVE_HEALING_TIME));
        }

        @Test
        @DisplayName("tiempo fuera de rango -> excepción con catálogo correcto")
        void healingTime_outOfRange_throws() {
            assertThatThrownBy(() -> new ImplantologyDetails(1, "Titanium", "Upper jaw", false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ImplantologyError.ERR_IMPLANTOLOGY_INVALID_HEALING_TIME));

            assertThatThrownBy(() -> new ImplantologyDetails(20, "Titanium", "Upper jaw", false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ImplantologyError.ERR_IMPLANTOLOGY_INVALID_HEALING_TIME));
        }

        @Test
        @DisplayName("injerto óseo con tiempo demasiado corto -> excepción con catálogo correcto")
        void healingTime_withGraftTooShort_throws() {
            assertThatThrownBy(() -> new ImplantologyDetails(2, "Titanium", "Upper jaw", true))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ImplantologyError.ERR_IMPLANTOLOGY_BONE_GRAFT_HEALING_MISMATCH));
        }
    }

    @Nested
    @DisplayName("Validaciones de sitio de colocación")
    class PlacementSiteTests {

        @Test
        @DisplayName("sitio demasiado corto -> excepción con catálogo correcto")
        void placementSite_tooShort_throws() {
            assertThatThrownBy(() -> new ImplantologyDetails(6, "Titanium", "A", false))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ImplantologyError.ERR_IMPLANTOLOGY_INVALID_PLACEMENT_SITE));
        }
    }

    @Nested
    @DisplayName("Igualdad y hashCode")
    class EqualityTests {

        @Test
        @DisplayName("dos instancias con mismos atributos son iguales")
        void equals_sameAttributes() {
            ImplantologyDetails d1 = new ImplantologyDetails(6, "Titanium", "Upper jaw", false);
            ImplantologyDetails d2 = new ImplantologyDetails(6, "Titanium", "Upper jaw", false);

            assertThat(d1).isEqualTo(d2);
            assertThat(d1.hashCode()).isEqualTo(d2.hashCode());
        }

        @Test
        @DisplayName("instancias con atributos distintos no son iguales")
        void equals_differentAttributes() {
            ImplantologyDetails d1 = new ImplantologyDetails(6, "Titanium", "Upper jaw", false);
            ImplantologyDetails d2 = new ImplantologyDetails(6, "Zirconia", "Lower jaw", true);

            assertThat(d1).isNotEqualTo(d2);
        }
    }
}
