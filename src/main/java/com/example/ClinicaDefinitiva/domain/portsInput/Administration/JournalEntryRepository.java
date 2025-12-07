package com.example.ClinicaDefinitiva.domain.portsInput.Administration;

import com.example.ClinicaDefinitiva.domain.administration.contable.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.ThirdPartiesId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;


/**
 * Repositorio para JournalEntry
 */
public interface JournalEntryRepository {
    JournalEntry save(JournalEntry journalEntry);
    Optional<JournalEntry> findById(JournalEntryId id);
    Page<JournalEntry> findByCompanyId(CompanyId companyId);
    Page<JournalEntry> findByDateRange(LocalDate startDate, LocalDate endDate);
    Page<JournalEntry> findByAccount(LedgerAccountId accountId);
    Page<JournalEntry> findByThirdParty(ThirdPartiesId thirdPartiesId);
    Page<JournalEntry> findUnpostedEntries();
    Page<JournalEntry> findByDocumentNumber(String documentNumber);
    boolean existsByDocumentNumber(String documentNumber);
}