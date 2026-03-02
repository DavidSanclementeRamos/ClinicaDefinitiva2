package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.journalEntry;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.AddJournalEntryLineDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.CreateJournalEntryDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.UpdateJournalEntryDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount.ReadLedgerAccountDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntryLine;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import java.util.Currency;
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


    public String toDescription(UpdateJournalEntryDto dto) {
        return dto.description();
    }

    public String toDocumentNumber(UpdateJournalEntryDto dto) {
        return dto.documentNumber();
    }


    public JournalEntryLine toAddLineDto(AddJournalEntryLineDto dto){
        return  JournalEntryLine.of(
               LedgerAccountId.of( dto.id()),
               ThirdPartiesId.of( dto.thirdPartyId()),
                dto.description(), 
               Price.of(dto.amount(), Currency.getInstance("COP")), 
                dto.isDebit(), 
                dto.document()


        );
    }
}
