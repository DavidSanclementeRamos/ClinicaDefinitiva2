package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.journalEntry;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntryLine;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.JournalEntryEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.JournalEntryLineEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class JournalEntryWriteEntityMapper {

    public JournalEntryEntity toEntity(JournalEntry entry) {
        if (entry == null) return null;

        JournalEntryEntity entity = new JournalEntryEntity();

         if (entry.getId() != null && entry.getId().getValue() != null) {
            entity.setId(entry.getId().getValue());
        }

        entity.setDate(entry.getDate());
        entity.setDocumentNumber(entry.getDocumentNumber());
        entity.setDescription(entry.getDescription());
        entity.setBalanced(entry.isBalanced());
        entity.setPosted(entry.isPosted());

        entity.setLines(entry.getLines().stream()
                .map(line -> toLineEntity(line, entity))
                .collect(Collectors.toList()));

        return entity;
    }

    private JournalEntryLineEntity toLineEntity(JournalEntryLine line, JournalEntryEntity entryEntity) {
        JournalEntryLineEntity entity = new JournalEntryLineEntity();
        entity.setAccountingEntry(entryEntity);
        entity.setDescription(line.getDescription());
        entity.setAmount(line.getAmount().asBigDecimal());
        entity.setCurrency(line.getAmount().getCurrency().getCurrencyCode());
        entity.setDebit(line.isDebit());
        entity.setDocumentReference(line.getDocumentReference());
        return entity;
    }
}