
package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceNumberTest {

    @Test
    void shouldCreateValidInvoiceNumberWithOf() {
        InvoiceNumber number = InvoiceNumber.of("FAC-0001");
        assertEquals("FAC", number.getPrefix());
        assertEquals(1L, number.getSequence());
        assertEquals("FAC-0001", number.getValue());
    }

    @Test
    void shouldCreateValidInvoiceNumberWithFrom() {
        InvoiceNumber number = InvoiceNumber.from("INV", 25);
        assertEquals("INV", number.getPrefix());
        assertEquals(25L, number.getSequence());
        assertEquals("INV-0025", number.getValue());
    }

    @Test
    void shouldThrowExceptionWhenPrefixIsNull() {
        assertThrows(ValueObjectValidationException.class, () -> InvoiceNumber.from(null, 1));
    }

    @Test
    void shouldThrowExceptionWhenSequenceIsNegative() {
        assertThrows(ValueObjectValidationException.class, () -> InvoiceNumber.from("FAC", -5));
    }

    @Test
    void shouldThrowExceptionWhenValueIsInvalidFormat() {
        assertThrows(ValueObjectValidationException.class, () -> InvoiceNumber.of("INVALID"));
    }

    @Test
    void shouldNormalizeToUpperCase() {
        InvoiceNumber number = InvoiceNumber.of("fac-0002");
        assertEquals("FAC", number.getPrefix());
        assertEquals("FAC-0002", number.toString());
    }

    @Test
    void shouldRespectEquality() {
        InvoiceNumber n1 = InvoiceNumber.of("FAC-0003");
        InvoiceNumber n2 = InvoiceNumber.of("FAC-0003");
        InvoiceNumber n3 = InvoiceNumber.of("INV-0003");

        assertEquals(n1, n2);
        assertNotEquals(n1, n3);
    }
}

