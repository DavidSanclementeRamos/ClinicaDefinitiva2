
package com.example.ClinicaDefinitiva.domain.actor;

import com.example.ClinicaDefinitiva.domain.actor.vo.Sector;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SectorTest {

    @Test
    void shouldCreateValidSectorFromEnum() {
        Sector sector = Sector.of(Sector.Type.RECEPTION);
        assertEquals(Sector.Type.RECEPTION, sector.getValue());
        assertEquals("Recepción", sector.getDescription());
    }

    @Test
    void shouldCreateValidSectorFromString() {
        Sector sector = Sector.fromString("reception");
        assertEquals(Sector.Type.RECEPTION, sector.getValue());
        assertEquals("Recepción", sector.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenStringIsNullOrBlank() {
        assertThrows(ValueObjectValidationException.class,
            () -> Sector.fromString(null));
        assertThrows(ValueObjectValidationException.class,
            () -> Sector.fromString("   "));
    }

    @Test
    void shouldThrowExceptionWhenStringNotAllowed() {
        assertThrows(ValueObjectValidationException.class,
            () -> Sector.fromString("unknown"));
    }

    @Test
    void shouldBeEqualWhenSameSector() {
        Sector s1 = Sector.fromString("billing");
        Sector s2 = Sector.of(Sector.Type.BILLING);

        assertEquals(s1.getValue(), s2.getValue());
        assertEquals(s1.toString(), s2.toString());
    }
}

