package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.journalEntry;

import com.example.ClinicaDefinitiva.application.dto.administration.accounting.journalEntry.AddJournalEntryLineDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.journalEntry.CreateJournalEntryDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.journalEntry.UpdateJournalEntryDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntryLine;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import java.time.LocalDate;
import java.util.Currency;

import org.springframework.stereotype.Component;

@Component
public class JournalEntryWriteMapper {

    public CompanyId toCompanyId(CreateJournalEntryDto dto) {
        return CompanyId.of(dto.companyId());
    }

    public LocalDate toDate(CreateJournalEntryDto dto) {
        return dto.date();
    }

    public String toDocumentNumber(CreateJournalEntryDto dto) {
        return dto.documentNumber();
    }

    public String toDescription(CreateJournalEntryDto dto) {
        return dto.description();
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
