
package com.example.ClinicaDefinitiva.domain.actor;

import com.example.ClinicaDefinitiva.domain.actor.vo.TypeGuardian;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TypeGuardianTest {

    @Test
    void shouldCreateValidTypeGuardian() {
        TypeGuardian guardian = TypeGuardian.of("MAMA", "Madre");
        assertEquals("MAMA", guardian.getCode());
        assertEquals("Madre", guardian.getDescription());
        assertTrue(guardian.isParent());
        assertEquals(1, guardian.getLegalPriority());
    }

    @Test
    void shouldThrowExceptionWhenCodeIsNull() {
        assertThrows(ValueObjectValidationException.class,
            () -> TypeGuardian.of(null, "Madre"));
    }

    @Test
    void shouldThrowExceptionWhenCodeIsBlank() {
        assertThrows(ValueObjectValidationException.class,
            () -> TypeGuardian.of("   ", "Madre"));
    }

    @Test
    void shouldThrowExceptionWhenCodeIsInvalid() {
        assertThrows(ValueObjectValidationException.class,
            () -> TypeGuardian.of("INVALID", "Otro"));
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsBlank() {
        assertThrows(ValueObjectValidationException.class,
            () -> TypeGuardian.of("MAMA", "   "));
    }

    @Test
    void shouldIdentifyGrandparent() {
        TypeGuardian abuelo = TypeGuardian.of("ABUELO", "Abuelo");
        assertTrue(abuelo.isGrandparent());
        assertEquals(3, abuelo.getLegalPriority());
    }

    @Test
    void shouldIdentifySibling() {
        TypeGuardian hermano = TypeGuardian.of("HERMANO", "Hermano");
        assertTrue(hermano.isSibling());
        assertEquals(4, hermano.getLegalPriority());
    }

    @Test
    void shouldIdentifyLegalGuardian() {
        TypeGuardian tutor = TypeGuardian.of("TUTOR_LEGAL", "Tutor Legal");
        assertTrue(tutor.isLegalGuardian());
        assertEquals(2, tutor.getLegalPriority());
    }

    @Test
    void shouldBeEqualWhenSameCode() {
        TypeGuardian g1 = TypeGuardian.of("PAPA", "Padre");
        TypeGuardian g2 = TypeGuardian.of("PAPA", "Padre");

        assertEquals(g1, g2);
        assertEquals(g1.hashCode(), g2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentCode() {
        TypeGuardian g1 = TypeGuardian.of("MAMA", "Madre");
        TypeGuardian g2 = TypeGuardian.of("PAPA", "Padre");

        assertNotEquals(g1, g2);
    }

    @Test
    void shouldReturnReadableToString() {
        TypeGuardian g = TypeGuardian.of("MAMA", "Madre");
        assertEquals("Madre (MAMA)", g.toString());
    }
}

