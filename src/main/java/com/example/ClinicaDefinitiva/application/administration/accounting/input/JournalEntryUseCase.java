package com.example.ClinicaDefinitiva.application.administration.accounting.input;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.AddJournalEntryLineDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.CreateJournalEntryDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.PageJournalEntryDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.ReadJournalEntryDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.UpdateJournalEntryDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface JournalEntryUseCase {
    ReadJournalEntryDto findById(JournalEntryId id, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageJournalEntryDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageJournalEntryDto> findByCompany(CompanyId companyId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageJournalEntryDto> findByDateRange(LocalDate start, LocalDate end, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageJournalEntryDto> findByAccount(LedgerAccountId accountId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageJournalEntryDto> findByThirdParty(ThirdPartiesId thirdPartyId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    ReadJournalEntryDto createJournalEntry(CreateJournalEntryDto dto, UserIdentityId requesterId, RolId requesterRolId);

    ReadJournalEntryDto addLine(JournalEntryId id, AddJournalEntryLineDto dto, UserIdentityId requesterId, RolId requesterRolId);

    ReadJournalEntryDto removeLine(JournalEntryId id, int lineIndex, UserIdentityId requesterId, RolId requesterRolId);

    ReadJournalEntryDto updateInformation(JournalEntryId id, UpdateJournalEntryDto dto, UserIdentityId requesterId, RolId requesterRolId);

    ReadJournalEntryDto post(JournalEntryId id, UserIdentityId requesterId, RolId requesterRolId);

    ReadJournalEntryDto registerRverse(JournalEntryId id, String reason, UserIdentityId requesterId, RolId requesterRolId);
}
