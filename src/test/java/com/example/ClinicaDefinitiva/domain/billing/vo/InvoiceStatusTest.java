
package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.billing.valueObject.InvoiceStatus;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceStatusTest {

    @Test
    void shouldCreateDraftStatus() {
        InvoiceStatus status = InvoiceStatus.draft();
        assertTrue(status.isDraft());
        assertEquals("Borrador", status.getValue().getDescription());
    }

    @Test
    void shouldThrowExceptionWhenStatusIsNull() {
        assertThrows(ValueObjectValidationException.class, () -> InvoiceStatus.of(null));
    }

    @Test
    void shouldAllowValidTransition() {
        InvoiceStatus status = InvoiceStatus.draft();
        assertTrue(status.canTransitionTo(InvoiceStatus.Status.PENDING));
        InvoiceStatus next = status.transitionTo(InvoiceStatus.Status.PENDING);
        assertTrue(next.isPending());
    }

    @Test
    void shouldThrowExceptionForInvalidTransition() {
        InvoiceStatus status = InvoiceStatus.paid();
        assertThrows(ValueObjectValidationException.class,
                () -> status.transitionTo(InvoiceStatus.Status.DRAFT));
    }

    @Test
    void shouldReturnCorrectDescriptions() {
        assertEquals("Pendiente", InvoiceStatus.Status.PENDING.getDescription());
        assertEquals("Pagado", InvoiceStatus.Status.PAID.getDescription());
        assertEquals("Cancelado", InvoiceStatus.Status.CANCELLED.getDescription());
    }
}


