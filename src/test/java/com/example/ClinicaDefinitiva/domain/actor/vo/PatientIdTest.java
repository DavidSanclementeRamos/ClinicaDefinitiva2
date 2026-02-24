
package com.example.ClinicaDefinitiva.domain.actor;

import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientIdTest {

    @Test
    void shouldCreateValidPatientId() {
        PatientId id = PatientId.of(200L);
        assertEquals(200L, id.value());
    }

    @Test
    void shouldThrowExceptionWhenNullValue() {
        assertThrows(ValueObjectValidationException.class,
            () -> PatientId.of(null));
    }

    @Test
    void shouldBeEqualWhenSameValue() {
        PatientId id1 = PatientId.of(300L);
        PatientId id2 = PatientId.of(300L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentValues() {
        PatientId id1 = PatientId.of(300L);
        PatientId id2 = PatientId.of(400L);

        assertNotEquals(id1, id2);
    }
}
