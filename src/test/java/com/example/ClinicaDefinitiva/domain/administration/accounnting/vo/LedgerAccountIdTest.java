
package com.example.ClinicaDefinitiva.domain.administration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LedgerAccountIdTest {

    @Test
    void shouldCreateValidLedgerAccountId() {
        LedgerAccountId id = LedgerAccountId.of(10L);
        assertEquals(10L, id.getValue()); // compara Long con Long
        assertEquals("LedgerAccountId[getValue=10]", id.toString()); // formato por defecto de record
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> LedgerAccountId.of(null));

        assertEquals(VoAccountingError.ERR_LEDGER_ACCOUNT_ID_NULL, ex.getCatalogo());
        assertEquals(VOContext.ACCOUNTING, ex.getContexto());
    }

    @Test
    void shouldBeEqualWhenValuesAreSame() {
        LedgerAccountId id1 = LedgerAccountId.of(5L);
        LedgerAccountId id2 = LedgerAccountId.of(5L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        LedgerAccountId id1 = LedgerAccountId.of(5L);
        LedgerAccountId id2 = LedgerAccountId.of(6L);

        assertNotEquals(id1, id2);
    }
}

