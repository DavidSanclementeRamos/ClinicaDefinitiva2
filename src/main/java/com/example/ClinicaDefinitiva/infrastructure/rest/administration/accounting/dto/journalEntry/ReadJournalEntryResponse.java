package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ReadJournalEntryResponse(
    Long id,
    Long companyId,
    LocalDate date,
    String documentNumber,
    String description,
    boolean balanced,
    boolean posted,
    BigDecimal totalDebits,
    BigDecimal totalCredits,
    List<JournalEntryLineResponse> lines
) {}