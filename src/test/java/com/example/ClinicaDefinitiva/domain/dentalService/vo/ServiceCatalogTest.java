package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ServiceCatalogTest {

    @Test
    @DisplayName("Crear catálogo válido")
    void shouldCreateValidCatalog() {
        ServiceId id = ServiceId.of(1L);
        ServiceName name = ServiceName.custom("Limpieza Dental");
        ServiceCatalog catalog = ServiceCatalog.of(id, name, "General");
        assertThat(catalog.getCategory()).isEqualTo("General");
        assertThat(catalog.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Categoría nula o vacía lanza excepción")
    void shouldThrowForInvalidCategory() {
        ServiceId id = ServiceId.of(1L);
        ServiceName name = ServiceName.custom("Limpieza");
        assertThatThrownBy(() -> ServiceCatalog.of(id, name, null))
                .isInstanceOf(ValueObjectValidationException.class);
        assertThatThrownBy(() -> ServiceCatalog.of(id, name, ""))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Usar catálogos por defecto")
    void testDefaults() {
        ServiceCatalog general = ServiceCatalog.Defaults.GENERAL_CONSULTATION.get();
        assertThat(general.getCategory()).isEqualTo("General");
        assertThat(general.getName().getValue()).isEqualTo("General Consultation");
    }
}
