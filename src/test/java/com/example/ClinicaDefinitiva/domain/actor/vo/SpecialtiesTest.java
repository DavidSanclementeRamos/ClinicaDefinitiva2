package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class SpecialtiesTest {

    @Test
    @DisplayName("Crear Specialties con conjunto válido")
    void shouldCreateSpecialties() {
        Set<Specialty> specialtiesSet = Set.of(
                Specialty.of("Orthodontics"),
                Specialty.of("Endodontics")
        );
        Specialties specialties = Specialties.of(specialtiesSet);
        assertThat(specialties.asSet()).containsExactlyInAnyOrderElementsOf(specialtiesSet);
        assertThat(specialties.contains(Specialty.of("Orthodontics"))).isTrue();
        assertThat(specialties.isMultidisciplinary()).isTrue();
    }

    @Test
    @DisplayName("Crear Specialties con conjunto vacío lanza excepción")
    void shouldThrowForEmptySet() {
        assertThatThrownBy(() -> Specialties.of(Set.of()))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("allowsSurgicalProcedures() retorna true si incluye Oral Surgery")
    void testAllowsSurgicalProcedures() {
        Set<Specialty> withSurgery = Set.of(
                Specialty.of("Oral Surgery"),
                Specialty.of("Orthodontics")
        );
        Specialties specialties = Specialties.of(withSurgery);
        assertThat(specialties.allowsSurgicalProcedures()).isTrue();

        Set<Specialty> withoutSurgery = Set.of(Specialty.of("Orthodontics"));
        Specialties noSurgery = Specialties.of(withoutSurgery);
        assertThat(noSurgery.allowsSurgicalProcedures()).isFalse();
    }
}
