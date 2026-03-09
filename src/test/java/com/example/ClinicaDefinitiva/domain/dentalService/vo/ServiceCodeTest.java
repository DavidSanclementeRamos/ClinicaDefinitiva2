
package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ServiceCodeTest {

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear con código válido")
        void create_valid() {
            ServiceCode code = ServiceCode.of("SERV-1234");

            assertThat(code).isNotNull();
            assertThat(code.getValue()).isEqualTo("SERV-1234");
            assertThat(code.toString()).isEqualTo("SERV-1234");
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class ValidationTests {

        @Test
        @DisplayName("código nulo o vacío -> excepción")
        void nullOrBlank_throws() {
            assertThatThrownBy(() -> ServiceCode.of(null))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_CODE_REQUIRED));

            assertThatThrownBy(() -> ServiceCode.of("   "))
                    .isInstanceOf(ValueObjectValidationException.class);
        }

        @Test
        @DisplayName("código con caracteres inválidos -> excepción")
        void invalidFormat_throws() {
            assertThatThrownBy(() -> ServiceCode.of("serv_123"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_CODE_FORMAT_INVALID));
        }

        @Test
        @DisplayName("código demasiado corto o largo -> excepción")
        void invalidLength_throws() {
            assertThatThrownBy(() -> ServiceCode.of("ABC")) // < 4
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException)ex).getCatalogo()) 
                            .isEqualTo(ServiceVOError.ERR_SERVICE_CODE_LENGTH_INVALID));

            assertThatThrownBy(() -> ServiceCode.of("ABCDEFGHIJKLMNOX")) // > 15
                    .isInstanceOf(ValueObjectValidationException.class);
        }
    }

    @Nested
    @DisplayName("Duplicados")
    class DuplicateTests {

        @Test
        @DisplayName("ensureUniqueCode lanza excepción si existe")
        void ensureUniqueCode_exists_throws() {
            ServiceCode code = ServiceCode.of("SERV-5678");

            assertThatThrownBy(() -> code.ensureUniqueCode(true))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_CODE_DUPLICATE));
        }

        @Test
        @DisplayName("ensureUniqueCode no lanza excepción si no existe")
        void ensureUniqueCode_notExists_ok() {
            ServiceCode code = ServiceCode.of("SERV-5678");

            assertThatCode(() -> code.ensureUniqueCode(false))
                    .doesNotThrowAnyException();
        }
    }
}