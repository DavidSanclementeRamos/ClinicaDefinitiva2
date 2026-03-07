
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompanyStatusTest {

    @Test
    void shouldCreateActiveStatus() {
        CompanyStatus status = CompanyStatus.of(CompanyStatus.Status.ACTIVE);
        assertTrue(status.isEditable());
        assertEquals("Compañía activa", status.getDescription());
    }

    @Test
    void shouldCreateInactiveStatus() {
        CompanyStatus status = CompanyStatus.of(CompanyStatus.Status.INACTIVE);
        assertTrue(status.isInactive());
        assertEquals("Compañía inactiva", status.getDescription());
    }

    @Test
    void shouldCreateSuspendedStatus() {
        CompanyStatus status = CompanyStatus.of(CompanyStatus.Status.SUSPENDED);
        assertTrue(status.isSuspended());
        assertEquals("Compañía suspendida", status.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenStatusIsNull() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> CompanyStatus.of(null));

        assertEquals(VoAccountingError.ERR_COMPANY_STATUS_NULL, ex.getCatalogo());
        assertEquals(VOContext.ACCOUNTING, ex.getContexto());
    }

    @Test
    void shouldNotBeEditableWhenInactiveOrSuspended() {
        CompanyStatus inactive = CompanyStatus.of(CompanyStatus.Status.INACTIVE);
        CompanyStatus suspended = CompanyStatus.of(CompanyStatus.Status.SUSPENDED);

        assertFalse(inactive.isEditable());
        assertFalse(suspended.isEditable());
    }
}
