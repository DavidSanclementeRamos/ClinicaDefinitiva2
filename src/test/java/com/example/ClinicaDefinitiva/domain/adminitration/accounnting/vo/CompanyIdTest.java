
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompanyIdTest {

    @Test
    void shouldCreateValidCompanyId() {
        CompanyId id = CompanyId.of(10L);
        assertEquals(10L, id.getValue());
        assertEquals("10", id.toString());
    }

    @Test
    void shouldAllowZeroOrNegativeValues() {
        CompanyId idZero = CompanyId.of(0L);
        assertEquals(0L, idZero.getValue());

        CompanyId idNegative = CompanyId.of(-5L);
        assertEquals(-5L, idNegative.getValue());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> CompanyId.of(null));

        assertEquals(VoAccountingError.ERR_COMPANY_ID_NULL, ex.getCatalogo());
        assertEquals(VOContext.ACCOUNTING, ex.getContexto());
    }

    @Test
    void shouldBeEqualWhenValuesAreSame() {
        CompanyId id1 = CompanyId.of(5L);
        CompanyId id2 = CompanyId.of(5L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        CompanyId id1 = CompanyId.of(5L);
        CompanyId id2 = CompanyId.of(6L);

        assertNotEquals(id1, id2);
    }
}

