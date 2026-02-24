package com.example.ClinicaDefinitiva.domain.actor;

import com.example.ClinicaDefinitiva.domain.actor.vo.Age;
import com.example.ClinicaDefinitiva.domain.actor.vo.DateOfBirth;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AgeTest {

    @Test
    void shouldCreateValidAge() {
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(25));
        Age age = Age.of(dob);

        assertEquals(25, age.asInt());
        assertTrue(age.isAdult());
        assertFalse(age.isElderly());
        assertTrue(age.isEligibleForRegistration());
        assertEquals("Adult", age.ageCategory());
    }



    @Test
    void shouldIdentifyElderly() {
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(70));
        Age age = Age.of(dob);

        assertTrue(age.isElderly());
        assertEquals("Senior", age.ageCategory());
    }

    @Test
    void shouldIdentifyChild() {
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(10));
        Age age = Age.of(dob);

        assertFalse(age.isAdult());
        assertEquals("Child", age.ageCategory());
    }

    @Test
    void shouldIdentifyTeenager() {
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(15));
        Age age = Age.of(dob);

        assertTrue(age.isEligibleForRegistration());
        assertEquals("Teenager", age.ageCategory());
    }

    @Test
    void shouldCheckIsBetween() {
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(30));
        Age age = Age.of(dob);

        assertTrue(age.isBetween(20, 40));
        assertFalse(age.isBetween(31, 35));
    }
}