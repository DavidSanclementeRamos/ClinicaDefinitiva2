
package com.example.ClinicaDefinitiva.domain.administration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Document;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    @Test
    void shouldCreateValidDocument() {
    Document doc = Document.of("Report", "http://example.com/doc.pdf", "pdf", 1024);
    assertEquals("Report", doc.getName());
    assertEquals("http://example.com/doc.pdf", doc.getUrl());
    assertEquals("PDF", doc.getType());
    assertEquals(1024, doc.getSize());
}


    @Test
    void shouldThrowExceptionWhenNameIsNullOrBlank() {
        assertThrows(ValueObjectValidationException.class,
            () -> Document.of(null, "http://example.com", "pdf", 1024));

        assertThrows(ValueObjectValidationException.class,
            () -> Document.of("   ", "http://example.com", "pdf", 1024));
    }

    @Test
    void shouldThrowExceptionWhenUrlIsNullOrBlank() {
        assertThrows(ValueObjectValidationException.class,
            () -> Document.of("Report", null, "pdf", 1024));

        assertThrows(ValueObjectValidationException.class,
            () -> Document.of("Report", "   ", "pdf", 1024));
    }

    @Test
    void shouldThrowExceptionWhenUrlIsTooLong() {
        String longUrl = "http://example.com/" + "a".repeat(600);
        assertThrows(ValueObjectValidationException.class,
            () -> Document.of("Report", longUrl, "pdf", 1024));
    }

    @Test
    void shouldThrowExceptionWhenSizeIsInvalid() {
        assertThrows(ValueObjectValidationException.class,
            () -> Document.of("Report", "http://example.com", "pdf", 0));

        assertThrows(ValueObjectValidationException.class,
            () -> Document.of("Report", "http://example.com", "pdf", -100));

        assertThrows(ValueObjectValidationException.class,
            () -> Document.of("Report", "http://example.com", "pdf", 20_000_000));
    }
}
