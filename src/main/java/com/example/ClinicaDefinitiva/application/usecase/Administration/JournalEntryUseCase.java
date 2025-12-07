package com.example.ClinicaDefinitiva.application.usecase.Administration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.JournalEntryLineRequest;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface JournalEntryUseCase {
    JournalEntryResponse findJournalEntryById(String id);
    Page<JournalEntryListResponse> listJournalEntriesByCompany(String companyId);
    Page<JournalEntryListResponse> listJournalEntriesByDateRange(LocalDate startDate, LocalDate endDate);
    Page<JournalEntryListResponse> listPostedJournalEntries();
    Page<BalanceReportResponse> GenerateBalanceReport(String companyId, LocalDate startDate, LocalDate endDate);
    JournalEntryResponse registerJournalEntry(CreateJournalEntryRequest request);
    JournalEntryResponse updateJournalEntry(UpdateJournalEntryRequest request);
    JournalEntryResponse addJournalEntryLine(String journalEntryId, JournalEntryLineRequest request);
    JournalEntryResponse postJournalEntry(String journalEntryId);
    JournalEntryResponse reverseJournalEntry(String journalEntryId, ReverseJournalEntryRequest request);

}
