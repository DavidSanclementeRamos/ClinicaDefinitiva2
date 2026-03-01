package com.example.ClinicaDefinitiva.application.service.adminitration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.*;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.CompanyNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.JournalEntryNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.LedgerAccountNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.ThirdPartyNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.journalEntry.JournalEntryReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.journalEntry.JournalEntryWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting.JournalEntryUseCase;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.JournalEntryRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.LedgerAccountRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ThirdPartiesRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JournalEntryApplicationService implements JournalEntryUseCase {

    private final JournalEntryRepository journalEntryRepository;
    private final ThirdPartiesRepository thirdPartiesRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final CompanyRepository companyRepository;
    private final JournalEntryReadMapper readMapper;
    private final JournalEntryWriteMapper writeMapper;
    private final AuthorizationHelper authorizationHelper;

    public JournalEntryApplicationService(JournalEntryRepository journalEntryRepository,
                                          ThirdPartiesRepository thirdPartiesRepository,
                                          LedgerAccountRepository ledgerAccountRepository,
                                          CompanyRepository companyRepository,
                                          JournalEntryReadMapper readMapper,
                                          JournalEntryWriteMapper writeMapper,
                                          AuthorizationHelper authorizationHelper) {
        this.journalEntryRepository = journalEntryRepository;
        this.thirdPartiesRepository = thirdPartiesRepository;
        this.ledgerAccountRepository = ledgerAccountRepository;
        this.companyRepository = companyRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.JOURNAL_ENTRY,
            action = ActionCatalog.BasicAction.READ)
    public ReadJournalEntryDto findById(JournalEntryId id,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {

        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new JournalEntryNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.JOURNAL_ENTRY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        return readMapper.toReadDto(entry);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.JOURNAL_ENTRY,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageJournalEntryDto> findAll(Pageable pageable,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.JOURNAL_ENTRY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return journalEntryRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.JOURNAL_ENTRY,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageJournalEntryDto> findByCompany(CompanyId companyId,
                                                   Pageable pageable,
                                                   UserIdentityId requesterId,
                                                   RolId requesterRolId) {

        companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.JOURNAL_ENTRY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(companyId.getValue())
                        .build()
        );

        return journalEntryRepository.findByCompanyId(companyId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.JOURNAL_ENTRY,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageJournalEntryDto> findByDateRange(LocalDate start,
                                                     LocalDate end,
                                                     Pageable pageable,
                                                     UserIdentityId requesterId,
                                                     RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.JOURNAL_ENTRY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return journalEntryRepository.findByDateRange(start, end, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.JOURNAL_ENTRY,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageJournalEntryDto> findByAccount(LedgerAccountId accountId,
                                                   Pageable pageable,
                                                   UserIdentityId requesterId,
                                                   RolId requesterRolId) {

        ledgerAccountRepository.findById(accountId)
                .orElseThrow(() -> new LedgerAccountNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.JOURNAL_ENTRY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(accountId.getValue())
                        .build()
        );

        return journalEntryRepository.findByAccount(accountId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.JOURNAL_ENTRY,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageJournalEntryDto> findByThirdParty(ThirdPartiesId thirdPartyId,
                                                      Pageable pageable,
                                                      UserIdentityId requesterId,
                                                      RolId requesterRolId) {

        thirdPartiesRepository.findById(thirdPartyId)
                .orElseThrow(() -> new ThirdPartyNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.JOURNAL_ENTRY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(thirdPartyId.getValue())
                        .build()
        );

        return journalEntryRepository.findByThirdParty(thirdPartyId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.JOURNAL_ENTRY,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadJournalEntryDto createJournalEntry(CreateJournalEntryDto dto,
                                                  UserIdentityId requesterId,
                                                  RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.JOURNAL_ENTRY,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder().build()
        );

        JournalEntry entry = writeMapper.fromCreateDto(dto);
        JournalEntry saved = journalEntryRepository.save(entry);

        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.JOURNAL_ENTRY,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadJournalEntryDto addLine(JournalEntryId id,
                                       AddJournalEntryLineDto dto,
                                       UserIdentityId requesterId,
                                       RolId requesterRolId) {

        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new JournalEntryNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.JOURNAL_ENTRY,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        entry.addLine(writeMapper.toAddLineDto(dto));
        JournalEntry updated = journalEntryRepository.save(entry);

        return readMapper.toReadDto(updated);
    }

 

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.JOURNAL_ENTRY,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadJournalEntryDto updateInformation(JournalEntryId id,
                                                 UpdateJournalEntryDto dto,
                                                 UserIdentityId requesterId,
                                                 RolId requesterRolId) {

        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new JournalEntryNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.JOURNAL_ENTRY,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        writeMapper.toUpdateDto(dto, entry);
        JournalEntry updated = journalEntryRepository.save(entry);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.JOURNAL_ENTRY,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadJournalEntryDto post(JournalEntryId id,
                                    UserIdentityId requesterId,
                                    RolId requesterRolId) {

        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new JournalEntryNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.JOURNAL_ENTRY,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        entry.post();
        JournalEntry posted = journalEntryRepository.save(entry);

        return readMapper.toReadDto(posted);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.JOURNAL_ENTRY,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadJournalEntryDto reverse(JournalEntryId id,
                                       String reason,
                                       UserIdentityId requesterId,
                                       RolId requesterRolId) {

        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new JournalEntryNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.JOURNAL_ENTRY,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        entry.reverse(reason);
        JournalEntry reversed = journalEntryRepository.save(entry);

        return readMapper.toReadDto(reversed);
    }

    @Override
    public ReadJournalEntryDto removeLine(JournalEntryId id, int lineIndex, UserIdentityId requesterId, RolId requesterRolId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}