package com.example.ClinicaDefinitiva.domain.actor;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistAvailabilityStatus;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DentistAvailabilityStatusTest {

    @Test
    void shouldCreateAvailableStatus() {
        DentistAvailabilityStatus status = DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.AVAILABLE);

        assertTrue(status.isAvailable());
        assertFalse(status.isAbsent());
        assertEquals(DentistAvailabilityStatus.Priority.HIGH, status.getPriority());
        assertEquals("Disponible", status.getDescription());
        assertEquals(DentistAvailabilityStatus.Status.AVAILABLE, status.getValue());
        assertEquals("AVAILABLE", status.toString());
    }

    @Test
    void shouldCreateSickLeaveStatus() {
        DentistAvailabilityStatus status = DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.SICK_LEAVE);

        assertFalse(status.isAvailable());
        assertTrue(status.isAbsent());
        assertEquals(DentistAvailabilityStatus.Priority.NOT_ASSIGNABLE, status.getPriority());
        assertEquals("Incapacidad médica", status.getDescription());
    }

    @Test
    void shouldCreateVacationStatus() {
        DentistAvailabilityStatus status = DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.VACATION);

        assertFalse(status.isAvailable());
        assertTrue(status.isAbsent());
        assertEquals(DentistAvailabilityStatus.Priority.NOT_ASSIGNABLE, status.getPriority());
        assertEquals("Ausencia planificada", status.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenNullStatus() {
        assertThrows(ValueObjectValidationException.class,
                () -> DentistAvailabilityStatus.of(null));
    }
}
