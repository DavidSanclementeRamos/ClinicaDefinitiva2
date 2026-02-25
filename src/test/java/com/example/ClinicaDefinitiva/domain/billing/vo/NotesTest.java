
package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.billing.valueObject.Notes;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

class NotesTest {

    @Test
    void shouldCreateValidNotes() {
        Notes notes = Notes.of("Valid note");
        assertTrue(notes.getValue().isPresent());
        assertEquals("Valid note", notes.getValue().get());
    }

    @Test
    void shouldTrimNotesValue() {
        Notes notes = Notes.of("   Trimmed note   ");
        assertEquals("Trimmed note", notes.getValue().get());
    }

    @Test
    void shouldAllowNullNotes() {
        Notes notes = Notes.of(null);
        assertTrue(notes.getValue().isEmpty());
        assertEquals("", notes.toString());
    }

    @Test
    void shouldThrowExceptionWhenTooShort() {
        assertThrows(ValueObjectValidationException.class, () -> Notes.of("ab"));
    }

    @Test
    void toStringShouldReturnValueOrEmpty() {
        Notes notes1 = Notes.of("Some note");
        assertEquals("Some note", notes1.toString());

        Notes notes2 = Notes.of(null);
        assertEquals("", notes2.toString());
    }
}

