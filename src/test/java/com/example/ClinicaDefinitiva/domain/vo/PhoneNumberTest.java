
package com.example.ClinicaDefinitiva.domain.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PhoneNumberTest {

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear número válido nacional")
        void create_validLocal() {
            PhoneNumber phone = PhoneNumber.of("3001234567");

            assertThat(phone.Value()).isEqualTo("3001234567");
            assertThat(phone.isInternational()).isFalse();
        }

        @Test
        @DisplayName("crear número válido internacional")
        void create_validInternational() {
            PhoneNumber phone = PhoneNumber.of("+573001234567");

            assertThat(phone.Value()).isEqualTo("+573001234567");
            assertThat(phone.isInternational()).isTrue();
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class ValidationTests {

        @Test
        @DisplayName("lanza excepción si es nulo")
        void nullPhone_throws() {
            assertThatThrownBy(() -> PhoneNumber.of(null))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(VoActorError.ERR_PHONE_NULL));
        }

        @Test
        @DisplayName("lanza excepción si es vacío")
        void blankPhone_throws() {
            assertThatThrownBy(() -> PhoneNumber.of("   "))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(VoActorError.ERR_PHONE_BLANK));
        }

        @Test
        @DisplayName("lanza excepción si formato inválido")
        void invalidFormat_throws() {
            assertThatThrownBy(() -> PhoneNumber.of("abc123"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(VoActorError.ERR_PHONE_INVALID_FORMAT));
        }
    }

    @Nested
    @DisplayName("Métodos semánticos")
    class SemanticMethodsTests {

        @Test
        @DisplayName("isInternational devuelve true si empieza con +")
        void isInternational_valid() {
            PhoneNumber phone = PhoneNumber.of("+573001234567");
            assertThat(phone.isInternational()).isTrue();
        }

        @Test
        @DisplayName("isLocalTo devuelve true si coincide prefijo")
        void isLocalTo_valid() {
            PhoneNumber phone = PhoneNumber.of("+573001234567");
            assertThat(phone.isLocalTo("+57")).isTrue();
        }
    }

    @Nested
    @DisplayName("Métodos utilitarios")
    class UtilityMethodsTests {

        @Test
        @DisplayName("masked oculta todos menos últimos 4 dígitos")
        void masked_valid() {
            PhoneNumber phone = PhoneNumber.of("3001234567");
            assertThat(phone.masked()).isEqualTo("***4567");
        }

        @Test
        @DisplayName("asText devuelve el valor original")
        void asText_valid() {
            PhoneNumber phone = PhoneNumber.of("3001234567");
            assertThat(phone.asText()).isEqualTo("3001234567");
        }

        @Test
        @DisplayName("toString devuelve el valor original")
        void toString_valid() {
            PhoneNumber phone = PhoneNumber.of("3001234567");
            assertThat(phone.toString()).isEqualTo("3001234567");
        }
    }

    @Nested
    @DisplayName("Igualdad y hashCode")
    class EqualityTests {

        @Test
        @DisplayName("dos números iguales son equals y tienen mismo hashCode")
        void equals_sameValues() {
            PhoneNumber p1 = PhoneNumber.of("3001234567");
            PhoneNumber p2 = PhoneNumber.of("3001234567");

            assertThat(p1).isEqualTo(p2);
            assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
        }

        @Test
        @DisplayName("números distintos no son equals")
        void equals_differentValues() {
            PhoneNumber p1 = PhoneNumber.of("3001234567");
            PhoneNumber p2 = PhoneNumber.of("3007654321");

            assertThat(p1).isNotEqualTo(p2);
        }
    }
}
