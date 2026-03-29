package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.journalEntry;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntryLine;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.JournalEntryEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.JournalEntryLineEntity;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.stream.Collectors;

@Component
public class JournalEntryReadEntityMapper {

    public JournalEntry toDomain(JournalEntryEntity entity) {
        if (entity == null) return null;

        JournalEntry entry = JournalEntry.registerJournalEntry(
                CompanyId.of(entity.getCompany().getId()),
                entity.getDate(),
                entity.getDocumentNumber(),
                entity.getDescription(),
                entity.getLines().stream()
                        .map(this::toLineDomain)
                        .collect(Collectors.toList())
        );

        // Necesitarás un método reconstruct para establecer ID, balanced, posted
        return entry;
    }

    private JournalEntryLine toLineDomain(JournalEntryLineEntity entity) {
        return JournalEntryLine.of(
                LedgerAccountId.of(entity.getAccount().getId()),
                entity.getThirdParty() != null ? 
                        ThirdPartiesId.of(entity.getThirdParty().getId()) : null,
                entity.getDescription(),
                Price.of(entity.getAmount(), Currency.getInstance(entity.getCurrency())),
                entity.isDebit(),
                entity.getDocumentReference()
        );
    }
}
