
package com.example.ClinicaDefinitiva.domain.actor;

import com.example.ClinicaDefinitiva.domain.actor.vo.Specialties;
import com.example.ClinicaDefinitiva.domain.actor.vo.Specialty;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class SpecialtiesTest {

    @Test
    void shouldCreateValidSpecialties() {
        Specialties specialties = Specialties.of(Set.of(
            Specialty.of("Orthodontics"),
            Specialty.of("Endodontics")
        ));

        assertTrue(specialties.contains(Specialty.of("Orthodontics")));
        assertTrue(specialties.isMultidisciplinary());
        assertEquals(2, specialties.asSet().size());
    }

    @Test
    void shouldThrowExceptionWhenEmptySet() {
        assertThrows(ValueObjectValidationException.class,
            () -> Specialties.of(Set.of()));
    }

    @Test
    void shouldAllowSurgicalProcedures() {
        Specialties specialties = Specialties.of(Set.of(
            Specialty.of("Oral Surgery"),
            Specialty.of("General Dentistry")
        ));

        assertTrue(specialties.allowsSurgicalProcedures());
    }

    @Test
    void shouldNotAllowSurgicalProcedures() {
        Specialties specialties = Specialties.of(Set.of(
            Specialty.of("Orthodontics")
        ));

        assertFalse(specialties.allowsSurgicalProcedures());
    }

    @Test
    void shouldBeEqualWhenSameSpecialties() {
        Specialties s1 = Specialties.of(Set.of(Specialty.of("Orthodontics")));
        Specialties s2 = Specialties.of(Set.of(Specialty.of("Orthodontics")));

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentSpecialties() {
        Specialties s1 = Specialties.of(Set.of(Specialty.of("Orthodontics")));
        Specialties s2 = Specialties.of(Set.of(Specialty.of("Endodontics")));

        assertNotEquals(s1, s2);
    }
}
