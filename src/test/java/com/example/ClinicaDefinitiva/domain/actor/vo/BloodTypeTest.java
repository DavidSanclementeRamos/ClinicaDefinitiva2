package com.example.ClinicaDefinitiva.domain.actor;

import com.example.ClinicaDefinitiva.domain.actor.vo.BloodType;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BloodTypeTest {

    @Test
    void shouldCreateValidBloodType() {
        BloodType bt = BloodType.fromLabel("a+");
        assertEquals("A+", bt.getValue());
    }

    @Test
    void shouldThrowExceptionForInvalidBloodType() {
        assertThrows(ValueObjectValidationException.class,
                () -> BloodType.fromLabel("X+"));
    }

    @Test
    void shouldNormalizeToUpperCase() {
        BloodType bt = BloodType.fromLabel("o-");
        assertEquals("O-", bt.getValue());
    }

    @Test
    void shouldAcceptAllValidTypes() {
        String[] validTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        for (String type : validTypes) {
            BloodType bt = BloodType.fromLabel(type.toLowerCase());
            assertEquals(type, bt.getValue());
        }
    }
}
