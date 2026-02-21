package com.example.ClinicaDefinitiva.application.service.adminitration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.*;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.JournalEntryNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.journalEntry.JournalEntryReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.journalEntry.JournalEntryWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting.JournalEntryUseCase;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.Price;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.JournalEntryRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.LedgerAccountRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ThirdPartiesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class JournalEntryApplicationService implements JournalEntryUseCase {
    private final JournalEntryRepository journalEntryRepository;
    private final ThirdPartiesRepository thirdPartiesRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final CompanyRepository companyRepository;
    private final JournalEntryReadMapper readMapper;
    private final JournalEntryWriteMapper writeMapper;

    public JournalEntryApplicationService(JournalEntryRepository journalEntryRepository, ThirdPartiesRepository thirdPartiesRepository, LedgerAccountRepository ledgerAccountRepository, CompanyRepository companyRepository, JournalEntryReadMapper readMapper, JournalEntryWriteMapper writeMapper) {
        this.journalEntryRepository = journalEntryRepository;
        this.thirdPartiesRepository = thirdPartiesRepository;
        this.ledgerAccountRepository = ledgerAccountRepository;
        this.companyRepository = companyRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    public ReadJournalEntryDto findById(JournalEntryId id, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageJournalEntryDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageJournalEntryDto> findByCompany(CompanyId companyId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageJournalEntryDto> findByDateRange(LocalDate start, LocalDate end, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageJournalEntryDto> findByAccount(LedgerAccountId accountId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageJournalEntryDto> findByThirdParty(ThirdPartiesId thirdPartyId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadJournalEntryDto createJournalEntry(CreateJournalEntryDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadJournalEntryDto addLine(JournalEntryId id, AddJournalEntryLineDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadJournalEntryDto removeLine(JournalEntryId id, int lineIndex, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadJournalEntryDto updateInformation(JournalEntryId id, UpdateJournalEntryDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadJournalEntryDto post(JournalEntryId id, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadJournalEntryDto reverse(JournalEntryId id, String reason, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }
}
