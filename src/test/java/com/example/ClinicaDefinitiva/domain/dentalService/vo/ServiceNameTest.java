
package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ServiceNameTest {

    @Nested
    @DisplayName("Creación con valores predefinidos")
    class PredefinedTests {

        @Test
        @DisplayName("crear con valor predefinido CLEANING")
        void create_predefined_cleaning() {
            ServiceName name = ServiceName.of(ServiceName.DentalServiceName.CLEANING);

            assertThat(name).isNotNull();
            assertThat(name.getValue()).isEqualTo("CLEANING");
        }
    }

    @Nested
    @DisplayName("Creación personalizada")
    class CustomTests {

        @Test
        @DisplayName("crear con nombre válido")
        void create_custom_valid() {
            ServiceName name = ServiceName.custom("Laser Whitening");

            assertThat(name).isNotNull();
            assertThat(name.getValue()).isEqualTo("Laser Whitening");
        }

        @Test
        @DisplayName("crear con nombre nulo o muy corto -> excepción")
        void create_custom_invalid() {
            assertThatThrownBy(() -> ServiceName.custom(null))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_NAME_CUSTOM_INVALID));

            assertThatThrownBy(() -> ServiceName.custom("  "))
                    .isInstanceOf(ValueObjectValidationException.class);

            assertThatThrownBy(() -> ServiceName.custom("AB"))
                    .isInstanceOf(ValueObjectValidationException.class);
        }
    }

    @Nested
    @DisplayName("Igualdad y hashCode")
    class EqualityTests {

        @Test
        @DisplayName("dos instancias con mismo valor son iguales")
        void equals_sameValue() {
            ServiceName n1 = ServiceName.custom("Laser Whitening");
            ServiceName n2 = ServiceName.custom("Laser Whitening");

            assertThat(n1).isEqualTo(n2);
            assertThat(n1.hashCode()).isEqualTo(n2.hashCode());
        }

        @Test
        @DisplayName("instancias con valores distintos no son iguales")
        void equals_differentValue() {
            ServiceName n1 = ServiceName.custom("Laser Whitening");
            ServiceName n2 = ServiceName.custom("Porcelain Veneer");

            assertThat(n1).isNotEqualTo(n2);
        }
    }
}
