
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntryLine;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JournalEntryTest {

    @Test
    void shouldRegisterJournalEntryWithDefaults() {
        JournalEntry entry = JournalEntry.registerJournalEntry(
                CompanyId.of(1L),
                LocalDate.now(),
                "DOC-001",
                "Registro inicial",
                List.of(
                        JournalEntryLine.of(LedgerAccountId.of(1105L), null, "Caja",
                                Price.of(500, Currency.getInstance("COP")), true, null),
                        JournalEntryLine.of(LedgerAccountId.of(220L), null, "Proveedores",
                                Price.of(500, Currency.getInstance("COP")), false, null)
                )
        );

        assertTrue(entry.isBalanced());
        assertEquals(2, entry.getLineCount());
    }

    @Test
    void shouldAddAndRemoveLine() {
        JournalEntry entry = JournalEntry.registerJournalEntry(
                CompanyId.of(1L),
                LocalDate.now(),
                "DOC-002",
                "Prueba líneas",
                List.of(
                        JournalEntryLine.of(LedgerAccountId.of(1105L), null, "Caja",
                                Price.of(1000, Currency.getInstance("COP")), true, null),
                        JournalEntryLine.of(LedgerAccountId.of(2205L), null, "Proveedores",
                                Price.of(1000, Currency.getInstance("COP")), false, null)
                )
        );

        assertEquals(2, entry.getLineCount());

        entry.removeLine(entry.getLines().get(0));
        assertEquals(1, entry.getLineCount());
    }

    @Test
    void shouldValidateBalanceWithDebitAndCredit() {
        JournalEntry entry = JournalEntry.builder()
                .withCompanyId(CompanyId.of(1L))
                .withDate(LocalDate.now())
                .withDocumentNumber("DOC-003")
                .withDescription("Balance prueba")
                .withLines(List.of(
                        JournalEntryLine.of(LedgerAccountId.of(1105L), null, "Caja",
                                Price.of(500, Currency.getInstance("COP")), true, null),
                        JournalEntryLine.of(LedgerAccountId.of(2205L), null, "Proveedores",
                                Price.of(500, Currency.getInstance("COP")), false, null)
                ))
                .build();

        entry.validateBalance();
        assertTrue(entry.isBalanced());
    }

    @Test
    void shouldThrowExceptionWhenDebitsNotEqualCredits() {
        JournalEntry entry = JournalEntry.builder()
                .withCompanyId(CompanyId.of(1L))
                .withDate(LocalDate.now())
                .withDocumentNumber("DOC-004")
                .withDescription("Desbalance prueba")
                .withLines(List.of(
                        JournalEntryLine.of(LedgerAccountId.of(1105L), null, "Caja",
                                Price.of(600, Currency.getInstance("COP")), true, null),
                        JournalEntryLine.of(LedgerAccountId.of(2205L), null, "Proveedores",
                                Price.of(500, Currency.getInstance("COP")), false, null)
                ))
                .build();

        assertThrows(BusinessRuleViolationException.class, entry::validateBalance);
    }

    @Test
    void shouldReversePostedEntry() {
        JournalEntry entry = JournalEntry.registerJournalEntry(
                CompanyId.of(1L),
                LocalDate.now(),
                "DOC-004",
                "Reversar prueba",
                List.of(
                        JournalEntryLine.of(LedgerAccountId.of(1105L), null, "Caja",
                                Price.of(500, Currency.getInstance("COP")), true, null),
                        JournalEntryLine.of(LedgerAccountId.of(2205L), null, "Proveedores",
                                Price.of(500, Currency.getInstance("COP")), false, null)
                )
        );

        entry.validateBalance();
        entry.post();

        JournalEntry reversed = entry.registerRverse("Error en registro");
        assertEquals("DOC-004-REV", reversed.getDocumentNumber());
        assertTrue(reversed.getDescription().contains("REVERSA"));
    }

    @Test
    void shouldUpdateInformation() {
        JournalEntry entry = JournalEntry.registerJournalEntry(
                CompanyId.of(1L),
                LocalDate.now(),
                "DOC-007",
                "Descripción inicial",
                List.of(
                        JournalEntryLine.of(LedgerAccountId.of(1105L), null, "Caja",
                                Price.of(500, Currency.getInstance("COP")), true, null),
                        JournalEntryLine.of(LedgerAccountId.of(2205L), null, "Proveedores",
                                Price.of(500, Currency.getInstance("COP")), false, null)
                )
        );

        entry.updateInformation("Descripción nueva", "DOC-NEW");
        assertEquals("Descripción nueva", entry.getDescription());
        assertEquals("DOC-NEW", entry.getDocumentNumber());
    }
}