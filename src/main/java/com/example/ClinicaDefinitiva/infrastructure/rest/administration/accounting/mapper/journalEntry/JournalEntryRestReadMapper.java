package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.journalEntry;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.JournalEntryLineDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.PageJournalEntryDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.ReadJournalEntryDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry.JournalEntryLineResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry.PageJournalEntryResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry.ReadJournalEntryResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class JournalEntryRestReadMapper {

    public ReadJournalEntryResponse toRest(ReadJournalEntryDto dto) {
        if (dto == null) return null;

        return new ReadJournalEntryResponse(
                dto.id(),
                dto.companyId(),
                dto.date(),
                dto.documentNumber(),
                dto.description(),
                dto.balanced(),
                dto.posted(),
                dto.totalDebits(),
                dto.totalCredits(),
                dto.lines().stream()
                        .map(this::toLineRest)
                        .collect(Collectors.toList())
        );
    }    private JournalEntryLineResponse toLineRest(JournalEntryLineDto dto) {
        return new JournalEntryLineResponse(
                dto.id(),
                dto.thirdPartyId(),
                dto.description(),
                dto.amount(),
                dto.isDebit(),
           dto.document()

        );
    }

    public PageJournalEntryResponse toPageRest(PageJournalEntryDto dto) {
        if (dto == null) return null;

        return new PageJournalEntryResponse(
                dto.id(),
                dto.documentNumber(),
                dto.description(),
                dto.date(),
                dto.posted(),
                dto.totalDebits()
        );
    }
}
