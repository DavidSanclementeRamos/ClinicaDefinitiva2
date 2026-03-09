
package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceIdTest {

    @Test
    void shouldCreateInvoiceIdSuccessfully() {
        InvoiceId id = InvoiceId.of(100L);
        assertEquals(100L, id.getValue());
    }

    @Test
    void shouldThrowExceptionForNullValue() {
        assertThrows(ValueObjectValidationException.class, () -> InvoiceId.of(null));
    }

    @Test
    void shouldRespectEquality() {
        InvoiceId id1 = InvoiceId.of(200L);
        InvoiceId id2 = InvoiceId.of(200L);
        InvoiceId id3 = InvoiceId.of(300L);

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
    }

    @Test
    void toStringShouldReturnValue() {
        InvoiceId id = InvoiceId.of(400L);
        assertEquals("InvoiceId[getValue=400]", id.toString());
    }
}

