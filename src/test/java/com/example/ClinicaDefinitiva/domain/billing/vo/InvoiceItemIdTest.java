
package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceItemIdTest {

    @Test
    void shouldCreateInvoiceItemIdSuccessfully() {
        InvoiceItemId id = InvoiceItemId.of(100L);
        assertEquals(100L, id.getValue());
    }

    @Test
    void shouldThrowExceptionForNullValue() {
        assertThrows(ValueObjectValidationException.class, () -> InvoiceItemId.of(null));
    }

    @Test
    void shouldRespectEquality() {
        InvoiceItemId id1 = InvoiceItemId.of(200L);
        InvoiceItemId id2 = InvoiceItemId.of(200L);
        InvoiceItemId id3 = InvoiceItemId.of(300L);

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
    }

    @Test
    void toStringShouldReturnValue() {
        InvoiceItemId id = InvoiceItemId.of(400L);
        assertEquals("InvoiceItemId[getValue=400]", id.toString());
    }
}

