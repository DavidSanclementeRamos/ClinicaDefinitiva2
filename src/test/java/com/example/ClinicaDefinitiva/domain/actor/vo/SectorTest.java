package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

class SectorTest {

    @ParameterizedTest
    @EnumSource(Sector.Type.class)
    @DisplayName("Crear Sector con todos los tipos")
    void shouldCreateSector(Sector.Type type) {
        Sector sector = Sector.of(type);
        assertThat(sector.getValue()).isEqualTo(type);
        assertThat(sector.is(type)).isTrue();
        assertThat(sector.getDescription()).isEqualTo(type.getDescription());
    }

    @Test
    @DisplayName("Sector.fromString válido")
    void shouldCreateFromString() {
        Sector sector = Sector.fromString("RECEPTION");
        assertThat(sector.getValue()).isEqualTo(Sector.Type.RECEPTION);
    }

    @Test
    @DisplayName("Sector.fromString inválido lanza excepción")
    void shouldThrowForInvalidString() {
        assertThatThrownBy(() -> Sector.fromString("INVALID"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Sector.fromString con valor nulo lanza excepción")
    void shouldThrowForNullString() {
        assertThatThrownBy(() -> Sector.fromString(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
