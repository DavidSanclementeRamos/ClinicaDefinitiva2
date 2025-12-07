package com.example.ClinicaDefinitiva.application.mapper.Administration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.BalanceReportResponse;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.JournalEntryLineResponse;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.JournalEntryListResponse;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.JournalEntryResponse;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntryLine;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.LedgerAccount;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ThirdPartiesId;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
/**
 * Mapper: JournalEntry (Domain → DTO)
 */
@Component
public class JournalEntryMapper {
    public JournalEntryResponse toResponse(JournalEntry entry,
                                           java.util.Map<LedgerAccountId, LedgerAccount> accounts,
                                           java.util.Map<ThirdPartiesId, ThirdParties> thirdParties) {
        return new JournalEntryResponse(
                entry.getId() != null ? entry.getId().getValue() : null,
                entry.getCompanyId() != null ? entry.getCompanyId().getValue() : null,
                entry.getDate(),
                entry.getDocumentNumber(),
                entry.getDescription(),
                entry.getLines().stream()
                        .map(line -> toLineResponse(line, accounts, thirdParties))
                        .collect(Collectors.toList()),
                entry.getTotalDebits().asBigDecimal(),
                entry.getTotalCredits().asBigDecimal(),
                entry.isBalanced(),
                entry.isPosted()
        );
    }

    public JournalEntryListResponse toListResponse(JournalEntry entry) {
        return new JournalEntryListResponse(
                entry.getId() != null ? entry.getId().getValue() : null,
                entry.getDate(),
                entry.getDocumentNumber(),
                entry.getDescription(),
                entry.getTotalDebits().asBigDecimal(),
                entry.getTotalCredits().asBigDecimal(),
                entry.isPosted()
        );
    }

    private JournalEntryLineResponse toLineResponse(JournalEntryLine line,
                                                    java.util.Map<LedgerAccountId, LedgerAccount> accounts,
                                                    java.util.Map<ThirdPartiesId, ThirdParties> thirdParties) {
        LedgerAccount account = accounts.get(line.getLedgerAccountId());
        ThirdParties thirdParty = line.getThirdPartiesId() != null ?
                thirdParties.get(line.getThirdPartiesId()) : null;

        return new JournalEntryLineResponse(
                line.getLedgerAccountId() != null ? line.getLedgerAccountId().getValue() : null,
                account != null ? account.getCode() : null,
                account != null ? account.getName().getName() : null,
                line.getThirdPartiesId() != null ? line.getThirdPartiesId().getValue() : null,
                thirdParty != null ? thirdParty.getName().getName() : null,
                line.getDescription(),
                line.getAmount().asBigDecimal(),
                line.getAmount().getCurrency(),
                line.isDebit(),
                line.getMovementType(),
                line.getDocumentReference()
        );
    }

    public BalanceReportResponse toBalanceResponse(String accountCode, String accountName,
                                                   java.math.BigDecimal debits,
                                                   java.math.BigDecimal credits,
                                                   java.math.BigDecimal balance) {
        return new BalanceReportResponse(accountCode, accountName, debits, credits, balance);
    }
}

