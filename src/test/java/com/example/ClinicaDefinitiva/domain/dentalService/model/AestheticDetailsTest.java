
package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.dental.care.service.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.AestheticError;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AestheticDetailsTest {

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear con datos válidos")
        void create_valid() {
            AestheticDetails details = new AestheticDetails(
                    "WHITENING",
                    "Laser",
                    "Whiter teeth with natural look"
            );

            assertThat(details.getAestheticType()).isEqualTo("WHITENING");
            assertThat(details.getMaterialUsed()).isEqualTo("Laser");
            assertThat(details.getExpectedResult()).isEqualTo("Whiter teeth with natural look");
            assertThat(details.serviceType()).isEqualTo(ServiceType.AESTHETICS);
            assertThat(details.toString()).contains("WHITENING").contains("Laser");
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class ValidationTests {

        @Test
        @DisplayName("tipo nulo o vacío -> excepción con catálogo correcto")
        void type_nullOrBlank_throws() {
            assertThatThrownBy(() -> new AestheticDetails(null, "Laser", "Valid expected result"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(AestheticError.ERR_AESTHETIC_MISSING_TYPE));

            assertThatThrownBy(() -> new AestheticDetails("   ", "Laser", "Valid expected result"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(AestheticError.ERR_AESTHETIC_MISSING_TYPE));
        }

        @Test
        @DisplayName("tipo demasiado corto -> excepción con catálogo correcto")
        void type_tooShort_throws() {
            assertThatThrownBy(() -> new AestheticDetails("AB", "Laser", "Valid expected result"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(AestheticError.ERR_AESTHETIC_TYPE_TOO_SHORT));
        }

        @Test
        @DisplayName("tipo inválido -> excepción con catálogo correcto")
        void type_invalid_throws() {
            assertThatThrownBy(() -> new AestheticDetails("INVALID_TYPE", "Laser", "Valid expected result"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(AestheticError.ERR_AESTHETIC_INVALID_TYPE));
        }

        @Test
        @DisplayName("resultado esperado demasiado corto -> excepción con catálogo correcto")
        void expectedResult_tooShort_throws() {
            assertThatThrownBy(() -> new AestheticDetails("WHITENING", "Laser", "Too short"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(AestheticError.ERR_AESTHETIC_RESULT_TOO_SHORT));
        }
    }

    @Nested
    @DisplayName("Igualdad y hashCode")
    class EqualityTests {

        @Test
        @DisplayName("dos instancias con mismos atributos son iguales")
        void equals_sameAttributes() {
            AestheticDetails d1 = new AestheticDetails("WHITENING", "Laser", "Whiter teeth with natural look");
            AestheticDetails d2 = new AestheticDetails("WHITENING", "Laser", "Whiter teeth with natural look");

            assertThat(d1).isEqualTo(d2);
            assertThat(d1.hashCode()).isEqualTo(d2.hashCode());
        }

        @Test
        @DisplayName("instancias con atributos distintos no son iguales")
        void equals_differentAttributes() {
            AestheticDetails d1 = new AestheticDetails("WHITENING", "Laser", "Whiter teeth with natural look");
            AestheticDetails d2 = new AestheticDetails("VENEER", "Porcelain", "Natural smile");

            assertThat(d1).isNotEqualTo(d2);
        }
    }
}
