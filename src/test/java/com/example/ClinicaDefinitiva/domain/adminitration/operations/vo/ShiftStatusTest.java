
package com.example.ClinicaDefinitiva.domain.adminitration.operations;

import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ShiftStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.operations.OperationsVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShiftStatusTest {

    @Test
    void shouldCreateActiveStatus() {
        ShiftStatus status = ShiftStatus.of(ShiftStatus.Status.ACTIVE);
        assertTrue(status.isActive());
        assertEquals("Activo", status.getDescription());
    }

    @Test
    void shouldTransitionFromActiveToCompleted() {
        ShiftStatus status = ShiftStatus.of(ShiftStatus.Status.ACTIVE);
        ShiftStatus completed = status.complete();
        assertTrue(completed.isCompleted());
    }

    @Test
    void shouldTransitionFromActiveToCancelled() {
        ShiftStatus status = ShiftStatus.of(ShiftStatus.Status.ACTIVE);
        ShiftStatus cancelled = status.cancel();
        assertTrue(cancelled.isCancelled());
    }

    @Test
    void shouldThrowExceptionWhenCompletingCancelledShift() {
        ShiftStatus status = ShiftStatus.of(ShiftStatus.Status.CANCELLED);
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            status::complete);

        assertEquals(OperationsVoError.ERR_SHIFT_INVALID_COMPLETION, ex.getCatalogo());
        assertEquals(VOContext.OPERATIONS, ex.getContexto());
    }

    @Test
    void shouldThrowExceptionWhenCancellingCompletedShift() {
        ShiftStatus status = ShiftStatus.of(ShiftStatus.Status.COMPLETED);
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            status::cancel);

        assertEquals(OperationsVoError.ERR_SHIFT_INVALID_CANCELLATION, ex.getCatalogo());
        assertEquals(VOContext.OPERATIONS, ex.getContexto());
    }

    @Test
    void shouldThrowExceptionWhenNullValue() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> ShiftStatus.of(null));

        assertEquals(OperationsVoError.ERR_SHIFT_STATUS_NULL, ex.getCatalogo());
        assertEquals(VOContext.OPERATIONS, ex.getContexto());
    }
}
