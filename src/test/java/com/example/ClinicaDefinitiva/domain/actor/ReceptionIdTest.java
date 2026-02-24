
package com.example.ClinicaDefinitiva.domain.actor;

import com.example.ClinicaDefinitiva.domain.actor.vo.ReceptionId;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReceptionIdTest {

    @Test
    void shouldCreateValidReceptionId() {
        ReceptionId id = ReceptionId.of(500L);
        assertEquals(500L, id.getValue());
    }

    @Test
    void shouldThrowExceptionWhenNullValue() {
        assertThrows(ValueObjectValidationException.class,
            () -> ReceptionId.of(null));
    }

    @Test
    void shouldBeEqualWhenSameValue() {
        ReceptionId id1 = ReceptionId.of(100L);
        ReceptionId id2 = ReceptionId.of(100L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentValues() {
        ReceptionId id1 = ReceptionId.of(100L);
        ReceptionId id2 = ReceptionId.of(200L);

        assertNotEquals(id1, id2);
    }
}


