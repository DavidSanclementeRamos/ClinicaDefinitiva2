
package com.example.ClinicaDefinitiva.domain.adminitration.operations.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.administration.operations.enu.ShiftType;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ShiftId;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class ShiftTest {

    private final DentistId dentistId = DentistId.of(1L);
    private final ShiftId shiftId = ShiftId.from(100L);

    @Test
    void shouldCreateValidShift() {
        Shift shift = Shift.create(shiftId, dentistId,
                LocalDate.now().plusDays(1),
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                ShiftType.CLINICAL);

        assertTrue(shift.isActive());
        assertEquals(4, shift.getDurationInHours());
    }

    @Test
    void shouldThrowExceptionWhenInvalidTimeRange() {
        assertThrows(BusinessRuleViolationException.class,
            () -> Shift.create(shiftId, dentistId,
                    LocalDate.now(),
                    LocalTime.of(12, 0),
                    LocalTime.of(10, 0),
                    ShiftType.CLINICAL));
    }

    @Test
    void shouldExcludeBlockWithinShift() {
        Shift shift = Shift.create(shiftId, dentistId,
                LocalDate.now().plusDays(1),
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                ShiftType.ADMINISTRATIVE);

        shift.excludeBlock(LocalTime.of(10, 0), LocalTime.of(10, 30), "Coffee break");

        assertEquals(1, shift.getExcludedBlocks().size());
        assertEquals("Coffee break", shift.getExcludedBlocks().get(0).getReason());
    }

    @Test
    void shouldNotAccommodateAppointmentOverlappingExcludedBlock() {
        Shift shift = Shift.create(shiftId, dentistId,
                LocalDate.now().plusDays(1),
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                ShiftType.ON_CALL);

        shift.excludeBlock(LocalTime.of(10, 0), LocalTime.of(10, 30), "Coffee break");

        boolean canAccommodate = shift.canAccommodateAppointment(
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 15)),
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 25)));

        assertFalse(canAccommodate);
    }

    @Test
    void shouldDetectOverlapBetweenShifts() {
        Shift shift1 = Shift.create(shiftId, dentistId,
                LocalDate.now().plusDays(1),
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                ShiftType.TRAINING);

        Shift shift2 = Shift.create(ShiftId.from(200L), dentistId,
                LocalDate.now().plusDays(1),
                LocalTime.of(11, 0),
                LocalTime.of(13, 0),
                ShiftType.CLINICAL);

        assertTrue(shift1.overlapsWith(shift2));
    }

    @Test
    void shouldCancelShiftWithReason() {
        Shift shift = Shift.create(shiftId, dentistId,
                LocalDate.now().plusDays(1),
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                ShiftType.ADMINISTRATIVE);

        shift.cancel("Dentist unavailable");

        assertTrue(shift.getStatus().isCancelled());
        assertEquals("Dentist unavailable", shift.getCancellationReason());
    }

    @Test
    void shouldCompleteShiftSuccessfully() {
        Shift shift = Shift.create(shiftId, dentistId,
                LocalDate.now().plusDays(1),
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                ShiftType.ON_CALL);

        shift.complete();

        assertTrue(shift.getStatus().isCompleted());
    }

    @Test
    void shouldThrowExceptionWhenCompletingCancelledShift() {
        Shift shift = Shift.create(shiftId, dentistId,
                LocalDate.now().plusDays(1),
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                ShiftType.ADMINISTRATIVE);

        shift.cancel("Dentist unavailable");

        assertThrows(BusinessRuleViolationException.class, shift::complete);
    }
}

