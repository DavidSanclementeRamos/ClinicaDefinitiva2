package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.journalEntry;

import com.example.ClinicaDefinitiva.application.dto.administration.accounting.journalEntry.BalanceReportResponse;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.journalEntry.JournalEntryLineDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.journalEntry.PageJournalEntryDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.journalEntry.ReadJournalEntryDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntryLine;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
/**
 * Mapper: JournalEntry (Domain → DTO)
 */
@Component
public class JournalEntryReadMapper {

    public ReadJournalEntryDto toReadDto(JournalEntry entry) {
        return new ReadJournalEntryDto(
                entry.getId().getValue(),
                entry.getCompanyId().value(),
                entry.getDate(),
                entry.getDocumentNumber(),
                entry.getDescription(),
                entry.getLines().stream()
                        .map(this::toAddLine)
                        .collect(Collectors.toList()),
                entry.getTotalDebits().asBigDecimal(),
                entry.getTotalCredits().asBigDecimal(),
                entry.isBalanced(),
                entry.isPosted()
        );
    }

    public PageJournalEntryDto toPageDto(JournalEntry entry) {
        return new PageJournalEntryDto(
                entry.getId().getValue(),
                entry.getDate(),
                entry.getDocumentNumber(),
                entry.getDescription(),
                entry.getTotalDebits().asBigDecimal(),
                entry.getTotalCredits().asBigDecimal(),
                entry.isPosted()
        );
    }

    private JournalEntryLineDto toAddLine(JournalEntryLine line) {
        return new JournalEntryLineDto(
                line.getLedgerAccountId().getValue(),
                line.getThirdPartiesId().getValue(),
                line.getDescription(),
                line.getAmount().asBigDecimal(),
                line.getDocumentReference(),
                line.isDebit()
        );
    }

    public BalanceReportResponse toBalanceDto(String accountCode, String accountName,
                                              java.math.BigDecimal debits,
                                              java.math.BigDecimal credits,
                                              java.math.BigDecimal balance) {
        return new BalanceReportResponse(accountCode, accountName, debits, credits, balance);
    }
}