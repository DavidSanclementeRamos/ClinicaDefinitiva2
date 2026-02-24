package com.example.ClinicaDefinitiva.domain.actor;

import org.junit.jupiter.api.Test;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import static org.junit.jupiter.api.Assertions.*;

class DentistIdTest {

    @Test
    void shouldCreateValidDentistId() {
        DentistId id = DentistId.of(123L);
        assertEquals(123L, id.value());
    }

    @Test
    void shouldThrowExceptionWhenNullValue() {
        assertThrows(ValueObjectValidationException.class,
                () -> DentistId.of(null));
    }

    @Test
    void shouldBeEqualWhenSameValue() {
        DentistId id1 = DentistId.of(10L);
        DentistId id2 = DentistId.of(10L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentValues() {
        DentistId id1 = DentistId.of(10L);
        DentistId id2 = DentistId.of(20L);

        assertNotEquals(id1, id2);
    }
}
