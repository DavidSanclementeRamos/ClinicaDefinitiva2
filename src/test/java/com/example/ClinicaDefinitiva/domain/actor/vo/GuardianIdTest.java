
package com.example.ClinicaDefinitiva.domain.actor;

import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuardianIdTest {

    @Test
    void shouldCreateValidGuardianId() {
        GuardianId id = GuardianId.fromLong(100L);
        assertEquals(100L, id.value());
    }

    @Test
    void shouldThrowExceptionWhenNullValue() {
        assertThrows(ValueObjectValidationException.class,
            () -> GuardianId.fromLong(null));
    }

    @Test
    void shouldBeEqualWhenSameValue() {
        GuardianId id1 = GuardianId.fromLong(50L);
        GuardianId id2 = GuardianId.fromLong(50L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentValues() {
        GuardianId id1 = GuardianId.fromLong(50L);
        GuardianId id2 = GuardianId.fromLong(60L);

        assertNotEquals(id1, id2);
    }
}
