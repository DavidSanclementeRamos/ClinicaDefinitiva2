
package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.dental.care.service.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ProstheticError;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ProstheticDetailsTest {

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear prótesis fija válida")
        void create_valid_fixed() {
            ProstheticDetails details = new ProstheticDetails(
                    "FIXED",
                    "Porcelain",
                    "Crown",
                    3
            );

            assertThat(details.getFixedOrRemovable()).isEqualTo("FIXED");
            assertThat(details.getMaterial()).isEqualTo("Porcelain");
            assertThat(details.getProstheticType()).isEqualTo("Crown");
            assertThat(details.getUnits()).isEqualTo(3);
            assertThat(details.serviceType()).isEqualTo(ServiceType.PROSTHETICS);
            assertThat(details.toString()).contains("FIXED").contains("Porcelain");
        }

        @Test
        @DisplayName("crear prótesis removible válida con unidades dentro del límite")
        void create_valid_removable() {
            ProstheticDetails details = new ProstheticDetails(
                    "REMOVABLE",
                    "Acrylic",
                    "Dentures",
                    14
            );

            assertThat(details.getFixedOrRemovable()).isEqualTo("REMOVABLE");
            assertThat(details.getUnits()).isEqualTo(14);
        }
    }

    @Nested
    @DisplayName("Validaciones de tipo")
    class TypeTests {

        @Test
        @DisplayName("tipo nulo o vacío -> excepción con catálogo correcto")
        void type_nullOrBlank_throws() {
            assertThatThrownBy(() -> new ProstheticDetails(null, "Porcelain", "Crown", 2))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ProstheticError.ERR_PROSTHETIC_MISSING_TYPE));

            assertThatThrownBy(() -> new ProstheticDetails("   ", "Porcelain", "Crown", 2))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ProstheticError.ERR_PROSTHETIC_MISSING_TYPE));
        }

        @Test
        @DisplayName("tipo inválido -> excepción con catálogo correcto")
        void type_invalid_throws() {
            assertThatThrownBy(() -> new ProstheticDetails("INVALID", "Porcelain", "Crown", 2))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ProstheticError.ERR_PROSTHETIC_INVALID_TYPE_VALUE));
        }
    }

    @Nested
    @DisplayName("Validaciones de unidades")
    class UnitsTests {

        @Test
        @DisplayName("unidades negativas -> excepción con catálogo correcto")
        void units_negative_throws() {
            assertThatThrownBy(() -> new ProstheticDetails("FIXED", "Porcelain", "Crown", -1))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ProstheticError.ERR_PROSTHETIC_INVALID_UNITS));
        }

        @Test
        @DisplayName("unidades excesivas en removible -> excepción con catálogo correcto")
        void units_excessiveRemovable_throws() {
            assertThatThrownBy(() -> new ProstheticDetails("REMOVABLE", "Acrylic", "Dentures", 20))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ProstheticError.ERR_PROSTHETIC_EXCESSIVE_UNITS));
        }
    }

    @Nested
    @DisplayName("Igualdad y hashCode")
    class EqualityTests {

        @Test
        @DisplayName("dos instancias con mismos atributos son iguales")
        void equals_sameAttributes() {
            ProstheticDetails d1 = new ProstheticDetails("FIXED", "Porcelain", "Crown", 2);
            ProstheticDetails d2 = new ProstheticDetails("FIXED", "Porcelain", "Crown", 2);

            assertThat(d1).isEqualTo(d2);
            assertThat(d1.hashCode()).isEqualTo(d2.hashCode());
        }

        @Test
        @DisplayName("instancias con atributos distintos no son iguales")
        void equals_differentAttributes() {
            ProstheticDetails d1 = new ProstheticDetails("FIXED", "Porcelain", "Crown", 2);
            ProstheticDetails d2 = new ProstheticDetails("REMOVABLE", "Acrylic", "Dentures", 10);

            assertThat(d1).isNotEqualTo(d2);
        }
    }
}
