package com.example.ClinicaDefinitiva.domain.actor;

import com.example.ClinicaDefinitiva.domain.actor.vo.DateOfBirth;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateOfBirthTest {

    @Test
    void shouldCreateValidDateOfBirth() {
        LocalDate date = LocalDate.now().minusYears(25);
        DateOfBirth dob = DateOfBirth.of(date);

        assertEquals(date, dob.asDate());
        assertEquals(date, dob.Value());
        assertEquals("Date of birth: " + date, dob.toString());
    }

    @Test
    void shouldThrowExceptionWhenDateIsNull() {
        assertThrows(ValueObjectValidationException.class,
                () -> DateOfBirth.of(null));
    }

    @Test
    void shouldThrowExceptionWhenDateIsFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        assertThrows(ValueObjectValidationException.class,
                () -> DateOfBirth.of(futureDate));
    }

    @Test
    void shouldThrowExceptionWhenDateTooOld() {
        LocalDate oldDate = LocalDate.now().minusYears(150);
        assertThrows(ValueObjectValidationException.class,
                () -> DateOfBirth.of(oldDate));
    }

    @Test
    void shouldBeEqualWhenSameDate() {
        LocalDate date = LocalDate.of(1990, 1, 1);
        DateOfBirth dob1 = DateOfBirth.of(date);
        DateOfBirth dob2 = DateOfBirth.of(date);

        assertEquals(dob1, dob2);
        assertEquals(dob1.hashCode(), dob2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentDates() {
        DateOfBirth dob1 = DateOfBirth.of(LocalDate.of(1990, 1, 1));
        DateOfBirth dob2 = DateOfBirth.of(LocalDate.of(2000, 1, 1));

        assertNotEquals(dob1, dob2);
    }
}
