package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ImplantologyDetailsTest {

    @Test
    @DisplayName("Crear detalles de implantología válidos")
    void shouldCreateValid() {
        ImplantologyDetails details = new ImplantologyDetails(
                6, "Titanio", "Maxilar superior", false
        );
        assertThat(details.getHealingTimeMonths()).isEqualTo(6);
        assertThat(details.getImplantType()).isEqualTo("Titanio");
        assertThat(details.getPlacementSite()).isEqualTo("Maxilar superior");
        assertThat(details.getRequiresBoneGraft()).isFalse();
    }

    @Test
    @DisplayName("Tiempo de cicatrización negativo lanza excepción")
    void shouldRejectNegativeHealingTime() {
        assertThatThrownBy(() -> new ImplantologyDetails(-1, "Titanio", "Maxilar", false))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Tiempo de cicatrización fuera de rango [2,12]")
    void shouldRejectHealingTimeOutOfRange() {
        assertThatThrownBy(() -> new ImplantologyDetails(1, "Titanio", "Maxilar", false))
                .isInstanceOf(ValueObjectValidationException.class);
        assertThatThrownBy(() -> new ImplantologyDetails(13, "Titanio", "Maxilar", false))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Injerto óseo requiere ≥4 meses de cicatrización")
    void boneGraftRequiresMinHealingTime() {
        assertThatThrownBy(() -> new ImplantologyDetails(3, "Titanio", "Maxilar", true))
                .isInstanceOf(ValueObjectValidationException.class);
        new ImplantologyDetails(4, "Titanio", "Maxilar", true); // no debe lanzar
    }

    @Test
    @DisplayName("Sitio de colocación debe tener longitud mínima 2")
    void placementSiteMinLength() {
        assertThatThrownBy(() -> new ImplantologyDetails(6, "Titanio", "A", false))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}