package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.journalEntry;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntryLine;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;
/**
 * Mapper: JournalEntry (Domain → DTO)
 */
@Component
public class JournalEntryReadMapper {
    public ReadJournalEntryDto toReadDto(JournalEntry entry
                                          ) {
        return new ReadJournalEntryDto(
                entry.getId() != null ? entry.getId().getValue() : null,
                entry.getCompanyId() != null ? entry.getCompanyId().getValue() : null,
                entry.getDate(),
                entry.getDocumentNumber(),
                entry.getDescription(),
                entry.getLines().stream().map(this::toAddLine)
                        .collect(Collectors.toList()),
                entry.getTotalDebits().asBigDecimal(),
                entry.getTotalCredits().asBigDecimal(),
                entry.isBalanced(),
                entry.isPosted()
        );
    }

    public PageJournalEntryDto toPageDto(JournalEntry entry) {
        return new PageJournalEntryDto(
                entry.getId() != null ? entry.getId().getValue() : null,
                entry.getDate(),
                entry.getDocumentNumber(),
                entry.getDescription(),
                entry.getTotalDebits().asBigDecimal(),
                entry.getTotalCredits().asBigDecimal(),
                entry.isPosted()
        );
    }

    private JournalEntryLineDto toAddLine(JournalEntryLine line
                                         ) {


        return new JournalEntryLineDto(
                line.getLedgerAccountId() != null ? line.getLedgerAccountId().getValue() : null,

                line.getThirdPartiesId() != null ? line.getThirdPartiesId().getValue() : null,
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

