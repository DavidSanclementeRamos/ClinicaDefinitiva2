
package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceNumberGeneratorTest {

    @Test
    void shouldCreateGeneratorSuccessfully() {
        InvoiceNumberGenerator generator = new InvoiceNumberGenerator("FAC", 0);
        assertEquals("FAC-0000", generator.current().getValue());
    }

    @Test
    void shouldThrowExceptionWhenPrefixIsNull() {
        assertThrows(ValueObjectValidationException.class, () -> new InvoiceNumberGenerator(null, 0));
    }

    @Test
    void shouldThrowExceptionWhenPrefixIsBlank() {
        assertThrows(ValueObjectValidationException.class, () -> new InvoiceNumberGenerator("   ", 0));
    }

    @Test
    void shouldThrowExceptionWhenInitialSequenceIsNegative() {
        assertThrows(ValueObjectValidationException.class, () -> new InvoiceNumberGenerator("FAC", -1));
    }

    @Test
    void nextShouldIncrementSequence() {
        InvoiceNumberGenerator generator = new InvoiceNumberGenerator("INV", 0);
        InvoiceNumber first = generator.next();
        InvoiceNumber second = generator.next();

        assertEquals("INV-0001", first.getValue());
        assertEquals("INV-0002", second.getValue());
    }

    @Test
    void currentShouldReturnLastGeneratedNumber() {
        InvoiceNumberGenerator generator = new InvoiceNumberGenerator("INV", 5);
        assertEquals("INV-0005", generator.current().getValue());

        generator.next();
        assertEquals("INV-0006", generator.current().getValue());
    }

    @Test
    void resetShouldUpdateSequence() {
        InvoiceNumberGenerator generator = new InvoiceNumberGenerator("FAC", 10);
        generator.reset(50);
        assertEquals("FAC-0050", generator.current().getValue());
    }

    @Test
    void resetShouldThrowExceptionForNegativeValue() {
        InvoiceNumberGenerator generator = new InvoiceNumberGenerator("FAC", 10);
        assertThrows(ValueObjectValidationException.class, () -> generator.reset(-5));
    }
}
