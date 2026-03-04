
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ThirdPartiesIdTest {

    @Test
    void shouldCreateValidThirdPartiesId() {
        ThirdPartiesId id = ThirdPartiesId.of(10L);
        assertEquals(10L, id.getValue()); // compara Long con Long
        assertEquals("ThirdPartiesId[getValue=10]", id.toString()); // formato por defecto de record
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> ThirdPartiesId.of(null));

        assertEquals(VoAccountingError.ERR_THIRDPARTIES_ID_NULL, ex.getCatalogo());
        assertEquals(VOContext.ACCOUNTING, ex.getContexto());
    }

    @Test
    void shouldBeEqualWhenValuesAreSame() {
        ThirdPartiesId id1 = ThirdPartiesId.of(5L);
        ThirdPartiesId id2 = ThirdPartiesId.of(5L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        ThirdPartiesId id1 = ThirdPartiesId.of(5L);
        ThirdPartiesId id2 = ThirdPartiesId.of(6L);

        assertNotEquals(id1, id2);
    }
}

