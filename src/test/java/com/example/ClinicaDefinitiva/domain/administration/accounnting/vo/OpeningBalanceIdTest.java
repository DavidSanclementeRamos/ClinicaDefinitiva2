
package com.example.ClinicaDefinitiva.domain.administration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.OpeningBalanceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OpeningBalanceIdTest {

    @Test
    void shouldCreateValidOpeningBalanceId() {
        OpeningBalanceId id = OpeningBalanceId.of(10L);
        assertEquals(10L, id.getValue()); // compara Long con Long
        assertEquals("OpeningBalanceId[getValue=10]", id.toString()); // formato por defecto de record
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> OpeningBalanceId.of(null));

        assertEquals(VoAccountingError.ERR_OPENING_BALANCE_ID_NULL, ex.getCatalogo());
        assertEquals(VOContext.ACCOUNTING, ex.getContexto());
    }

    @Test
    void shouldBeEqualWhenValuesAreSame() {
        OpeningBalanceId id1 = OpeningBalanceId.of(5L);
        OpeningBalanceId id2 = OpeningBalanceId.of(5L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        OpeningBalanceId id1 = OpeningBalanceId.of(5L);
        OpeningBalanceId id2 = OpeningBalanceId.of(6L);

        assertNotEquals(id1, id2);
    }
}

