package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.journalEntry;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.AddJournalEntryLineDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.CreateJournalEntryDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.UpdateJournalEntryDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount.ReadLedgerAccountDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;

public class JournalEntryWriteMapper {
    public JournalEntry fromCreateDto(CreateJournalEntryDto dto){
        return JournalEntry.registerJournalEntry(
               CompanyId.of( dto.companyId()),
                dto.date(),
                dto.documentNumber(),
                dto.description()

        );
    }

    public void toUpdateDto(UpdateJournalEntryDto dto, JournalEntry journalEntry){
        journalEntry.updateInformation(
                dto.description(),
                dto.documentNumber()
        );
    }

    public ReadLedgerAccountDto toAddLineDto(AddJournalEntryLineDto dto){
        return new ReadLedgerAccountDto(
                dto.id(),
                dto.thirdPartyId(),
                dto.description(),
                dto.amount(),
                dto.document(),
                dto.isDebit()

        );
    }
}
