
package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.billing.valueObject.ProviderId;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProviderIdTest {

    @Test
    void shouldCreateProviderIdSuccessfully() {
        ProviderId id = ProviderId.of(10L);
        assertEquals(10L, id.getValue());
        assertEquals("ProviderId[getValue=10]", id.toString());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        assertThrows(ValueObjectValidationException.class, () -> ProviderId.of(null));
    }

    @Test
    void shouldRespectEquality() {
        ProviderId id1 = ProviderId.of(20L);
        ProviderId id2 = ProviderId.of(20L);
        ProviderId id3 = ProviderId.of(30L);

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
    }
}

