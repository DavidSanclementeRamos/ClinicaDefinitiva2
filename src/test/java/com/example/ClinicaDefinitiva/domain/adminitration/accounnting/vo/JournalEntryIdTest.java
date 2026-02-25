
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JournalEntryIdTest {

    @Test
    void shouldCreateValidJournalEntryId() {
        JournalEntryId id = JournalEntryId.of(10L);
        assertEquals(10L, id.getValue()); // compara Long con Long
        assertEquals("JournalEntryId[getValue=10]", id.toString()); // formato por defecto de record
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> JournalEntryId.of(null));

        assertEquals(VoAccountingError.ERR_JOURNAL_ENTRY_ID_NULL, ex.getCatalogo());
        assertEquals(VOContext.ACCOUNTING, ex.getContexto());
    }

    @Test
    void shouldBeEqualWhenValuesAreSame() {
        JournalEntryId id1 = JournalEntryId.of(5L);
        JournalEntryId id2 = JournalEntryId.of(5L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        JournalEntryId id1 = JournalEntryId.of(5L);
        JournalEntryId id2 = JournalEntryId.of(6L);

        assertNotEquals(id1, id2);
    }
}

