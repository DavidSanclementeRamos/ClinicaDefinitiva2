
package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ServiceDescriptionTest {

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear con descripción válida")
        void create_valid() {
            String text = "This is a valid service description with more than twenty characters.";
            ServiceDescription description = ServiceDescription.of(text);

            assertThat(description).isNotNull();
            assertThat(description.getValue()).isEqualTo(text.trim());
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class ValidationTests {

        @Test
        @DisplayName("crear con descripción nula -> excepción con catálogo correcto")
        void create_null_throws() {
            assertThatThrownBy(() -> ServiceDescription.of(null))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_DESCRIPTION_INVALID));
        }

        @Test
        @DisplayName("crear con descripción demasiado corta -> excepción con catálogo correcto")
        void create_tooShort_throws() {
            assertThatThrownBy(() -> ServiceDescription.of("Too short"))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_DESCRIPTION_INVALID));
        }

        @Test
        @DisplayName("crear con descripción en blanco -> excepción con catálogo correcto")
        void create_blank_throws() {
            assertThatThrownBy(() -> ServiceDescription.of("     "))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_DESCRIPTION_INVALID));
        }
    }
}
 
