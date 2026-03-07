
package com.example.ClinicaDefinitiva.domain.actor;

import com.example.ClinicaDefinitiva.domain.actor.vo.FullName;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FullNameTest {

    @Test
    void shouldCreateValidFullName() {
        FullName name = FullName.of("John", "Doe");
        assertEquals("John Doe", name.asText());
        assertEquals("John", name.firstName());
        assertEquals("Doe", name.lastName());
        assertEquals("John Doe", name.toString());
    }

    @Test
    void shouldThrowExceptionWhenNullValues() {
        assertThrows(ValueObjectValidationException.class,
            () -> FullName.of(null, "Doe"));
        assertThrows(ValueObjectValidationException.class,
            () -> FullName.of("John", null));
    }

    @Test
    void shouldThrowExceptionWhenBlankValues() {
        assertThrows(ValueObjectValidationException.class,
            () -> FullName.of("   ", "Doe"));
        assertThrows(ValueObjectValidationException.class,
            () -> FullName.of("John", "   "));
    }

    @Test
    void shouldMatchFullNameIgnoringCase() {
        FullName name = FullName.of("John", "Doe");
        assertTrue(name.matches("john doe"));
        assertTrue(name.matches("JOHN DOE"));
    }

    @Test
    void shouldStartWithPrefixIgnoringCase() {
        FullName name = FullName.of("John", "Doe");
        assertTrue(name.startsWith("jo"));
        assertTrue(name.startsWith("JOHN"));
        assertFalse(name.startsWith("Jane"));
    }

    @Test
    void shouldReturnInitials() {
        FullName name = FullName.of("John", "Doe");
        assertEquals("JD", name.initials());
    }

    @Test
    void shouldBeEqualIgnoringCase() {
        FullName name1 = FullName.of("John", "Doe");
        FullName name2 = FullName.of("john", "doe");

        assertEquals(name1, name2);
        assertEquals(name1.hashCode(), name2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentNames() {
        FullName name1 = FullName.of("John", "Doe");
        FullName name2 = FullName.of("Jane", "Doe");

        assertNotEquals(name1, name2);
    }
}
