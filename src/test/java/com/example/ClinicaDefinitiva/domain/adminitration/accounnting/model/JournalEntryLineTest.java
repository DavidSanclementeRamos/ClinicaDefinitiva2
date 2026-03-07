
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntryLine;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptions.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.Test;
import java.util.Currency;
import static org.junit.jupiter.api.Assertions.*;

class JournalEntryLineTest {

    @Test
    void shouldCreateDebitLine() {
        JournalEntryLine line = JournalEntryLine.of(
                LedgerAccountId.of(1105L),
                null,
                "Caja",
                Price.of(1000, Currency.getInstance("COP")),
                true,
                null
        );

        assertTrue(line.isDebit());
        assertFalse(line.isCredit());
        assertEquals("Caja", line.getDescription());
        assertEquals("DÉBITO", line.getMovementType());
    }

    @Test
    void shouldCreateCreditLineWithThirdParty() {
        JournalEntryLine line = JournalEntryLine.of(
                LedgerAccountId.of(2205L),
                ThirdPartiesId.of(99L),
                "Proveedor",
                Price.of(500, Currency.getInstance("COP")),
                false,
                "FAC-123"
        );

        assertTrue(line.isCredit());
        assertTrue(line.hasThirdParty());
        assertTrue(line.hasDocumentReference());
        assertEquals("Proveedor", line.getDescription());
        assertEquals("CRÉDITO", line.getMovementType());
    }

    @Test
    void shouldReverseLine() {
        JournalEntryLine debitLine = JournalEntryLine.of(
                LedgerAccountId.of(1105L),
                null,
                "Caja",
                Price.of(200, Currency.getInstance("COP")),
                true,
                null
        );

        JournalEntryLine reversed = debitLine.reverse();

        assertTrue(reversed.isCredit());
        assertEquals("REVERSA: Caja", reversed.getDescription());
        assertEquals(debitLine.getAmount(), reversed.getAmount());
    }

    @Test
    void shouldThrowExceptionForInvalidAmount() {
        assertThrows(BusinessRuleViolationException.class, () ->
            JournalEntryLine.of(
                LedgerAccountId.of(1105L),
                null,
                "Caja",
                Price.of(0, Currency.getInstance("COP")),
                true,
                null
            )
        );
    }

    @Test
    void shouldThrowExceptionForMissingDescription() {
        assertThrows(DomainAggregateException.class, () ->
            JournalEntryLine.of(
                LedgerAccountId.of(2205L),
                null,
                "",
                Price.of(100, Currency.getInstance("COP")),
                false,
                null
            )
        );
    }
}