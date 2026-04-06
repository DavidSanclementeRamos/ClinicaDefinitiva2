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
        Set<Specialty> specialtiesSet = Set.of(Specialty.ORTHODONTICS, Specialty.ENDODONTICS);
        Specialties specialties = Specialties.of(specialtiesSet);
        assertThat(specialties.asSet()).containsExactlyInAnyOrderElementsOf(specialtiesSet);
        assertThat(specialties.contains(Specialty.ORTHODONTICS)).isTrue();
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
        Set<Specialty> withSurgery = Set.of(Specialty.ORAL_SURGERY, Specialty.ORTHODONTICS);
        Specialties specialties = Specialties.of(withSurgery);
        assertThat(specialties.allowsSurgicalProcedures()).isTrue();

        Set<Specialty> withoutSurgery = Set.of(Specialty.ORTHODONTICS);
        Specialties noSurgery = Specialties.of(withoutSurgery);
        assertThat(noSurgery.allowsSurgicalProcedures()).isFalse();
    }
}