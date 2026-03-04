
package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.billing.valueObject.RateId;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RateIdTest {

    @Test
    void shouldCreateRateIdSuccessfully() {
        RateId id = RateId.of(100L);
        assertEquals(100L, id.getValue());
        assertEquals("RateId[getValue=100]", id.toString());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        assertThrows(ValueObjectValidationException.class, () -> RateId.of(null));
    }

    @Test
    void shouldRespectEquality() {
        RateId id1 = RateId.of(200L);
        RateId id2 = RateId.of(200L);
        RateId id3 = RateId.of(300L);

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
    }
}

