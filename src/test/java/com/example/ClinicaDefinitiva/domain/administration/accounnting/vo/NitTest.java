
package com.example.ClinicaDefinitiva.domain.administration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Nit;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NitTest {

    @Test
    void shouldCreateValidNitWithoutCheckDigit() {
        Nit nit = Nit.of("123456789");
        assertEquals("123456789", nit.getValue());
    }

    @Test
    void shouldCreateValidNitWithCheckDigit() {
        Nit nit = Nit.of("123456789-0");
        assertEquals("123456789-0", nit.getValue());
    }

    @Test
    void shouldTrimValue() {
        Nit nit = Nit.of("   123456789   ");
        assertEquals("123456789", nit.getValue());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> Nit.of(null));

        assertEquals(VoAccountingError.ERR_NIT_NULL, ex.getCatalogo());
        assertEquals(VOContext.ACCOUNTING, ex.getContexto());
    }

    @Test
    void shouldThrowExceptionWhenFormatIsInvalid() {
        // demasiado corto
        assertThrows(ValueObjectValidationException.class,
            () -> Nit.of("1234"));

        // demasiado largo
        assertThrows(ValueObjectValidationException.class,
            () -> Nit.of("123456789012345"));

        // caracteres no numéricos
        assertThrows(ValueObjectValidationException.class,
            () -> Nit.of("ABC123"));

        // guion mal ubicado
        assertThrows(ValueObjectValidationException.class,
            () -> Nit.of("12345-6789-0"));
    }
}

