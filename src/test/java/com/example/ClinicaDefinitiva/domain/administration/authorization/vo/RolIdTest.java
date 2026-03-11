
package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RolIdTest {

    @Test
    void shouldCreateRolIdSuccessfully() {
        RolId rolId = RolId.of(10L);
        assertEquals(10L, rolId.getValue());
    }

    @Test
    void shouldThrowExceptionForInvalidValues() {
        assertThrows(ValueObjectValidationException.class, () -> RolId.of(null));
    }

    @Test
    void shouldRespectEquality() {
        RolId r1 = RolId.of(5L);
        RolId r2 = RolId.of(5L);
        RolId r3 = RolId.of(6L);

        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
    }

    @Test
    void shouldReturnProperToString() {
        RolId rolId = RolId.of(7L);
        assertTrue(rolId.toString().contains("7"));
    }
}

