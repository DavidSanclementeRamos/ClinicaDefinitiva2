package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PageJournalEntryResponse(
    Long id,
    String documentNumber,
    String description,
    LocalDate date,
    boolean posted,
    BigDecimal totalAmount
) {}
