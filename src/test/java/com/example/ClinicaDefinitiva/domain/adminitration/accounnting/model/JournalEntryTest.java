
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntryLine;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
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
                        JournalEntryLine.debit(LedgerAccountId.of(1105L), "Caja", Price.of(500, Currency.getInstance("COP"))),
                        JournalEntryLine.credit(LedgerAccountId.of(220L), "Proveedores", Price.of(500, Currency.getInstance("COP")))    
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
                          JournalEntryLine.debit(LedgerAccountId.of(1105L), "Caja", Price.of(1000, Currency.getInstance( "COP"))),
                           JournalEntryLine.credit(LedgerAccountId.of(2205L), "Proveedores", Price.of(1000, Currency.getInstance("COP")))
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
                        JournalEntryLine.debit(LedgerAccountId.of(1105L), "Caja", Price.of(500, Currency.getInstance("COP"))),
                        JournalEntryLine.credit(LedgerAccountId.of(2205L), "Proveedores", Price.of(500, Currency.getInstance("COP")))
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
                        JournalEntryLine.debit(LedgerAccountId.of(1105L), "Caja", Price.of(600, Currency.getInstance( "COP"))),
                        JournalEntryLine.credit(LedgerAccountId.of(2205L), "Proveedores", Price.of(500, Currency.getInstance ( "COP")))
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
                        JournalEntryLine.debit(LedgerAccountId.of(1105L), "Caja", Price.of(500, Currency.getInstance("COP"))),
                        JournalEntryLine.credit(LedgerAccountId.of(2205L), "Proveedores", Price.of(500, Currency.getInstance("COP")))

                )
        );
        
        
        entry.validateBalance();
        entry.post();

        JournalEntry reversed = entry.reverse("Error en registro");
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
                        JournalEntryLine.debit(LedgerAccountId.of(1105L), "Caja", Price.of(500,Currency.getInstance( "COP"))),
                        JournalEntryLine.credit(LedgerAccountId.of(2205L), "Proveedores", Price.of(500, Currency.getInstance( "COP"))
                        )
                )
        );
                entry.updateInformation("Descripción nueva", "DOC-NEW");
        assertEquals("Descripción nueva", entry.getDescription());
        assertEquals("DOC-NEW", entry.getDocumentNumber());
    }
}
