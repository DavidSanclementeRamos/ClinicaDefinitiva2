
package com.example.ClinicaDefinitiva.domain.actor;

import com.example.ClinicaDefinitiva.domain.actor.vo.Specialty;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpecialtyTest {

    @Test
    void shouldCreateValidSpecialty() {
        Specialty s = Specialty.of("Orthodontics");
        assertEquals("Orthodontics", s.asText());
        assertTrue(s.is("Orthodontics"));
    }

    @Test
    void shouldThrowExceptionForInvalidSpecialty() {
        assertThrows(ValueObjectValidationException.class,
            () -> Specialty.of("InvalidSpecialty"));
    }

    @Test
    void shouldThrowExceptionForBlankSpecialty() {
        assertThrows(ValueObjectValidationException.class,
            () -> Specialty.of("   "));
    }

    
    @Test
    void shouldBeEqualIgnoringCase() {
    Specialty s1 = Specialty.of("Endodontics");
    Specialty s2 = Specialty.of("Endodontics");

    assertEquals(s1, s2);
    assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentSpecialties() {
        Specialty s1 = Specialty.of("Periodontics");
        Specialty s2 = Specialty.of("Prosthodontics");

        assertNotEquals(s1, s2);
    }
}
