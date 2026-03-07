
package com.example.ClinicaDefinitiva.domain.adminitration.operations;

import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ShiftId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.operations.OperationsVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShiftIdTest {

    @Test
    void shouldCreateValidShiftId() {
        ShiftId id = ShiftId.from(10L);
        assertEquals(10L, id.value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> ShiftId.from(null));

        assertEquals(OperationsVoError.ERR_SHIFT_ID_REQUIRED, ex.getCatalogo());
        assertEquals(VOContext.OPERATIONS, ex.getContexto());
    }

    @Test
    void shouldBeEqualWhenValuesAreSame() {
        ShiftId id1 = ShiftId.from(5L);
        ShiftId id2 = ShiftId.from(5L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        ShiftId id1 = ShiftId.from(5L);
        ShiftId id2 = ShiftId.from(6L);

        assertNotEquals(id1, id2);
    }
}