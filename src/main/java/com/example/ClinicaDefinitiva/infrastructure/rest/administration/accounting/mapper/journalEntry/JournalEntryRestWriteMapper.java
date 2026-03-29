package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.journalEntry;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.AddJournalEntryLineDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.CreateJournalEntryDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.UpdateJournalEntryDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry.AddJournalEntryLineRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry.CreateJournalEntryRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry.UpdateJournalEntryRequest;
import org.springframework.stereotype.Component;

@Component
public class JournalEntryRestWriteMapper {

    public CreateJournalEntryDto toServiceCreate(CreateJournalEntryRequest request) {
        if (request == null) return null;

        return new CreateJournalEntryDto(
                request.companyId(),
                request.date(),
                request.documentNumber(),
                request.description()
        );
    }

    public AddJournalEntryLineDto toServiceAddLine(AddJournalEntryLineRequest request) {
        if (request == null) return null;

        return new AddJournalEntryLineDto(
                request.accountId(),
                request.thirdPartyId(),
                request.description(),
                request.amount(),
                request.documentReference(),
               request.isDebit()

        );
    }

    public UpdateJournalEntryDto toServiceUpdate(UpdateJournalEntryRequest request) {
        if (request == null) return null;

        return new UpdateJournalEntryDto(
                request.description(),
                request.documentNumber()
        );
    }
}