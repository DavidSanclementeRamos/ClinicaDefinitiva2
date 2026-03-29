package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ProstheticDetailsTest {

    @Test
    @DisplayName("Crear prótesis fija válida")
    void shouldCreateFixed() {
        ProstheticDetails details = new ProstheticDetails("FIXED", "Cerámica", "Corona", 1);
        assertThat(details.getFixedOrRemovable()).isEqualTo("FIXED");
        assertThat(details.getMaterial()).isEqualTo("Cerámica");
        assertThat(details.getProstheticType()).isEqualTo("Corona");
        assertThat(details.getUnits()).isEqualTo(1);
    }

    @Test
    @DisplayName("Crear prótesis removible con unidades limitadas")
    void shouldCreateRemovable() {
        ProstheticDetails details = new ProstheticDetails("REMOVABLE", "Acrílico", "Dentadura", 12);
        assertThat(details.getUnits()).isEqualTo(12);
    }

    @Test
    @DisplayName("Tipo de prótesis obligatorio")
    void shouldRequireType() {
        assertThatThrownBy(() -> new ProstheticDetails(null, "Material", "Tipo", 1))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Tipo de prótesis debe ser FIXED o REMOVABLE")
    void shouldValidateType() {
        assertThatThrownBy(() -> new ProstheticDetails("FLOATING", "Material", "Tipo", 1))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Unidades no pueden ser negativas")
    void shouldRejectNegativeUnits() {
        assertThatThrownBy(() -> new ProstheticDetails("FIXED", "Material", "Tipo", -1))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Prótesis removible no puede exceder 14 unidades")
    void removableExceedsUnitsLimit() {
        assertThatThrownBy(() -> new ProstheticDetails("REMOVABLE", "Acrílico", "Dentadura", 15))
                .isInstanceOf(ValueObjectValidationException.class);
        new ProstheticDetails("REMOVABLE", "Acrílico", "Dentadura", 14); // ok
    }
}
