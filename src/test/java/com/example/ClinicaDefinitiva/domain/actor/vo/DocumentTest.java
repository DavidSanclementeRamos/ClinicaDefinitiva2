
package com.example.ClinicaDefinitiva.domain.actor;

/**
 *
 * @author David
 */
import com.example.ClinicaDefinitiva.domain.actor.vo.Document;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    @Test
    void shouldCreateValidDocument() {
        Document doc = Document.of("123456789");
        assertEquals("123456789", doc.value());
        assertEquals("123456789", doc.toString());
    }

    @Test
    void shouldThrowExceptionWhenNull() {
        assertThrows(ValueObjectValidationException.class,
            () -> Document.of(null));
    }

    @Test
    void shouldThrowExceptionWhenBlank() {
        assertThrows(ValueObjectValidationException.class,
            () -> Document.of("   "));
    }

    @Test
    void shouldThrowExceptionWhenInvalidFormat() {
        assertThrows(ValueObjectValidationException.class,
            () -> Document.of("ABC123"));
        assertThrows(ValueObjectValidationException.class,
            () -> Document.of("12345")); // menos de 6 dígitos
        assertThrows(ValueObjectValidationException.class,
            () -> Document.of("12345678901")); // más de 10 dígitos
    }

    @Test
    void shouldBeEqualWhenSameValue() {
        Document doc1 = Document.of("123456789");
        Document doc2 = Document.of("123456789");

        assertEquals(doc1, doc2);
        assertEquals(doc1.hashCode(), doc2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentValues() {
        Document doc1 = Document.of("123456789");
        Document doc2 = Document.of("987654321");

        assertNotEquals(doc1, doc2);
    }
}
