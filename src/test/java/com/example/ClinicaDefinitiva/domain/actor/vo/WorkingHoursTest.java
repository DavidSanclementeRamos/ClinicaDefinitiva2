
package com.example.ClinicaDefinitiva.domain.actor;

import com.example.ClinicaDefinitiva.domain.actor.vo.WorkingHours;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class WorkingHoursTest {

    @Test
    void shouldCreateValidWorkingHours() {
        WorkingHours wh = WorkingHours.of(
                LocalTime.of(8, 0),
                LocalTime.of(16, 0),
                DayOfWeek.MONDAY,
                40
        );

        assertEquals(DayOfWeek.MONDAY, wh.getDayOfWeek());
        assertEquals(LocalTime.of(8, 0), wh.getStart());
        assertEquals(LocalTime.of(16, 0), wh.getEnd());
        assertEquals(40, wh.getDeclaredHoursPerWeek());
        assertEquals(Duration.ofHours(8), wh.duracionTotal());
    }

    @Test
    void shouldThrowExceptionWhenStartIsAfterEnd() {
        assertThrows(ValueObjectValidationException.class,
                () -> WorkingHours.of(LocalTime.of(16, 0),
                        LocalTime.of(8, 0),
                        DayOfWeek.MONDAY,
                        40));
    }

    @Test
    void shouldThrowExceptionWhenDeclaredHoursInvalid() {
        assertThrows(ValueObjectValidationException.class,
                () -> WorkingHours.of(LocalTime.of(8, 0),
                        LocalTime.of(16, 0),
                        DayOfWeek.MONDAY,
                        0));

        assertThrows(ValueObjectValidationException.class,
                () -> WorkingHours.of(LocalTime.of(8, 0),
                        LocalTime.of(16, 0),
                        DayOfWeek.MONDAY,
                        60));
    }

    @Test
    void shouldReturnTrueWhenDateTimeIsWithinWorkingHours() {
        WorkingHours wh = WorkingHours.of(
                LocalTime.of(8, 0),
                LocalTime.of(16, 0),
                DayOfWeek.MONDAY,
                40
        );

        LocalDateTime dt = LocalDateTime.of(2026, 2, 23, 10, 0); // lunes 10:00
        assertTrue(wh.isWithin(dt));
    }

    @Test
    void shouldReturnFalseWhenDateTimeIsOutsideWorkingHours() {
        WorkingHours wh = WorkingHours.of(
                LocalTime.of(8, 0),
                LocalTime.of(16, 0),
                DayOfWeek.MONDAY,
                40
        );

        LocalDateTime dt = LocalDateTime.of(2026, 2, 23, 18, 0); // lunes 18:00
        assertFalse(wh.isWithin(dt));
    }

    @Test
    void shouldReturnTrueWhenRangeIsWithinWorkingHours() {
        WorkingHours wh = WorkingHours.of(
                LocalTime.of(8, 0),
                LocalTime.of(16, 0),
                DayOfWeek.MONDAY,
                40
        );

        assertTrue(wh.isWithinRange(LocalTime.of(9, 0),
                LocalTime.of(15, 0),
                DayOfWeek.MONDAY));
    }

    @Test
    void shouldReturnFalseWhenRangeExceedsWorkingHours() {
        WorkingHours wh = WorkingHours.of(
                LocalTime.of(8, 0),
                LocalTime.of(16, 0),
                DayOfWeek.MONDAY,
                40
        );

        assertFalse(wh.isWithinRange(LocalTime.of(7, 0),
                LocalTime.of(17, 0),
                DayOfWeek.MONDAY));
    }
}
