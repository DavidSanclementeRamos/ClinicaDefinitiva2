
package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ServiceCatalogTest {

    private final ServiceId sampleId = ServiceId.of(100L);

    @Nested
    @DisplayName("Creación")
    class CreationTests {

        @Test
        @DisplayName("crear con datos válidos")
        void create_valid() {
            ServiceCatalog catalog = ServiceCatalog.of(
                    sampleId,
                    ServiceName.custom("Laser Whitening"),
                    "Aesthetics"
            );

            assertThat(catalog).isNotNull();
            assertThat(catalog.getId()).isEqualTo(sampleId);
            assertThat(catalog.getName().getValue()).isEqualTo("Laser Whitening");
            assertThat(catalog.getCategory()).isEqualTo("Aesthetics");
        }

        @Test
        @DisplayName("crear con categoría nula o vacía -> excepción")
        void create_invalidCategory_throws() {
            assertThatThrownBy(() -> ServiceCatalog.of(
                    sampleId,
                    ServiceName.custom("Laser Whitening"),
                    null
            ))
            .isInstanceOf(ValueObjectValidationException.class)
            .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                    .isEqualTo(ServiceVOError.ERR_SERVICE_CATEGORY_NULL_OR_BLANK));

            assertThatThrownBy(() -> ServiceCatalog.of(
                    sampleId,
                    ServiceName.custom("Laser Whitening"),
                    "   "
            ))
            .isInstanceOf(ValueObjectValidationException.class)
            .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                    .isEqualTo(ServiceVOError.ERR_SERVICE_CATEGORY_NULL_OR_BLANK));
        }
    }

    @Nested
    @DisplayName("Igualdad y hashCode")
    class EqualityTests {

        @Test
        @DisplayName("dos catálogos con mismos atributos son iguales")
        void equals_sameAttributes() {
            ServiceCatalog c1 = ServiceCatalog.of(sampleId, ServiceName.custom("Laser Whitening"), "Aesthetics");
            ServiceCatalog c2 = ServiceCatalog.of(sampleId, ServiceName.custom("Laser Whitening"), "Aesthetics");

            assertThat(c1).isEqualTo(c2);
            assertThat(c1.hashCode()).isEqualTo(c2.hashCode());
        }

        @Test
        @DisplayName("catálogos con atributos distintos no son iguales")
        void equals_differentAttributes() {
            ServiceCatalog c1 = ServiceCatalog.of(sampleId, ServiceName.custom("Laser Whitening"), "Aesthetics");
            ServiceCatalog c2 = ServiceCatalog.of(sampleId, ServiceName.custom("Porcelain Veneer"), "Prosthetics");

            assertThat(c1).isNotEqualTo(c2);
        }
    }

    @Nested
    @DisplayName("Defaults")
    class DefaultsTests {

        @Test
        @DisplayName("usar un valor por defecto")
        void defaults_valid() {
            ServiceCatalog defaultCatalog = ServiceCatalog.Defaults.AES_PORCELAIN_VENEER.get();

            assertThat(defaultCatalog.getId()).isEqualTo(ServiceId.of(10L));
            assertThat(defaultCatalog.getName().getValue()).isEqualTo("Porcelain Veneer");
            assertThat(defaultCatalog.getCategory()).isEqualTo("Aesthetics");
        }
    }
}
