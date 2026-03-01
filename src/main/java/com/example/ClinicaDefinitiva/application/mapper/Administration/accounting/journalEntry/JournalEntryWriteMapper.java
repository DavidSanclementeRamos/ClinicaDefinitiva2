package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.journalEntry;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.AddJournalEntryLineDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.CreateJournalEntryDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.UpdateJournalEntryDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount.ReadLedgerAccountDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntryLine;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import java.util.List;

public class JournalEntryWriteMapper {
    public JournalEntry fromCreateDto(CreateJournalEntryDto dto){
        return JournalEntry.registerJournalEntry(
               CompanyId.of( dto.companyId()),
                dto.date(),
                dto.documentNumber(),
                dto.description(),
                List.of()

        );
    }

    public void toUpdateDto(UpdateJournalEntryDto dto, JournalEntry journalEntry){
        journalEntry.updateInformation(
                dto.description(),
                dto.documentNumber()
        );
    }

    public JournalEntryLine toAddLineDto(AddJournalEntryLineDto dto){
        return  JournalEntryLine.(
                dto.id(),
                dto.thirdPartyId(),
                dto.description(),
                null,
                dto.document(),
                dto.isDebit(),
                true,
                true,
                1,
                "",
                true,
                true,
                true,
                true



        );
    }
}
