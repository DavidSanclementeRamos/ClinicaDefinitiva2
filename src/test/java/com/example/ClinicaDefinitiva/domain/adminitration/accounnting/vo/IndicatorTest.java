
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Indicator;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class IndicatorTest {

    @Test
    void shouldCreateValidIndicator() {
        Indicator indicator = Indicator.of(" Revenue ", BigDecimal.valueOf(1000.50), " USD ");
        assertEquals("Revenue", indicator.getName()); // trim aplicado
        assertEquals(BigDecimal.valueOf(1000.50), indicator.getValue());
        assertEquals("USD", indicator.getUnit()); // trim aplicado
    }

    @Test
    void shouldThrowExceptionWhenNameIsNullOrBlank() {
        assertThrows(ValueObjectValidationException.class,
            () -> Indicator.of(null, BigDecimal.ONE, "USD"));

        assertThrows(ValueObjectValidationException.class,
            () -> Indicator.of("   ", BigDecimal.ONE, "USD"));
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        assertThrows(ValueObjectValidationException.class,
            () -> Indicator.of("Revenue", null, "USD"));
    }

    @Test
    void shouldThrowExceptionWhenValueIsZeroOrNegative() {
        assertThrows(ValueObjectValidationException.class,
            () -> Indicator.of("Revenue", BigDecimal.ZERO, "USD"));

        assertThrows(ValueObjectValidationException.class,
            () -> Indicator.of("Revenue", BigDecimal.valueOf(-10), "USD"));
    }

    @Test
    void shouldThrowExceptionWhenUnitIsNullOrBlank() {
        assertThrows(ValueObjectValidationException.class,
            () -> Indicator.of("Revenue", BigDecimal.ONE, null));

        assertThrows(ValueObjectValidationException.class,
            () -> Indicator.of("Revenue", BigDecimal.ONE, "   "));
    }
}

