
package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Currency;

class CurrencyCodeTest {

    @Test
    void shouldCreateValidCurrencyCode() {
        CurrencyCode code = CurrencyCode.of("USD");
        assertEquals("USD", code.getCode());
        assertEquals(Currency.getInstance("USD"), code.toJavaCurrency());
    }

    @Test
    void shouldNormalizeToUpperCase() {
        CurrencyCode code = CurrencyCode.of("usd");
        assertEquals("USD", code.getCode());
    }

    @Test
    void shouldThrowExceptionWhenCodeIsNull() {
        assertThrows(ValueObjectValidationException.class, () -> CurrencyCode.of(null));
    }

    @Test
    void shouldThrowExceptionWhenCodeIsBlank() {
        assertThrows(ValueObjectValidationException.class, () -> CurrencyCode.of("   "));
    }

    @Test
    void shouldThrowExceptionWhenCodeIsInvalid() {
        assertThrows(ValueObjectValidationException.class, () -> CurrencyCode.of("XYZ123"));
    }

    @Test
    void toStringShouldReturnCode() {
        CurrencyCode code = CurrencyCode.of("EUR");
        assertEquals("EUR", code.toString());
    }
}

