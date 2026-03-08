
package com.example.ClinicaDefinitiva.domain.administration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Period;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PeriodTest {

    @Test
    void shouldCreateValidPeriod() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 12, 31);
        Period period = Period.of(start, end);

        assertEquals(start, period.getStartDate());
        assertEquals(end, period.getEndDate());
        assertEquals("Periodo: 2025-01-01 - 2025-12-31", period.toString());
    }

    @Test
    void shouldThrowExceptionWhenDatesAreNull() {
        assertThrows(ValueObjectValidationException.class,
            () -> Period.of(null, LocalDate.now()));

        assertThrows(ValueObjectValidationException.class,
            () -> Period.of(LocalDate.now(), null));
    }

    @Test
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        LocalDate start = LocalDate.of(2025, 1, 10);
        LocalDate end = LocalDate.of(2025, 1, 5);

        assertThrows(ValueObjectValidationException.class,
            () -> Period.of(start, end));
    }

    @Test
    void shouldReturnTrueForCurrentPeriod() {
        LocalDate today = LocalDate.now();
        Period period = Period.of(today.minusDays(5), today.plusDays(5));

        assertTrue(period.isCurrentPeriod());
    }

    @Test
    void shouldReturnFalseForCurrentPeriodWhenOutside() {
        LocalDate today = LocalDate.now();
        Period period = Period.of(today.minusDays(10), today.minusDays(5));

        assertFalse(period.isCurrentPeriod());
    }

    @Test
    void shouldReturnTrueForPastPeriod() {
        LocalDate today = LocalDate.now();
        Period period = Period.of(today.minusDays(10), today.minusDays(5));

        assertTrue(period.isPastPeriod());
    }

    @Test
    void shouldReturnFalseForPastPeriodWhenStillActive() {
        LocalDate today = LocalDate.now();
        Period period = Period.of(today.minusDays(5), today.plusDays(5));

        assertFalse(period.isPastPeriod());
    }

    @Test
    void shouldContainDateInsidePeriod() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);
        Period period = Period.of(start, end);

        assertTrue(period.contains(LocalDate.of(2025, 1, 15)));
        assertTrue(period.contains(start));
        assertTrue(period.contains(end));
    }

    @Test
    void shouldNotContainDateOutsidePeriod() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);
        Period period = Period.of(start, end);

        assertFalse(period.contains(LocalDate.of(2024, 12, 31)));
        assertFalse(period.contains(LocalDate.of(2025, 2, 1)));
    }
}

