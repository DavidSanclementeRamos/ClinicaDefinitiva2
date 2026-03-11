
package com.example.ClinicaDefinitiva.domain.administration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.AdministrativeReportId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.accounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AdministrativeReportIdTest {

   /** @Test
    void shouldCreateValidAdministrativeReportId() {
        AdministrativeReportId id = AdministrativeReportId.of(10L);
        assertEquals(10L, id.value());
    }

   

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> AdministrativeReportId.of(null));

        assertEquals(VoAccountingError.ERR_ADMINREPORT_ID_NULL, ex.getCatalogo());
        assertEquals(VOContext.ACCOUNTING, ex.getContexto());
    }

    @Test
    void shouldBeEqualWhenValuesAreSame() {
        AdministrativeReportId id1 = AdministrativeReportId.of(5L);
        AdministrativeReportId id2 = AdministrativeReportId.of(5L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        AdministrativeReportId id1 = AdministrativeReportId.of(5L);
        AdministrativeReportId id2 = AdministrativeReportId.of(6L);

        assertNotEquals(id1, id2);
    }*/
}


