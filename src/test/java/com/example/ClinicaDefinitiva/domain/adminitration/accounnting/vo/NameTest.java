
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NameTest {

    @Test
    void shouldCreateValidName() {
        Name name = Name.of("  David  ");
        assertEquals("David", name.getValue()); // trim aplicado
        assertEquals("David", name.toString()); // toString devuelve valor
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> Name.of(null));

        assertEquals(VoAccountingError.ERR_NAME_NULL, ex.getCatalogo());
        assertEquals(VOContext.ACCOUNTING, ex.getContexto());
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> Name.of("   "));

        assertEquals(VoAccountingError.ERR_NAME_BLANK, ex.getCatalogo());
        assertEquals(VOContext.ACCOUNTING, ex.getContexto());
    }

    @Test
    void shouldThrowExceptionWhenValueIsTooLong() {
        String longName = "a".repeat(300);
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> Name.of(longName));

        assertEquals(VoAccountingError.ERR_NAME_TOO_LONG, ex.getCatalogo());
        assertEquals(VOContext.ACCOUNTING, ex.getContexto());
    }
}

