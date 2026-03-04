
package com.example.ClinicaDefinitiva.domain.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AddressTest {

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear dirección válida")
        void create_valid() {
            Address address = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");

            assertThat(address.Street()).isEqualTo("Calle 123");
            assertThat(address.City()).isEqualTo("Cali");
            assertThat(address.State()).isEqualTo("Valle");
            assertThat(address.Country()).isEqualTo("Colombia");
            assertThat(address.PostalCode()).isEqualTo("760001");
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class ValidationTests {

        @Test
        @DisplayName("lanza excepción si algún campo es nulo")
        void nullField_throws() {
            assertThatThrownBy(() -> Address.of(null, "Cali", "Valle", "Colombia", "760001"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(VoActorError.ERR_ADDRESS_NULL));
        }

        @Test
        @DisplayName("lanza excepción si algún campo es vacío")
        void blankField_throws() {
            assertThatThrownBy(() -> Address.of(" ", "Cali", "Valle", "Colombia", "760001"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(VoActorError.ERR_ADDRESS_BLANK));
        }
    }

    @Nested
    @DisplayName("Métodos semánticos")
    class SemanticMethodsTests {

        @Test
        @DisplayName("isInCountry devuelve true si coincide país")
        void isInCountry_valid() {
            Address address = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");
            assertThat(address.isInCountry("colombia")).isTrue();
        }

        @Test
        @DisplayName("isLocalTo devuelve true si coincide ciudad")
        void isLocalTo_valid() {
            Address address = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");
            assertThat(address.isLocalTo("Cali")).isTrue();
        }
    }

    @Nested
    @DisplayName("Métodos utilitarios")
    class UtilityMethodsTests {

        @Test
        @DisplayName("fullAddress devuelve formato correcto")
        void fullAddress_valid() {
            Address address = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");
            assertThat(address.fullAddress()).isEqualTo("Calle 123, Cali, Valle, Colombia - 760001");
        }

        @Test
        @DisplayName("postalZone devuelve primeros 3 dígitos")
        void postalZone_valid() {
            Address address = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");
            assertThat(address.postalZone()).isEqualTo("760");
        }

        @Test
        @DisplayName("toString devuelve fullAddress")
        void toString_valid() {
            Address address = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");
            assertThat(address.toString()).isEqualTo(address.fullAddress());
        }
    }

    @Nested
    @DisplayName("Igualdad y hashCode")
    class EqualityTests {

        @Test
        @DisplayName("dos direcciones iguales son equals y tienen mismo hashCode")
        void equals_sameValues() {
            Address a1 = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");
            Address a2 = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");

            assertThat(a1).isEqualTo(a2);
            assertThat(a1.hashCode()).isEqualTo(a2.hashCode());
        }

        @Test
        @DisplayName("direcciones distintas no son equals")
        void equals_differentValues() {
            Address a1 = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");
            Address a2 = Address.of("Calle 456", "Bogotá", "Cundinamarca", "Colombia", "110111");

            assertThat(a1).isNotEqualTo(a2);
        }
    }
}