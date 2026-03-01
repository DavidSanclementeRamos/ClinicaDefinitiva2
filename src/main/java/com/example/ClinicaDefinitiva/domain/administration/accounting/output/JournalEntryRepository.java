package com.example.ClinicaDefinitiva.domain.administration.accounting.output;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Pageable;


/**
 * Repositorio para JournalEntry
 */
public interface JournalEntryRepository {
    JournalEntry save(JournalEntry journalEntry);
    Optional<JournalEntry> findById(JournalEntryId id);
    Page<JournalEntry> findByCompanyId(CompanyId companyId,Pageable pageable);
    Page<JournalEntry> findByDateRange(LocalDate startDate, LocalDate endDate,Pageable pageable);
    Page<JournalEntry> findByAccount(LedgerAccountId accountId,Pageable pageable);
    Page<JournalEntry> findByThirdParty(ThirdPartiesId thirdPartiesId,Pageable pageable);
    Page<JournalEntry> findUnpostedEntries();
    Page<JournalEntry> findByDocumentNumber(String documentNumber);
    boolean existsByDocumentNumber(String documentNumber);

    Page<JournalEntry> findAll(Pageable pageable);



}